package org.smartregister.chw.lab.model;

import androidx.annotation.NonNull;

import org.json.JSONObject;
import org.smartregister.chw.lab.contract.BaseLabTestRequestsProfileContract;
import org.smartregister.chw.lab.util.LabJsonFormUtils;

public class BaseLabTestRequestModel implements BaseLabTestRequestsProfileContract.Model {
    @Override
    public JSONObject getFormAsJson(String formName, @NonNull String entityId, String currentLocationId) throws Exception {
        JSONObject form = LabJsonFormUtils.getFormAsJson(formName);
        LabJsonFormUtils.getRegistrationForm(form, entityId, currentLocationId);
        return form;
    }
}
