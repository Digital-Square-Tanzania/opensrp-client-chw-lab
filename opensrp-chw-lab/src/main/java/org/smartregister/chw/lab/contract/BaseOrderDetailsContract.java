package org.smartregister.chw.lab.contract;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.smartregister.chw.lab.domain.OrderFeedbackObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;

public interface BaseOrderDetailsContract {

    interface View {
        void setDetailViewWithData(CommonPersonObjectClient pc);

        void setDetailViewWithFeedbackData(OrderFeedbackObject feedbackObject);

        void startFormActivity(JSONObject formJson);

        int getMainContentView();

        void initializePresenter();

        void hideButtons();

    }

    interface Presenter {
        void fillViewWithData(CommonPersonObjectClient pc);

        void fillViewWithFeedbackData(OrderFeedbackObject feedbackObject);

        void saveForm(String jsonString);

        void startForm(String formName, String entityId, String condomType, Long requestedAtMillis) throws Exception;

        @Nullable
        View getView();

        void refreshViewPageBottom(CommonPersonObjectClient pc);
    }

    interface Interactor {

        void saveForm(CommonPersonObjectClient pc, String jsonString, final InteractorCallBack callBack) throws Exception;
    }

    interface Model {

        JSONObject getFormAsJson(String formName) throws Exception;

    }

    interface InteractorCallBack {

    }
}
