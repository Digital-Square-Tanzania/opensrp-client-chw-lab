package org.smartregister.chw.lab.fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import org.smartregister.chw.lab.contract.BaseLabRegisterFragmentContract;
import org.smartregister.chw.lab.model.BaseLabManifestsRegisterFragmentModel;
import org.smartregister.chw.lab.presenter.BaseLabManifestsRegisterFragmentPresenter;
import org.smartregister.chw.lab.provider.BaseManifestsRegisterProvider;
import org.smartregister.chw.lab.util.DBConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.configurableviews.model.View;
import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.lab.R;
import org.smartregister.util.Utils;
import org.smartregister.view.customcontrols.CustomFontTextView;
import org.smartregister.view.customcontrols.FontVariant;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.HashMap;
import java.util.Set;

public class BaseLabManifestsRegisterFragment extends BaseRegisterFragment implements BaseLabRegisterFragmentContract.View {
    public static final String CLICK_VIEW_NORMAL = "click_view_normal";
    public static final String FOLLOW_UP_VISIT = "follow_up_visit";
    protected Toolbar toolbar;
    protected CustomFontTextView titleView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void initializeAdapter(Set<View> visibleColumns) {
        BaseManifestsRegisterProvider baseManifestsRegisterProvider = new BaseManifestsRegisterProvider(getActivity(), paginationViewHandler, registerActionHandler);
        clientAdapter = new RecyclerViewPaginatedAdapter(null, baseManifestsRegisterProvider, context().commonrepository(this.tablename));
        clientAdapter.setCurrentlimit(20);
        clientsView.setAdapter(clientAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        new android.os.Handler().postDelayed(() -> setupViews(rootView), 1000);
    }

    @Override
    public void setupViews(android.view.View view) {
        super.setupViews(view);

        toolbar = view.findViewById(R.id.register_toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.setContentInsetsRelative(0, 0);
        toolbar.setContentInsetStartWithNavigation(0);

        // Update top left icon
        qrCodeScanImageView = view.findViewById(org.smartregister.R.id.scanQrCode);
        if (qrCodeScanImageView != null) {
            qrCodeScanImageView.setVisibility(android.view.View.GONE);
        }

        // Update Search bar
        android.view.View searchBarLayout = view.findViewById(org.smartregister.R.id.search_bar_layout);
        searchBarLayout.setBackgroundResource(R.color.customAppThemeBlue);

        if (getSearchView() != null) {
            getSearchView().setBackgroundResource(R.color.white);
            getSearchView().setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_search, 0, 0, 0);
        }


        //hide views
        ImageView logo = view.findViewById(org.smartregister.R.id.opensrp_logo_image_view);
        logo.setVisibility(android.view.View.GONE);
        android.view.View topLeftLayout = view.findViewById(R.id.top_left_layout);
        topLeftLayout.setVisibility(android.view.View.GONE);
        android.view.View topRightLayout = view.findViewById(R.id.top_right_layout);
        topRightLayout.setVisibility(android.view.View.VISIBLE);
        android.view.View sortFilterBarLayout = view.findViewById(R.id.register_sort_filter_bar_layout);
        sortFilterBarLayout.setVisibility(android.view.View.GONE);
        android.view.View filterSortLayout = view.findViewById(R.id.filter_sort_layout);
        filterSortLayout.setVisibility(android.view.View.GONE);

        titleView = view.findViewById(R.id.txt_title_label);
        if (titleView != null) {
            titleView.setVisibility(android.view.View.VISIBLE);
            titleView.setText(R.string.manifests);
            titleView.setFontVariant(FontVariant.REGULAR);
        }
    }

    @Override
    public BaseLabRegisterFragmentContract.Presenter presenter() {
        return (BaseLabRegisterFragmentContract.Presenter) presenter;
    }

    @Override
    protected void initializePresenter() {
        if (getActivity() == null) {
            return;
        }
        presenter = new BaseLabManifestsRegisterFragmentPresenter(this, new BaseLabManifestsRegisterFragmentModel(), null);
    }

    @Override
    public void setUniqueID(String uniqueID) {
        if (getSearchView() != null) {
            getSearchView().setText(uniqueID);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_manifests;
    }

    @Override
    public void setAdvancedSearchFormData(HashMap<String, String> hashMap) {
//        implement search here
    }

    @Override
    protected String getMainCondition() {
        return presenter().getMainCondition();
    }

    @Override
    protected String getDefaultSortQuery() {
        return presenter().getDefaultSortQuery();
    }

    @Override
    protected void startRegistration() {
//        start forms here if the module requires registration
    }

    @Override
    protected void onViewClicked(android.view.View view) {
        if (getActivity() == null || !(view.getTag() instanceof CommonPersonObjectClient)) {
            return;
        }

        CommonPersonObjectClient client = (CommonPersonObjectClient) view.getTag();
        if (view.getTag(R.id.VIEW_ID) == CLICK_VIEW_NORMAL) {
            String manifestId = Utils.getValue(client.getColumnmaps(), DBConstants.KEY.MANIFEST_ID, true);
            openProfile(client.getCaseId(), manifestId);
        } else if (view.getTag(R.id.VIEW_ID) == FOLLOW_UP_VISIT) {
            openFollowUpVisit(client.getCaseId());
        }
    }

    protected void openProfile(String baseEntityId, String manifestId) {
    }

    protected void openFollowUpVisit(String baseEntityId) {
//        implement
    }

    @Override
    public void showNotFoundPopup(String s) {
//        implement dialog
    }

    @Override
    protected void refreshSyncProgressSpinner() {
        if (isSyncing()) {
            if (syncProgressBar != null) {
                syncProgressBar.setVisibility(android.view.View.VISIBLE);
            }
        } else {
            if (syncProgressBar != null) {
                syncProgressBar.setVisibility(android.view.View.GONE);
            }
        }
        if (syncButton != null) {
            syncButton.setVisibility(android.view.View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.nav_manifest, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_hvl_manifest) {
            createHvlManifest();
        } else if (item.getItemId() == R.id.action_heid_manifest) {
            createHeidManifest();
        } else if (item.getItemId() == R.id.action_settings) {
            setManifestSettings();
        }
        return true;
    }

    public void createHvlManifest() {
    }

    public void createHeidManifest() {
    }

    public void setManifestSettings() {
    }

}
