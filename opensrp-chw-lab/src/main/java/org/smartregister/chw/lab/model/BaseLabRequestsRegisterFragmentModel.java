package org.smartregister.chw.lab.model;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.smartregister.chw.lab.LabLibrary;
import org.smartregister.chw.lab.contract.BaseLabRegisterFragmentContract;
import org.smartregister.chw.lab.util.ConfigHelper;
import org.smartregister.chw.lab.util.Constants;
import org.smartregister.chw.lab.util.DBConstants;
import org.smartregister.configurableviews.ConfigurableViewsLibrary;
import org.smartregister.configurableviews.model.RegisterConfiguration;
import org.smartregister.configurableviews.model.View;
import org.smartregister.configurableviews.model.ViewConfiguration;
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder;

import java.util.HashSet;
import java.util.Set;

public class BaseLabRequestsRegisterFragmentModel implements BaseLabRegisterFragmentContract.Model {

    @Override
    public RegisterConfiguration defaultRegisterConfiguration() {
        return ConfigHelper.defaultRegisterConfiguration(LabLibrary.getInstance().context().applicationContext());
    }

    @Override
    public ViewConfiguration getViewConfiguration(String viewConfigurationIdentifier) {
        return ConfigurableViewsLibrary.getInstance().getConfigurableViewsHelper().getViewConfiguration(viewConfigurationIdentifier);
    }

    @Override
    public Set<View> getRegisterActiveColumns(String viewConfigurationIdentifier) {
        return ConfigurableViewsLibrary.getInstance().getConfigurableViewsHelper().getRegisterActiveColumns(viewConfigurationIdentifier);
    }

    @Override
    public String countSelect(String tableName, String mainCondition) {
        SmartRegisterQueryBuilder countQueryBuilder = new SmartRegisterQueryBuilder();
        countQueryBuilder.selectInitiateMainTableCounts(tableName);
        return countQueryBuilder.mainCondition(mainCondition);
    }

    @NonNull
    @Override
    public String mainSelect(@NonNull String tableName, @NonNull String mainCondition) {
        SmartRegisterQueryBuilder queryBuilder = new SmartRegisterQueryBuilder();
        queryBuilder.selectInitiateMainTable(tableName, mainColumns(tableName), DBConstants.KEY.BASE_ENTITY_ID);
        queryBuilder.selectInitiateMainTable(tableName, mainColumns(tableName));
        return queryBuilder.mainCondition(mainCondition);
    }

    @NotNull
    public String[] mainColumns(String tableName) {
        Set<String> columnList = new HashSet<>();
        columnList.add(tableName + "." + DBConstants.KEY.BASE_ENTITY_ID);
        columnList.add(tableName + "." + DBConstants.KEY.ENTITY_ID);
        columnList.add(" CASE WHEN printf(\"%.0f\", sample_id) IN (SELECT printf(\"%.0f\", value) FROM ec_lab_manifests, json_each(ec_lab_manifests.samples_list) WHERE ec_lab_manifests.dispatch_date IS NOT NULL) THEN 'Yes' ELSE 'No' END AS dispatched ");
        columnList.add(Constants.TABLES.LAB_TEST_REQUESTS + "." + DBConstants.KEY.RELATIONAL_ID);
        columnList.add(Constants.TABLES.LAB_TEST_REQUESTS + "." + DBConstants.KEY.SAMPLE_REQUEST_DATE);
        columnList.add(Constants.TABLES.LAB_TEST_REQUESTS + "." + DBConstants.KEY.SAMPLE_ID);
        columnList.add(Constants.TABLES.LAB_TEST_REQUESTS + "." + DBConstants.KEY.RESULTS);
        columnList.add(Constants.TABLES.LAB_TEST_REQUESTS + "." + DBConstants.KEY.SAMPLE_TYPE);
        columnList.add(Constants.TABLES.LAB_TEST_REQUESTS + "." + DBConstants.KEY.SAMPLE_COLLECTION_DATE);
        columnList.add(Constants.TABLES.LAB_TEST_REQUESTS + "." + DBConstants.KEY.SAMPLE_PROCESSED);
        columnList.add(Constants.TABLES.LAB_TEST_REQUESTS + "." + DBConstants.KEY.PATIENT_ID);
        columnList.add(Constants.TABLES.LAB_TEST_REQUESTS + "." + DBConstants.KEY.IS_CLOSED);

        return columnList.toArray(new String[columnList.size()]);
    }

}
