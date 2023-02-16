package org.smartregister.chw.cdp.presenter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONArray;
import org.json.JSONObject;
import org.smartregister.chw.cdp.contract.BaseCdpProfileContract;
import org.smartregister.chw.cdp.dao.CdpDao;
import org.smartregister.chw.cdp.domain.OutletObject;
import org.smartregister.chw.cdp.pojo.CdpOutletEventClient;
import org.smartregister.chw.cdp.pojo.RegisterParams;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.util.JsonFormUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import timber.log.Timber;


public class BaseCdpProfilePresenter implements BaseCdpProfileContract.Presenter {
    protected WeakReference<BaseCdpProfileContract.View> view;
    protected BaseCdpProfileContract.Interactor interactor;
    protected BaseCdpProfileContract.Model model;
    protected Context context;

    public BaseCdpProfilePresenter(BaseCdpProfileContract.View view, BaseCdpProfileContract.Interactor interactor, BaseCdpProfileContract.Model model, OutletObject outletObject) {
        this.view = new WeakReference<>(view);
        this.interactor = interactor;
        this.model = model;
        fillProfileData(outletObject);
        refreshLastVisitData(outletObject);
    }

    @Override
    public void fillProfileData(OutletObject outletObject) {
        if (outletObject != null && getView() != null) {
            getView().setProfileViewWithData(outletObject);
        }
    }

    @Override
    public void recordCDPButton(@Nullable String visitState) {
        //Implement
    }

    @Override
    public void refreshLastVisitData(OutletObject outletObject) {
        if (outletObject != null && getView() != null) {
            getView().updateLastRecordedStock();
        }
    }

    @Override
    @Nullable
    public BaseCdpProfileContract.View getView() {
        if (view != null && view.get() != null)
            return view.get();

        return null;
    }

    @Override
    public void refreshProfileBottom() {
        interactor.refreshProfileInfo();
    }

    @Override
    public void saveForm(String jsonString) {
        try {
            interactor.saveRegistration(jsonString, getView());
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    public void saveForm(String jsonString, RegisterParams registerParams) {
        //Use this for registration form
        try {
            List<CdpOutletEventClient> cdpOutletEventClientList = model.processRegistration(jsonString);
            if(cdpOutletEventClientList == null || cdpOutletEventClientList.isEmpty()){
                return;
            }
            interactor.saveRegistration(cdpOutletEventClientList, jsonString, registerParams, this);
        } catch (Exception e){
            Timber.e(e);
        }
    }

    @Override
    public void startForm(String formName, String entityId, String metadata, String currentLocationId) throws Exception {
        JSONObject form = model.getFormAsJson(formName, entityId, currentLocationId);

        JSONArray fields = form.getJSONObject(Constants.STEP_ONE).getJSONArray(JsonFormConstants.FIELDS);

        JSONObject numberOfMaleCondoms = JsonFormUtils.getFieldJSONObject(fields, "number_of_male_condoms");
        JSONObject numberOfFemaleCondoms = JsonFormUtils.getFieldJSONObject(fields, "number_of_female_condoms");
        if (numberOfMaleCondoms != null) {
            JSONObject vMax = new JSONObject();
            vMax.put(JsonFormUtils.VALUE, CdpDao.getLastRecordedMaleCondomsStockAtOutlet(entityId));
            vMax.put("err", "Condom count should be less than or equal to the previous restock amount");

            numberOfMaleCondoms.put("v_max", vMax);
        }

        if (numberOfFemaleCondoms != null) {
            JSONObject vMax = new JSONObject();
            vMax.put(JsonFormUtils.VALUE, CdpDao.getLastRecordedFemaleCondomsStockAtOutlet(entityId));
            vMax.put("err", "Condom count should be less than or equal to the previous restock amount");
            numberOfFemaleCondoms.put("v_max", vMax);
        }

        getView().startFormActivity(form);
    }
}
