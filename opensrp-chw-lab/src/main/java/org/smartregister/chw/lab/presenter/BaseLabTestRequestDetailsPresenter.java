package org.smartregister.chw.lab.presenter;

import android.content.Context;

import androidx.annotation.Nullable;

import org.json.JSONObject;
import org.smartregister.chw.lab.contract.BaseLabTestRequestsProfileContract;
import org.smartregister.chw.lab.pojo.CdpOutletEventClient;
import org.smartregister.chw.lab.pojo.RegisterParams;

import java.lang.ref.WeakReference;
import java.util.List;

import timber.log.Timber;


public class BaseLabTestRequestDetailsPresenter implements BaseLabTestRequestsProfileContract.Presenter {
    protected WeakReference<BaseLabTestRequestsProfileContract.View> view;
    protected BaseLabTestRequestsProfileContract.Interactor interactor;
    protected BaseLabTestRequestsProfileContract.Model model;
    protected Context context;

    public BaseLabTestRequestDetailsPresenter(BaseLabTestRequestsProfileContract.View view, BaseLabTestRequestsProfileContract.Interactor interactor, BaseLabTestRequestsProfileContract.Model model) {
        this.view = new WeakReference<>(view);
        this.interactor = interactor;
        this.model = model;
        fillProfileData();
        refreshLastVisitData();
    }

    @Override
    public void fillProfileData() {
        getView().setProfileViewWithData();
    }

    @Override
    public void recordCDPButton(@Nullable String visitState) {
        //Implement
    }

    @Override
    public void refreshLastVisitData() {

    }

    @Override
    @Nullable
    public BaseLabTestRequestsProfileContract.View getView() {
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
        getView().startFormActivity(form);
    }
}
