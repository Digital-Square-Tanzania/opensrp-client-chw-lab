package org.smartregister.chw.lab.interactor;
import static org.smartregister.chw.lab.util.LabUtil.addEvent;
import static org.smartregister.chw.lab.util.LabUtil.getClientProcessorForJava;
import static org.smartregister.chw.lab.util.LabUtil.getSyncHelper;
import static org.smartregister.chw.lab.util.LabUtil.updateOpenSRPId;
import static org.smartregister.util.Utils.getAllSharedPreferences;

import androidx.annotation.VisibleForTesting;

import org.smartregister.chw.lab.contract.BaseLabTestRequestsProfileContract;
import org.smartregister.chw.lab.pojo.CdpOutletEventClient;
import org.smartregister.chw.lab.pojo.RegisterParams;
import org.smartregister.chw.lab.presenter.BaseLabTestRequestDetailsPresenter;
import org.smartregister.chw.lab.util.AppExecutors;
import org.smartregister.chw.lab.util.LabUtil;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

public class BaseLabTestRequestDetailsInteractor implements BaseLabTestRequestsProfileContract.Interactor {
    protected AppExecutors appExecutors;

    @VisibleForTesting
    BaseLabTestRequestDetailsInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseLabTestRequestDetailsInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void refreshProfileInfo() {
        //Implement
    }

    @Override
    public void saveRegistration(final String jsonString, final BaseLabTestRequestsProfileContract.InteractorCallBack callback) {

        Runnable runnable = () -> {
            try {
                LabUtil.saveFormEvent(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveRegistration(List<CdpOutletEventClient> cdpOutletEventClientList, String jsonString, RegisterParams registerParams, BaseLabTestRequestDetailsPresenter baseLabTestRequestDetailsPresenter) {
        try {
            List<String> currentFormSubmissionIds = new ArrayList<>();

            for (int i = 0; i < cdpOutletEventClientList.size(); i++) {
                try {

                    CdpOutletEventClient allClientEvent = cdpOutletEventClientList.get(i);
                    Client baseClient = allClientEvent.getClient();
                    Event baseEvent = allClientEvent.getEvent();
                    addEvent(registerParams, currentFormSubmissionIds, baseEvent);
                    updateOpenSRPId(jsonString, registerParams, baseClient);
                } catch (Exception e) {
                    Timber.e(e, "ChwAllClientRegisterInteractor --> saveRegistration");
                }
            }

            long lastSyncTimeStamp = getAllSharedPreferences().fetchLastUpdatedAtDate(0);
            Date lastSyncDate = new Date(lastSyncTimeStamp);
            getClientProcessorForJava().processClient(getSyncHelper().getEvents(currentFormSubmissionIds));
            getAllSharedPreferences().saveLastUpdatedAtDate(lastSyncDate.getTime());
        } catch (Exception e) {
            Timber.e(e, "OpdRegisterInteractor --> saveRegistration");
        }
    }
}
