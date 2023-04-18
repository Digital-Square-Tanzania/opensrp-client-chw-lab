package org.smartregister.chw.cdp.model;

import static com.vijay.jsonwizard.constants.JsonFormConstants.ERR;
import static com.vijay.jsonwizard.constants.JsonFormConstants.FIELDS;
import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.STEP1;
import static com.vijay.jsonwizard.constants.JsonFormConstants.V_MAX;
import static com.vijay.jsonwizard.widgets.TimePickerFactory.KEY.VALUE;
import static org.smartregister.chw.cdp.dao.CdpStockingDao.getCurrentCondomCountByBrand;
import static org.smartregister.chw.cdp.dao.CdpStockingDao.getCurrentFemaleCondomCount;
import static org.smartregister.chw.cdp.dao.CdpStockingDao.getCurrentMaleCondomCount;

import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.cdp.contract.BaseOrderDetailsContract;
import org.smartregister.chw.cdp.dao.CdpStockingDao;
import org.smartregister.chw.cdp.util.CdpJsonFormUtils;
import org.smartregister.util.JsonFormUtils;
import org.smartregister.util.Utils;

import java.util.List;

import timber.log.Timber;

public class BaseOrderDetailsModel implements BaseOrderDetailsContract.Model {
    /**
     * This method returns a JSONObject of a form based on the formName.
     * The form's global section is updated with the current count of male and female condoms
     * and count of condoms available for each brand.
     * Additionally, the form's condom_brand field options are filtered to only include brands with available stock.
     *
     * @param formName   The name of the form.
     * @param entityId   The entity id of the form.
     * @param condomType The type of condom (male or female).
     * @param quantity   The quantity of condoms.
     * @return A JSONObject of the form.
     * @throws Exception if an error occurs while getting the form.
     */
    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String condomType, String quantity) throws Exception {
       return CdpJsonFormUtils.getFormAsJson(formName, condomType, quantity);
    }
}

