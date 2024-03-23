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

public class BaseLabManifestsRegisterFragmentModel implements BaseLabRegisterFragmentContract.Model {

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
        columnList.add(Constants.TABLES.LAB_MANIFESTS + "." + DBConstants.KEY.RELATIONAL_ID);
        columnList.add(Constants.TABLES.LAB_MANIFESTS + "." + DBConstants.KEY.BATCH_NUMBER);
        columnList.add(Constants.TABLES.LAB_MANIFESTS + "." + DBConstants.KEY.MANIFEST_TYPE);
        columnList.add(Constants.TABLES.LAB_MANIFESTS + "." + DBConstants.KEY.DESTINATION_HUB_NAME);
        columnList.add(Constants.TABLES.LAB_MANIFESTS + "." + DBConstants.KEY.DISPATCH_DATE);
        columnList.add(Constants.TABLES.LAB_MANIFESTS + "." + DBConstants.KEY.DISPATCH_TIME);
        columnList.add(Constants.TABLES.LAB_MANIFESTS + "." + DBConstants.KEY.DISPATCHER_NAME);
        columnList.add(Constants.TABLES.LAB_MANIFESTS + "." + DBConstants.KEY.SAMPLE_LIST);
        columnList.add(Constants.TABLES.LAB_MANIFESTS + "." + DBConstants.KEY.IS_CLOSED);

        return columnList.toArray(new String[columnList.size()]);
    }

}
