package org.smartregister.chw.lab.contract;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.smartregister.chw.lab.pojo.CdpOutletEventClient;
import org.smartregister.chw.lab.pojo.RegisterParams;
import org.smartregister.chw.lab.presenter.BaseLabTestRequestDetailsPresenter;

import java.util.List;

public interface BaseLabTestRequestsProfileContract {
    interface View extends InteractorCallBack {

        void setProfileViewWithData();


        void showProgressBar(boolean status);


        void hideView();

        void startFormActivity(JSONObject formJson);
    }

    interface Presenter {

        void fillProfileData();

        void saveForm(String jsonString);

        void saveForm(String jsonString, RegisterParams registerParams);

        void startForm(String formName, String entityId, String metadata, String currentLocationId) throws Exception;

        @Nullable View getView();

        void refreshProfileBottom();

        void recordCDPButton(String visitState);

        void refreshLastVisitData();
    }

    interface Interactor {

        void refreshProfileInfo();

        void saveRegistration(String jsonString, final BaseLabTestRequestsProfileContract.InteractorCallBack callBack);

        void saveRegistration(List<CdpOutletEventClient> cdpOutletEventClientList, String jsonString, RegisterParams registerParams, BaseLabTestRequestDetailsPresenter baseLabTestRequestDetailsPresenter);
    }

    interface Model {

        JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception;

        List<CdpOutletEventClient> processRegistration(String jsonString);
    }


    interface InteractorCallBack {


    }
}