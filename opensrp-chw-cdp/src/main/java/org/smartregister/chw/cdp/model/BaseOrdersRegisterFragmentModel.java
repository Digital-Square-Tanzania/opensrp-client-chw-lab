package org.smartregister.chw.cdp.model;

import static org.smartregister.chw.cdp.dao.CdpStockingDao.getCurrentCondomCountByBrand;
import static org.smartregister.chw.cdp.dao.CdpStockingDao.getCurrentFemaleCondomCount;
import static org.smartregister.chw.cdp.dao.CdpStockingDao.getCurrentMaleCondomCount;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.smartregister.chw.cdp.contract.BaseOrdersRegisterFragmentContract;
import org.smartregister.chw.cdp.dao.CdpStockingDao;
import org.smartregister.chw.cdp.util.CdpJsonFormUtils;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.chw.cdp.util.DBConstants;
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder;
import org.smartregister.util.Utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaseOrdersRegisterFragmentModel implements BaseOrdersRegisterFragmentContract.Model {
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
        queryBuilder.selectInitiateMainTable(tableName, mainColumns(tableName), DBConstants.KEY.LOCATION_ID);
        queryBuilder.selectInitiateMainTable(tableName, mainColumns(tableName));
        queryBuilder.customJoin("INNER JOIN " + Constants.TABLES.TASK + " ON  " + tableName + "." + DBConstants.KEY.BASE_ENTITY_ID + " = " + Constants.TABLES.TASK + "." + DBConstants.KEY.FOR + " COLLATE NOCASE ");
        return queryBuilder.mainCondition(mainCondition);
    }

    @Override
    public JSONObject getOrderFormAsJson(String formName) throws Exception {
        if (formName.equalsIgnoreCase(Constants.FORMS.CDP_CONDOM_ORDER_FACILITY)) {
            JSONObject form = CdpJsonFormUtils.getFormAsJson(formName);
            CdpJsonFormUtils.initializeHealthFacilitiesList(form);
            return form;
        }
        return CdpJsonFormUtils.getFormAsJson(formName);
    }

    @Override
    public JSONObject getDistributionFormAsJson(String formName) throws Exception {
        return CdpJsonFormUtils.getFormAsJson(formName,null, null);
    }

    @NotNull
    public String[] mainColumns(String tableName) {
        Set<String> columnList = new HashSet<>();
        columnList.add(tableName + "." + DBConstants.KEY.LOCATION_ID);
        columnList.add(tableName + "." + DBConstants.KEY.LOCATION_ID + " AS " + DBConstants.KEY.RELATIONAL_ID);
        columnList.add(tableName + "." + DBConstants.KEY.CONDOM_TYPE);
        columnList.add(tableName + "." + DBConstants.KEY.CONDOM_BRAND);
        columnList.add(tableName + "." + DBConstants.KEY.QUANTITY_REQ);
//        columnList.add(tableName + "." + DBConstants.KEY.RECEIVING_ORDER_FACILITY);
        columnList.add(tableName + "." + DBConstants.KEY.REQUEST_TYPE);
        columnList.add(tableName + "." + DBConstants.KEY.FORM_SUBMISSION_ID + " AS " + DBConstants.KEY.REQUEST_REFERENCE);
        columnList.add(tableName + "." + DBConstants.KEY.TEAM_ID);
        columnList.add(Constants.TABLES.TASK + "." + DBConstants.KEY.STATUS);
        columnList.add(Constants.TABLES.TASK + "." + DBConstants.KEY.GROUP_ID + " AS " + DBConstants.KEY.RECEIVING_ORDER_FACILITY);
        columnList.add(Constants.TABLES.TASK + "." + DBConstants.KEY.AUTHORED_ON + " AS " + DBConstants.KEY.REQUESTED_AT);
        columnList.add(Constants.TABLES.TASK + "." + DBConstants.KEY.ID + " AS " + DBConstants.KEY.TASK_ID);
        columnList.add(Constants.TABLES.TASK + "." + DBConstants.KEY.REQUESTER);


        return columnList.toArray(new String[columnList.size()]);
    }
}
