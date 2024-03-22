package org.smartregister.chw.lab.model;

import org.json.JSONObject;
import org.smartregister.chw.lab.contract.BaseCdpRegisterContract;
import org.smartregister.chw.lab.pojo.CdpOutletEventClient;
import org.smartregister.chw.lab.util.LabJsonFormUtils;

import java.util.List;

public class BaseLabRegisterModel implements BaseCdpRegisterContract.Model {

    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception {
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
