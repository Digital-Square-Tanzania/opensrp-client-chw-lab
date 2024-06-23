package org.smartregister.chw.lab.model;

import org.json.JSONObject;
import org.smartregister.chw.lab.contract.BaseManifestDetailsActivityContract;
import org.smartregister.chw.lab.util.LabJsonFormUtils;

public class BaseManifestDetailsModel implements BaseManifestDetailsActivityContract.Model {

    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception {
        JSONObject form = LabJsonFormUtils.getFormAsJson(formName);
        LabJsonFormUtils.getRegistrationForm(form, entityId, currentLocationId);

        return form;
    }
}
