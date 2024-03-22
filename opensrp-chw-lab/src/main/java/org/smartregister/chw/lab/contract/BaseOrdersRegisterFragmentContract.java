package org.smartregister.chw.lab.contract;

import org.json.JSONObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.view.contract.BaseRegisterFragmentContract;

public interface BaseOrdersRegisterFragmentContract {
    interface View extends BaseRegisterFragmentContract.View {

        Presenter presenter();

        Model model();

        void initializeAdapter(String tableName);

        void startOrderForm();

        void showDetails(CommonPersonObjectClient cp);
    }

    interface Presenter extends BaseRegisterFragmentContract.Presenter {
        void setTasksFocus(String taskFocus);

        String getMainCondition();

        String getDefaultSortQuery();
        String getDefaultFilterSortQuery(String filter, String mainSelect, String sortQueries, RecyclerViewPaginatedAdapter clientAdapter);

        String getMainTable();

    }


    interface Model {
        String countSelect(String tableName, String mainCondition);

        String mainSelect(String tableName, String mainCondition);

        JSONObject getOrderFormAsJson(String formName) throws Exception;

        JSONObject getDistributionFormAsJson(String formName) throws Exception;
    }
}
