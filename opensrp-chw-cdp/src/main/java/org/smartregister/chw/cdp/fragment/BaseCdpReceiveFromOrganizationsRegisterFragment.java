package org.smartregister.chw.cdp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;
import org.smartregister.cdp.R;
import org.smartregister.chw.cdp.CdpLibrary;
import org.smartregister.chw.cdp.activity.BaseCdpRegisterActivity;
import org.smartregister.chw.cdp.contract.RestockingHistoryContract;
import org.smartregister.chw.cdp.domain.Visit;
import org.smartregister.chw.cdp.interactor.BaseRestockingHistoryInteractor;
import org.smartregister.chw.cdp.model.BaseRestockingHistoryModel;
import org.smartregister.chw.cdp.presenter.BaseRestockingHistoryPresenter;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.chw.cdp.util.RestockingUtils;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.view.customcontrols.CustomFontTextView;
import org.smartregister.view.customcontrols.FontVariant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseCdpReceiveFromOrganizationsRegisterFragment extends Fragment implements View.OnClickListener, RestockingHistoryContract.View {
    protected View rootView;
    protected RestockingHistoryContract.Presenter presenter;
    protected CustomFontTextView titleView;
    protected LinearLayout linearLayout;
    protected String locationId;
    protected Toolbar toolbar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        rootView = view;
        setupViews(view);
        initializePresenter();
        return view;
    }

    protected void setupViews(View view) {
        toolbar = view.findViewById(R.id.register_toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.setContentInsetsRelative(0, 0);
        toolbar.setContentInsetStartWithNavigation(0);
        AllSharedPreferences allSharedPreferences = CdpLibrary.getInstance().context().allSharedPreferences();
        locationId = allSharedPreferences.fetchUserLocalityId(allSharedPreferences.fetchRegisteredANM());

        linearLayout = view.findViewById(R.id.linearLayoutRestockingHistory);

        Button restockingBtn = view.findViewById(R.id.restock_button);
        restockingBtn.setText(R.string.receive_condoms);

        restockingBtn.setOnClickListener(this);

        //hide views
        ImageView logo = view.findViewById(org.smartregister.R.id.opensrp_logo_image_view);
        logo.setVisibility(android.view.View.GONE);
        android.view.View topLeftLayout = view.findViewById(R.id.top_left_layout);
        topLeftLayout.setVisibility(android.view.View.GONE);
        android.view.View topRightLayout = view.findViewById(R.id.top_right_layout);
        topRightLayout.setVisibility(android.view.View.GONE);

        titleView = view.findViewById(R.id.txt_title_label);
        if (titleView != null) {
            titleView.setVisibility(android.view.View.VISIBLE);
            titleView.setText(R.string.menu_receive_cdp_from_organizations);
            titleView.setFontVariant(FontVariant.REGULAR);
        }
    }


    @LayoutRes
    protected int getLayout() {
        return R.layout.fragment_cdp_receive_from_organization;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.restock_button) {
            try {
                startRestockingForm(Constants.FORMS.CDP_RECEIVE_CONDOM_FROM_ORGANIZATIONS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initializePresenter() {
        presenter = new BaseRestockingHistoryPresenter(this, new BaseRestockingHistoryInteractor(), new BaseRestockingHistoryModel(), locationId);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.initialize();
    }

    @Override
    public RestockingHistoryContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void onDataReceived(List<Visit> visits) {
        getMainLayout().removeAllViews();
        if (visits.size() > 0) {
            for (Visit visit : visits) {
                View view = renderView(visit);
                getMainLayout().addView(view, 0);
            }
        }
    }

    protected void processViewData(Visit visit, View view) {
        List<Map<String, String>> visits_details = new ArrayList<>();
        String[] params = {"condom_restock_date", "condom_type", "male_condom_brand", "female_condom_brand", "restocked_female_condoms", "restocked_male_condoms", "issuing_organization"};
        RestockingUtils.extractVisit(visit, params, visits_details);
        RestockingUtils.processRestockingVisit(visits_details, view, requireActivity());
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public View renderView(Visit visit) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.cdp_restocking_visit_history_details, null);
        processViewData(visit, view);
        return view;
    }

    @Override
    public void startRestockingForm(String formName) throws Exception {
        presenter.startForm(formName, locationId);
    }

    @Override
    public void startFormActivity(JSONObject form) {
        Intent intent = new Intent(getActivity(), BaseCdpRegisterActivity.class);
        intent.putExtra(Constants.JSON_FORM_EXTRA.JSON, form.toString());
        requireActivity().startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    @Override
    public LinearLayout getMainLayout() {
        return linearLayout;
    }
}
