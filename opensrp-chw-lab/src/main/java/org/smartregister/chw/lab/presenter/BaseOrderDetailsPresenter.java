package org.smartregister.chw.lab.presenter;

import static com.vijay.jsonwizard.constants.JsonFormConstants.FIELDS;
import static com.vijay.jsonwizard.constants.JsonFormConstants.MIN_DATE;
import static com.vijay.jsonwizard.constants.JsonFormConstants.STEP1;
import static org.smartregister.chw.lab.util.LabUtil.formatTimeStamp;

import android.content.Context;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.smartregister.chw.lab.contract.BaseOrderDetailsContract;
import org.smartregister.chw.lab.dao.LabDao;
import org.smartregister.chw.lab.domain.OrderFeedbackObject;
import org.smartregister.chw.lab.util.Constants;
import org.smartregister.chw.lab.util.DBConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.util.JsonFormUtils;
import org.smartregister.util.Utils;

import java.lang.ref.WeakReference;

import timber.log.Timber;

public class BaseOrderDetailsPresenter implements BaseOrderDetailsContract.Presenter, BaseOrderDetailsContract.InteractorCallBack {
    protected WeakReference<BaseOrderDetailsContract.View> view;
    protected BaseOrderDetailsContract.Interactor interactor;
    protected BaseOrderDetailsContract.Model model;
    protected CommonPersonObjectClient pc;
    protected OrderFeedbackObject feedbackObject;
    protected Context context;

    public BaseOrderDetailsPresenter(BaseOrderDetailsContract.View view, BaseOrderDetailsContract.Interactor interactor, BaseOrderDetailsContract.Model model, CommonPersonObjectClient pc) {
        this.view = new WeakReference<>(view);
        this.interactor = interactor;
        this.model = model;
        this.pc = pc;
        feedbackObject = LabDao.getFeedbackObject(Utils.getValue(pc, DBConstants.KEY.REQUEST_REFERENCE, false));
        fillViewWithData(pc);
        fillViewWithFeedbackData(feedbackObject);
        refreshViewPageBottom(pc);
    }

    @Override
    public void fillViewWithData(CommonPersonObjectClient pc) {
        if (pc != null && getView() != null) {
            getView().setDetailViewWithData(pc);
        }
    }

    @Override
    public void fillViewWithFeedbackData(OrderFeedbackObject feedbackObject) {
        if (feedbackObject != null && getView() != null) {
            getView().setDetailViewWithFeedbackData(feedbackObject);
        }

    }

    @Override
    public void saveForm(String jsonString) {
        try {
            interactor.saveForm(pc, jsonString, this);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    public void startForm(String formName, String entityId, String condomType, Long requestedAtMillis) throws Exception {
        JSONObject form;
        if (feedbackObject != null) {
            form = model.getFormAsJson(formName);
        } else {
            form = model.getFormAsJson(formName);
        }

        if (formName.equals(Constants.FORMS.CDP_RECEIVE_CONDOM_FACILITY) && requestedAtMillis != null) {
            JSONObject condomReceiveDate = JsonFormUtils.getFieldJSONObject(form.getJSONObject(STEP1).getJSONArray(FIELDS), "condom_receive_date");
            if (condomReceiveDate != null)
                condomReceiveDate.put(MIN_DATE, formatTimeStamp(requestedAtMillis));
        }

        if (getView() != null)
            getView().startFormActivity(form);
    }

    @Override
    public BaseOrderDetailsContract.@Nullable View getView() {
        if (view != null && view.get() != null)
            return view.get();

        return null;
    }


    @Override
    public void refreshViewPageBottom(CommonPersonObjectClient pc) {
//        String status = interactor.getOrderStatus(pc);
//        boolean isRespondingFacility = interactor.isRespondingFacility(pc);
//        if (!status.equalsIgnoreCase(Constants.OrderStatus.READY) && getView() != null) {
//            getView().hideButtons();
//        } else if (getView() != null) {
//            getView().showOutOfStock();
//        }
//        if (!isRespondingFacility && status.equalsIgnoreCase(Constants.OrderStatus.IN_TRANSIT) && getView() != null) {
//            getView().showMarkAsReceived();
//        }
    }
}
