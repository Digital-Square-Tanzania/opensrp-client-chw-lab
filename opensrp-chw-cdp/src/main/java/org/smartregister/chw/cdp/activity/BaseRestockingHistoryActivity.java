package org.smartregister.chw.cdp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONObject;
import org.smartregister.cdp.R;
import org.smartregister.chw.cdp.contract.RestockingHistoryContract;
import org.smartregister.chw.cdp.domain.OutletObject;
import org.smartregister.chw.cdp.domain.Visit;
import org.smartregister.chw.cdp.interactor.BaseRestockingHistoryInteractor;
import org.smartregister.chw.cdp.model.BaseRestockingHistoryModel;
import org.smartregister.chw.cdp.presenter.BaseRestockingHistoryPresenter;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.chw.cdp.util.RestockingUtils;
import org.smartregister.view.activity.SecuredActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseRestockingHistoryActivity extends SecuredActivity implements View.OnClickListener, RestockingHistoryContract.View {

    protected OutletObject outletObject;
    protected RestockingHistoryContract.Presenter presenter;
    protected LinearLayout linearLayout;
    protected Toolbar toolbar;
    protected TextView tvTitle;
    private Button restockingBtn;

    public static void startMe(Activity activity, OutletObject outletObject) {
        Intent intent = new Intent(activity, BaseRestockingHistoryActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.OUTLET_OBJECT, outletObject);
        activity.startActivity(intent);
    }


    protected void setupViews() {
        toolbar = findViewById(R.id.collapsing_toolbar);
        tvTitle = findViewById(R.id.tvTitle);

        linearLayout = findViewById(R.id.linearLayoutRestockingHistory);

        restockingBtn = findViewById(R.id.restock_button);

        restockingBtn.setOnClickListener(this);

        tvTitle.setText(getString(R.string.back_to_outlet, outletObject.getOutletName()));
        setUpActionBar();
    }


    private void setUpActionBar() {

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.text_blue), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    protected void onCreation() {
        setContentView(R.layout.cdp_restocking_visit_history);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            outletObject = (OutletObject) getIntent().getSerializableExtra(Constants.ACTIVITY_PAYLOAD.OUTLET_OBJECT);
        }
        setupViews();
        initializePresenter();
    }

    @Override
    protected void onResumption() {
        setupViews();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.restock_button) {
            try {
                startRestockingForm(Constants.FORMS.CDP_OUTLET_RESTOCK);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initializePresenter() {
        presenter = new BaseRestockingHistoryPresenter(this, new BaseRestockingHistoryInteractor(), new BaseRestockingHistoryModel(), outletObject.getBaseEntityId());
    }

    @Override
    public RestockingHistoryContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void onDataReceived(List<Visit> visits) {
        if (visits.size() > 0) {
            restockingBtn.setText(R.string.restock_button_label);
            for (Visit visit : visits) {
                View view = renderView(visit);
                getMainLayout().addView(view, 0);
            }
        } else {
            restockingBtn.setText(R.string.stock_button_label);
        }

    }

    @Override
    public LinearLayout getMainLayout() {
        return linearLayout;
    }

    protected void processViewData(Visit visit, View view) {
        List<Map<String, String>> visits_details = new ArrayList<>();
        String[] params = {"condom_restock_date", "condom_type", "male_condom_brand", "female_condom_brand", "restocked_female_condoms", "restocked_male_condoms"};
        RestockingUtils.extractVisit(visit, params, visits_details);
        RestockingUtils.processRestockingVisit(visits_details, view, this);
    }

    @Override
    public Context getViewContext() {
        return getApplicationContext();
    }

    @Override
    public View renderView(Visit visit) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.cdp_restocking_visit_history_details, null);
        processViewData(visit, view);
        return view;
    }

    @Override
    public void startFormActivity(JSONObject jsonForm) {
        Intent intent = new Intent(this, BaseCdpRegisterActivity.class);
        intent.putExtra(Constants.JSON_FORM_EXTRA.JSON, jsonForm.toString());
        startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    @Override
    public void startRestockingForm(String formName) throws Exception {
        presenter.startForm(formName, outletObject.getBaseEntityId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_GET_JSON && resultCode == RESULT_OK) {
            presenter.saveForm(data.getStringExtra(Constants.JSON_FORM_EXTRA.JSON));
        }
    }
}
