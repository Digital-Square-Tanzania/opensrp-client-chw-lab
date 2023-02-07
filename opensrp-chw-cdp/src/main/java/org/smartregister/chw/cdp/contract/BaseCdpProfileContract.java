package org.smartregister.chw.cdp.contract;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.smartregister.chw.cdp.domain.OutletObject;
import org.smartregister.chw.cdp.pojo.CdpOutletEventClient;
import org.smartregister.chw.cdp.pojo.RegisterParams;
import org.smartregister.chw.cdp.presenter.BaseCdpProfilePresenter;

import java.util.List;

public interface BaseCdpProfileContract {
    interface View extends InteractorCallBack {

        void setProfileViewWithData(OutletObject outletObject);


        void showProgressBar(boolean status);


        void hideView();

        void startFormActivity(JSONObject formJson);

        void updateLastRecordedStock();

        void updateFollowupButton();
    }

    interface Presenter {

        void fillProfileData(@Nullable OutletObject outletObject);

        void saveForm(String jsonString);

        void saveForm(String jsonString, RegisterParams registerParams);

        void startForm(String formName, String entityId, String metadata, String currentLocationId) throws Exception;

        @Nullable View getView();

        void refreshProfileBottom();

        void recordCDPButton(String visitState);

        void refreshLastVisitData(OutletObject outletObject);
    }

    interface Interactor {

        void refreshProfileInfo();

        void saveRegistration(String jsonString, final BaseCdpProfileContract.InteractorCallBack callBack);

        void saveRegistration(List<CdpOutletEventClient> cdpOutletEventClientList, String jsonString, RegisterParams registerParams, BaseCdpProfilePresenter baseCdpProfilePresenter);
    }

    interface Model {

        JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception;

        List<CdpOutletEventClient> processRegistration(String jsonString);
    }


    interface InteractorCallBack {


    }
}