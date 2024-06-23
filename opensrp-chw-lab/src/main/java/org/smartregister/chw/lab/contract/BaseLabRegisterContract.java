package org.smartregister.chw.lab.contract;

import com.vijay.jsonwizard.domain.Form;

import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONObject;
import org.smartregister.chw.lab.pojo.RegisterParams;
import org.smartregister.view.contract.BaseRegisterContract;

public interface BaseLabRegisterContract {

    interface View extends BaseRegisterContract.View {
        Presenter presenter();

        Form getFormConfig();

        void startFormActivity(JSONObject form, String formName);
    }

    interface Presenter extends BaseRegisterContract.Presenter {

        void startForm(String formName, String entityId, String metadata, String currentLocationId) throws Exception;

        void saveForm(String jsonString);

        void saveManifestForm(String jsonString, String encounterType);

        void saveForm(String jsonString, RegisterParams registerParams);

    }

    interface Model {

        JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception;

    }

    interface Interactor {

        void saveRegistration(String jsonString, final InteractorCallBack callBack);

        void getNextUniqueId(final Triple<String, String, String> triple, final BaseLabRegisterContract.InteractorCallBack callBack);


    }

    interface InteractorCallBack {

        void onRegistrationSaved();

        void onRegistrationSaved(boolean editMode);

        void onNoUniqueId();

        void onUniqueIdFetched(Triple<String, String, String> triple, String entityId);

    }
}
