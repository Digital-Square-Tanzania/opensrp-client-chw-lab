package org.smartregister.chw.lab.model;

import org.json.JSONObject;
import org.smartregister.chw.lab.contract.BaseOrderDetailsContract;
import org.smartregister.chw.lab.util.LabJsonFormUtils;

public class BaseOrderDetailsModel implements BaseOrderDetailsContract.Model {
    /**
     * This method returns a JSONObject of a form based on the formName.
     * The form's global section is updated with the current count of male and female condoms
     * and count of condoms available for each brand.
     * Additionally, the form's condom_brand field options are filtered to only include brands with available stock.
     *
     * @param formName   The name of the form.
     * @return A JSONObject of the form.
     * @throws Exception if an error occurs while getting the form.
     */
    @Override
    public JSONObject getFormAsJson(String formName) throws Exception {
       return LabJsonFormUtils.getFormAsJson(formName);
    }
}

