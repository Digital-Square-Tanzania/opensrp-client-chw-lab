package org.smartregister.chw.lab.contract;

import org.smartregister.configurableviews.model.RegisterConfiguration;
import org.smartregister.configurableviews.model.ViewConfiguration;
import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.view.contract.BaseRegisterFragmentContract;

import java.util.Set;

public interface BaseLabRegisterFragmentContract {

    interface View extends BaseRegisterFragmentContract.View {

        void initializeAdapter(Set<org.smartregister.configurableviews.model.View> visibleColumns);

        Presenter presenter();
    }

    interface Presenter extends BaseRegisterFragmentContract.Presenter {

        String getMainCondition();

        String getDefaultSortQuery();

        String getMainTable();

        String getTestSamplesWithResultsQuery();

        String getTestSamplesWithNoResultsQuery();

        String getDefaultFilterSortQuery(String filter, String mainSelect, String sortQueries, RecyclerViewPaginatedAdapter clientAdapter);

        String getDueFilterCondition();
    }

    interface Model {

        RegisterConfiguration defaultRegisterConfiguration();

        ViewConfiguration getViewConfiguration(String viewConfigurationIdentifier);

        Set<org.smartregister.configurableviews.model.View> getRegisterActiveColumns(String viewConfigurationIdentifier);

        String countSelect(String tableName, String mainCondition);

        String mainSelect(String tableName, String mainCondition);
    }
}
