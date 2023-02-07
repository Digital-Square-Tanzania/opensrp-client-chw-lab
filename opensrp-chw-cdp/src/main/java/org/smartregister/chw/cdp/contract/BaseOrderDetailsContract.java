package org.smartregister.chw.cdp.contract;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.smartregister.chw.cdp.domain.OrderFeedbackObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;

public interface BaseOrderDetailsContract {

    interface View {
        void setDetailViewWithData(CommonPersonObjectClient pc);

        void setDetailViewWithFeedbackData(OrderFeedbackObject feedbackObject);

        void startFormActivity(JSONObject formJson);

        int getMainContentView();

        void initializePresenter();

        void showOutOfStock();

        void recordOutOfStockResponse();

        void showOutOfStockDialog();

        void hideButtons();

        void showMarkAsReceived();

    }

    interface Presenter {
        void fillViewWithData(CommonPersonObjectClient pc);

        void fillViewWithFeedbackData(OrderFeedbackObject feedbackObject);

        void saveForm(String jsonString);

        void saveMarkAsReceivedForm(String jsonString);

        void startForm(String formName, String entityId, String condomType, Long requestedAtMillis) throws Exception;

        @Nullable
        View getView();

        void refreshViewPageBottom(CommonPersonObjectClient pc);

        void cancelOrderRequest(CommonPersonObjectClient pc);
    }

    interface Interactor {

        void saveForm(CommonPersonObjectClient pc, String jsonString, final InteractorCallBack callBack) throws Exception;

        void saveReceivedStockForm(CommonPersonObjectClient pc, String jsonString, final InteractorCallBack callBack) throws Exception;

        void cancelOrderRequest(CommonPersonObjectClient pc) throws Exception;

        String getOrderStatus(CommonPersonObjectClient pc);

        boolean isRespondingFacility(CommonPersonObjectClient pc);
    }

    interface Model {

        JSONObject getFormAsJson(String formName, String entityId, String condomType, String quantity) throws Exception;

    }

    interface InteractorCallBack {

    }
}
