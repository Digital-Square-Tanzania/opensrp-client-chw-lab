package org.smartregister.chw.lab.model;

import androidx.annotation.NonNull;

import org.json.JSONObject;
import org.smartregister.chw.lab.contract.BaseLabTestRequestsProfileContract;
import org.smartregister.chw.lab.pojo.CdpOutletEventClient;
import org.smartregister.chw.lab.util.LabJsonFormUtils;

import java.util.List;

public class BaseCdpProfileModel implements BaseLabTestRequestsProfileContract.Model {
    @Override
    public JSONObject getFormAsJson(String formName, @NonNull String entityId, String currentLocationId) throws Exception {
        JSONObject form = LabJsonFormUtils.getFormAsJson(formName);
        LabJsonFormUtils.getRegistrationForm(form, entityId, currentLocationId);
        return form;
    }

    @Override
    public List<CdpOutletEventClient> processRegistration(String jsonString) {
//        return OutletUtil.getOutletEventClient(jsonString);
        return null;
    }
}
