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
        JSONObject form = CdpJsonFormUtils.getFormAsJson(formName);
        JSONObject global = form.getJSONObject("global");
        List<String> condomBrands = CdpStockingDao.getCondomBrands();

        // Get user location id
        String userLocationId = Utils.getAllSharedPreferences().fetchUserLocalityId(Utils.getAllSharedPreferences().fetchRegisteredANM());

        // Add current male and female condom count to global
        global.put("male_condom_count", getCurrentMaleCondomCount(userLocationId));
        global.put("female_condom_count", getCurrentFemaleCondomCount(userLocationId));

        // If condom type is provided, add it to the form data
        if (condomType != null) {
            global.put("condom_type", condomType);
        }

        // If quantity is provided, add it to the form data
        if (quantity != null) {
            global.put("condom_quantity", quantity);
        }

        // Get form fields
        JSONArray fields = form.getJSONObject(STEP1).getJSONArray(FIELDS);

        // String to store condom brand counts information to be passed to the form
        StringBuilder condomBrandCounts = new StringBuilder("<p>Condom Stock On Hand</p>");
        for (String condomBrand : condomBrands) {
            global.put("male_condom_" + condomBrand + "_count", getCurrentCondomCountByBrand(condomBrand, CdpStockingDao.CondomStockLog.CondomType.MALE));
            global.put("female_condom_" + condomBrand + "_count", getCurrentCondomCountByBrand(condomBrand, CdpStockingDao.CondomStockLog.CondomType.FEMALE));

            String brandName = condomBrand.replace("_", " ");
            brandName = brandName.substring(0, 1).toUpperCase() + brandName.substring(1);
            condomBrandCounts.append("<p><b>Brand:</b> ").append(brandName).append(" <b><i>Male Condoms</i>:</b> ").append(getCurrentCondomCountByBrand(condomBrand, CdpStockingDao.CondomStockLog.CondomType.MALE)).append(" <b><i>Female Condoms</i></b>: ").append(getCurrentCondomCountByBrand(condomBrand, CdpStockingDao.CondomStockLog.CondomType.FEMALE)).append("</p>");
        }

        JSONObject numberOfCondomsAvailable = JsonFormUtils.getFieldJSONObject(fields, "number_of_condoms_available");
        if (numberOfCondomsAvailable != null) {
            numberOfCondomsAvailable.put("text", condomBrandCounts.toString());
        }


        try {
            JSONArray condomBrandFields = JsonFormUtils.getFieldJSONObject(fields, "condom_brand").getJSONArray("options");
            for (int i = condomBrandFields.length() - 1; i >= 0; i--) {
                JSONObject condomBrand = condomBrandFields.getJSONObject(i);
                String brandName = condomBrand.getString(KEY);

                if (!condomBrands.contains(brandName) || (getCurrentCondomCountByBrand(brandName, CdpStockingDao.CondomStockLog.CondomType.MALE) == 0 && getCurrentCondomCountByBrand(brandName, CdpStockingDao.CondomStockLog.CondomType.FEMALE) == 0)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        condomBrandFields.remove(i);
                    }
                } else {
                    addFormConstraints(fields, brandName);
                }
            }
        } catch (Exception e) {
            Timber.e(e);
        }

        return form;
    }

    private void addFormConstraints(JSONArray fields, String brandName) throws JSONException {
        JSONObject maleCondomsObject = JsonFormUtils.getFieldJSONObject(fields, "provided_" + CdpStockingDao.CondomStockLog.CondomType.MALE.toString().toLowerCase() + "_condoms_" + brandName);
        JSONObject femaleCondomsObject = JsonFormUtils.getFieldJSONObject(fields, "provided_" + CdpStockingDao.CondomStockLog.CondomType.FEMALE.toString().toLowerCase() + "_condoms_" + brandName);


        JSONObject maxMaleValue = new JSONObject();
        maxMaleValue.put(VALUE, getCurrentCondomCountByBrand(brandName, CdpStockingDao.CondomStockLog.CondomType.MALE));
        maxMaleValue.put(ERR, "Distributed condoms should be equal to or less than available condoms");
        maleCondomsObject.put(V_MAX, maxMaleValue);

        JSONObject maxFemaleValue = new JSONObject();
        maxFemaleValue.put(VALUE, getCurrentCondomCountByBrand(brandName, CdpStockingDao.CondomStockLog.CondomType.FEMALE));
        maxFemaleValue.put(ERR, "Distributed condoms should be equal to or less than available condoms");
        femaleCondomsObject.put(V_MAX, maxFemaleValue);
    }
}

