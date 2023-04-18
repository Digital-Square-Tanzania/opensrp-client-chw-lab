package org.smartregister.chw.cdp.util;

import static com.vijay.jsonwizard.constants.JsonFormConstants.ERR;
import static com.vijay.jsonwizard.constants.JsonFormConstants.FIELDS;
import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.STEP1;
import static com.vijay.jsonwizard.constants.JsonFormConstants.V_MAX;
import static com.vijay.jsonwizard.widgets.TimePickerFactory.KEY.VALUE;
import static org.smartregister.chw.cdp.dao.CdpStockingDao.getCurrentCondomCountByBrand;
import static org.smartregister.chw.cdp.dao.CdpStockingDao.getCurrentFemaleCondomCount;
import static org.smartregister.chw.cdp.dao.CdpStockingDao.getCurrentMaleCondomCount;
import static org.smartregister.chw.cdp.util.Constants.ENCOUNTER_TYPE;
import static org.smartregister.chw.cdp.util.Constants.STEP_ONE;
import static org.smartregister.chw.cdp.util.Constants.STEP_TWO;

import android.os.Build;
import android.util.Log;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.cdp.CdpLibrary;
import org.smartregister.chw.cdp.dao.CdpStockingDao;
import org.smartregister.chw.cdp.repository.LocationWithTagsRepository;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.domain.Location;
import org.smartregister.domain.LocationTag;
import org.smartregister.domain.tag.FormTag;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.util.FormUtils;
import org.smartregister.util.JsonFormUtils;
import org.smartregister.util.Utils;

import java.util.List;
import java.util.Set;

import timber.log.Timber;

public class CdpJsonFormUtils extends org.smartregister.util.JsonFormUtils {
    public static final String METADATA = "metadata";
    public static final String OPENSRP_ID = "opensrp_id";
    public static final String CURRENT_OPENSRP_ID = "current_opensrp_id";

    public static Triple<Boolean, JSONObject, JSONArray> validateParameters(String jsonString) {

        JSONObject jsonForm = toJSONObject(jsonString);
        JSONArray fields = cdpFormFields(jsonForm);

        Triple<Boolean, JSONObject, JSONArray> registrationFormParams = Triple.of(jsonForm != null && fields != null, jsonForm, fields);
        return registrationFormParams;
    }

    public static JSONArray cdpFormFields(JSONObject jsonForm) {
        try {
            JSONArray fieldsOne = fields(jsonForm, STEP_ONE);
            JSONArray fieldsTwo = fields(jsonForm, STEP_TWO);
            if (fieldsTwo != null) {
                for (int i = 0; i < fieldsTwo.length(); i++) {
                    fieldsOne.put(fieldsTwo.get(i));
                }
            }
            return fieldsOne;

        } catch (JSONException e) {
            Log.e(TAG, "", e);
        }
        return null;
    }

    public static JSONArray fields(JSONObject jsonForm, String step) {
        try {

            JSONObject step1 = jsonForm.has(step) ? jsonForm.getJSONObject(step) : null;
            if (step1 == null) {
                return null;
            }

            return step1.has(FIELDS) ? step1.getJSONArray(FIELDS) : null;

        } catch (JSONException e) {
            Log.e(TAG, "", e);
        }
        return null;
    }

    public static Event processJsonForm(AllSharedPreferences allSharedPreferences, String
            jsonString) {

        Triple<Boolean, JSONObject, JSONArray> registrationFormParams = validateParameters(jsonString);

        if (!registrationFormParams.getLeft()) {
            return null;
        }

        JSONObject jsonForm = registrationFormParams.getMiddle();
        JSONArray fields = registrationFormParams.getRight();
        String entityId = getString(jsonForm, ENTITY_ID);
        String encounter_type = jsonForm.optString(Constants.JSON_FORM_EXTRA.ENCOUNTER_TYPE);

        if (Constants.EVENT_TYPE.CDP_RESTOCK.equals(encounter_type)) {
            encounter_type = Constants.TABLES.CDP_OUTLET_STOCK_COUNT;
        } else if (Constants.EVENT_TYPE.CDP_OUTLET_VISIT.equals(encounter_type)) {
            encounter_type = Constants.TABLES.CDP_OUTLET_VISIT;
        }
        return org.smartregister.util.JsonFormUtils.createEvent(fields, getJSONObject(jsonForm, METADATA), formTag(allSharedPreferences), entityId, getString(jsonForm, ENCOUNTER_TYPE), encounter_type);
    }

    public static FormTag formTag(AllSharedPreferences allSharedPreferences) {
        FormTag formTag = new FormTag();
        formTag.providerId = allSharedPreferences.fetchRegisteredANM();
        formTag.appVersion = CdpLibrary.getInstance().getApplicationVersion();
        formTag.databaseVersion = CdpLibrary.getInstance().getDatabaseVersion();
        return formTag;
    }

    public static void tagEvent(AllSharedPreferences allSharedPreferences, Event event) {
        String providerId = allSharedPreferences.fetchRegisteredANM();
        event.setProviderId(providerId);
        event.setLocationId(locationId(allSharedPreferences));
        event.setChildLocationId(allSharedPreferences.fetchCurrentLocality());
        event.setTeam(allSharedPreferences.fetchDefaultTeam(providerId));
        event.setTeamId(allSharedPreferences.fetchDefaultTeamId(providerId));

        event.setClientApplicationVersion(CdpLibrary.getInstance().getApplicationVersion());
        event.setClientDatabaseVersion(CdpLibrary.getInstance().getDatabaseVersion());
    }

    private static String locationId(AllSharedPreferences allSharedPreferences) {
        String providerId = allSharedPreferences.fetchRegisteredANM();
        String userLocationId = allSharedPreferences.fetchUserLocalityId(providerId);
        if (StringUtils.isBlank(userLocationId)) {
            userLocationId = allSharedPreferences.fetchDefaultLocalityId(providerId);
        }

        return userLocationId;
    }

    public static void getRegistrationForm(JSONObject jsonObject, String entityId, String
            currentLocationId) throws JSONException {
        jsonObject.getJSONObject(METADATA).put(ENCOUNTER_LOCATION, currentLocationId);
        jsonObject.put(org.smartregister.util.JsonFormUtils.ENTITY_ID, entityId);
        jsonObject.put(DBConstants.KEY.RELATIONAL_ID, entityId);
    }

    public static void getVisitForm(JSONObject jsonObject, String entityId, String currentLocationId) throws JSONException {
        jsonObject.getJSONObject(METADATA).put(ENCOUNTER_LOCATION, currentLocationId);
        jsonObject.put(org.smartregister.util.JsonFormUtils.ENTITY_ID, entityId);
    }

    public static JSONObject getFormAsJson(String formName) throws Exception {
        return FormUtils.getInstance(CdpLibrary.getInstance().context().applicationContext()).getFormJson(formName);
    }

    public static JSONObject getFormAsJson(String formName, String condomType, String quantity) throws Exception {
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

    private static void addFormConstraints(JSONArray fields, String brandName) throws JSONException {
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

    public static void initializeHealthFacilitiesList(JSONObject form) {
        AllSharedPreferences allSharedPreferences = CdpLibrary.getInstance().context().allSharedPreferences();

        String providerId = allSharedPreferences.fetchRegisteredANM();
        String userLocationId = allSharedPreferences.fetchUserLocalityId(providerId);

        LocationWithTagsRepository locationRepository = new LocationWithTagsRepository();
        List<Location> locations = locationRepository.getAllLocationsWithTags();
        if (locations != null && form != null) {

            try {

                JSONArray fields = form.getJSONObject(STEP_ONE)
                        .getJSONArray(JsonFormConstants.FIELDS);

                JSONObject referralHealthFacilities = JsonFormUtils.getFieldJSONObject(fields, Constants.JSON_FORM_KEY.RECEIVING_ORDER_FACILITY);

                JSONArray options = referralHealthFacilities.getJSONArray("options");
                String healthFacilityWithMsdCodeTagName = "Facility";
                for (Location location : locations) {
                    Set<LocationTag> locationTags = location.getLocationTags();
                    if (locationTags.iterator().next().getName().equalsIgnoreCase(healthFacilityWithMsdCodeTagName) && !location.getProperties().getUid().equalsIgnoreCase(userLocationId)) {
                        JSONObject optionNode = new JSONObject();
                        optionNode.put("text", StringUtils.capitalize(location.getProperties().getName()));
                        optionNode.put("key", StringUtils.capitalize(location.getProperties().getName()));
                        optionNode.put("openmrs_entity_parent", "");
                        optionNode.put("openmrs_entity", "concept");
                        optionNode.put("openmrs_entity_id", location.getProperties().getUid());

                        JSONObject propertyObject = new JSONObject();
                        propertyObject.put("presumed-id", location.getProperties().getUid());
                        propertyObject.put("confirmed-id", location.getProperties().getUid());
                        optionNode.put("property", propertyObject);

                        options.put(optionNode);
                    }
                }
            } catch (JSONException e) {
                Timber.e(e);
            }
        }
    }

}
