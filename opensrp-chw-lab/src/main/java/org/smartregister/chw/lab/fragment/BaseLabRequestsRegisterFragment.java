package org.smartregister.chw.lab.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.android.material.tabs.TabLayout;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.lab.LabLibrary;
import org.smartregister.chw.lab.activity.BaseLabTestRequestDetailsActivity;
import org.smartregister.chw.lab.contract.BaseLabRegisterFragmentContract;
import org.smartregister.chw.lab.model.BaseLabRequestsRegisterFragmentModel;
import org.smartregister.chw.lab.presenter.BaseLabRequestsRegisterFragmentPresenter;
import org.smartregister.chw.lab.provider.BaseLabRegisterProvider;
import org.smartregister.chw.lab.util.DBConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.configurableviews.model.View;
import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.lab.R;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.util.Utils;
import org.smartregister.view.customcontrols.CustomFontTextView;
import org.smartregister.view.customcontrols.FontVariant;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.HashMap;
import java.util.Set;

import timber.log.Timber;

public class BaseLabRequestsRegisterFragment extends BaseRegisterFragment implements BaseLabRegisterFragmentContract.View {
    public static final String CLICK_VIEW_NORMAL = "click_view_normal";
    public static final String FOLLOW_UP_VISIT = "follow_up_visit";
    protected Toolbar toolbar;
    protected CustomFontTextView titleView;

    protected String customGroupFilter;

    @Override
    public void initializeAdapter(Set<View> visibleColumns) {
        BaseLabRegisterProvider baseLabRegisterProvider = new BaseLabRegisterProvider(getActivity(), paginationViewHandler, registerActionHandler, visibleColumns);
        clientAdapter = new RecyclerViewPaginatedAdapter(null, baseLabRegisterProvider, context().commonrepository(this.tablename));
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
        AllSharedPreferences allSharedPreferences = LabLibrary.getInstance().context().allSharedPreferences();

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
            titleView.setText(R.string.test_requests);
            titleView.setFontVariant(FontVariant.REGULAR);
        }

        setUpTabLayout(view);
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
        presenter = new BaseLabRequestsRegisterFragmentPresenter(this, new BaseLabRequestsRegisterFragmentModel(), null);
    }

    @Override
    public void setUniqueID(String uniqueID) {
        if (getSearchView() != null) {
            getSearchView().setText(uniqueID);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_sample_requests;
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
            String sampleId = Utils.getValue(client.getColumnmaps(), DBConstants.KEY.SAMPLE_ID, true);
            openProfile(Utils.getValue(client.getColumnmaps(), DBConstants.KEY.ENTITY_ID, true), sampleId);
        } else if (view.getTag(R.id.VIEW_ID) == FOLLOW_UP_VISIT) {
            openFollowUpVisit(client.getCaseId());
        }
    }

    protected void openProfile(String baseEntityId, String testSampleId) {
        BaseLabTestRequestDetailsActivity.startProfileActivity(getActivity(), baseEntityId, testSampleId, false);
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

    protected void setUpTabLayout(android.view.View view) {
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setVisibility(android.view.View.VISIBLE);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        customGroupFilter = presenter().getTestSamplesWithNoResultsQuery();
                        ;
                        filterandSortExecute();
                        break;
                    case 1:
                        customGroupFilter = presenter().getTestSamplesWithResultsQuery();
                        filterandSortExecute();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //do something
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //do something
            }
        });


        if (tabLayout.getSelectedTabPosition() == 0) {
            customGroupFilter = presenter().getTestSamplesWithNoResultsQuery();
            filterandSortExecute();
        } else {
            customGroupFilter = presenter().getTestSamplesWithResultsQuery();
            filterandSortExecute();
        }
    }


    @Override
    public void countExecute() {
        Cursor c = null;
        try {

            String query = "select count(*) from " + presenter().getMainTable() +
                    " where " + presenter().getMainCondition();


            if (StringUtils.isNotBlank(customGroupFilter)) {
                query = query + " and ( " + customGroupFilter + " ) ";
            }

            c = commonRepository().rawCustomQueryForAdapter(query);
            c.moveToFirst();
            clientAdapter.setTotalcount(c.getInt(0));
            Timber.v("total count here %s", clientAdapter.getTotalcount());

            clientAdapter.setCurrentlimit(20);
            clientAdapter.setCurrentoffset(0);

        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return new CursorLoader(requireActivity()) {
                @Override
                public Cursor loadInBackground() {
                    // Count query
                    final String COUNT = "count_execute";
                    if (args != null && args.getBoolean(COUNT)) {
                        countExecute();
                    }
                    String query = presenter().getDefaultFilterSortQuery(customGroupFilter, mainSelect, Sortqueries, clientAdapter);
                    return commonRepository().rawCustomQueryForAdapter(query);
                }
            };
        }
        return super.onCreateLoader(id, args);
    }

}
