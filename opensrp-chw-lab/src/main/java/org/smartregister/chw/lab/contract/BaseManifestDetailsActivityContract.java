package org.smartregister.chw.lab.contract;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public interface BaseManifestDetailsActivityContract {

    interface View {
        void setDetailViewWithData(String batchNumber);

        void startFormActivity(JSONObject formJson);

        int getMainContentView();

    }

    interface Presenter {

        void saveForm(String jsonString);

        void startForm(String formName, String entityId, String metadata, String currentLocationId) throws Exception;

        @Nullable
        View getView();
    }

    interface Interactor {
        void saveRegistration(String jsonString, final BaseManifestDetailsActivityContract.InteractorCallBack callBack);
    }

    interface Model {

        JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception;

    }

    interface InteractorCallBack {
        void onRegistrationSaved();
    }
}
