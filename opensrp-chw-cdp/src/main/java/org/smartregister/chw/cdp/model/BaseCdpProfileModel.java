package org.smartregister.chw.cdp.model;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONArray;
import org.json.JSONObject;
import org.smartregister.chw.cdp.contract.BaseCdpProfileContract;
import org.smartregister.chw.cdp.dao.CdpDao;
import org.smartregister.chw.cdp.pojo.CdpOutletEventClient;
import org.smartregister.chw.cdp.util.CdpJsonFormUtils;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.chw.cdp.util.OutletUtil;
import org.smartregister.util.JsonFormUtils;

import androidx.annotation.NonNull;

import java.util.List;

import timber.log.Timber;

public class BaseCdpProfileModel implements BaseCdpProfileContract.Model {
    @Override
    public JSONObject getFormAsJson(String formName, @NonNull String entityId, String currentLocationId) throws Exception {
        JSONObject form = CdpJsonFormUtils.getFormAsJson(formName);
        CdpJsonFormUtils.getRegistrationForm(form, entityId, currentLocationId);
        if (formName.equals(Constants.FORMS.CD_OUTLET_VISIT)) {
            try {
                JSONArray fields = form.getJSONObject(Constants.STEP_ONE).getJSONArray(JsonFormConstants.FIELDS);
                JSONObject visitNumber = org.smartregister.util.JsonFormUtils.getFieldJSONObject(fields, "visit_number");
                assert visitNumber != null;
                visitNumber.put(JsonFormUtils.VALUE, CdpDao.getNextOutletVisitNumber(entityId));
            } catch (Exception e) {
                Timber.e(e);
            }
        }

        return form;
    }

    @Override
    public List<CdpOutletEventClient> processRegistration(String jsonString) {
        return OutletUtil.getOutletEventClient(jsonString);
    }
}
