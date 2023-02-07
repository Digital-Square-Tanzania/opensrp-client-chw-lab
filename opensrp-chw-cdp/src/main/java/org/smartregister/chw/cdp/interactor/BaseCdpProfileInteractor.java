package org.smartregister.chw.cdp.interactor;

import static org.smartregister.chw.cdp.util.CdpUtil.addClient;
import static org.smartregister.chw.cdp.util.CdpUtil.addEvent;
import static org.smartregister.chw.cdp.util.CdpUtil.getClientProcessorForJava;
import static org.smartregister.chw.cdp.util.CdpUtil.getSyncHelper;
import static org.smartregister.chw.cdp.util.CdpUtil.updateOpenSRPId;
import static org.smartregister.util.Utils.getAllSharedPreferences;

import androidx.annotation.VisibleForTesting;

import org.smartregister.chw.cdp.contract.BaseCdpProfileContract;
import org.smartregister.chw.cdp.pojo.CdpOutletEventClient;
import org.smartregister.chw.cdp.pojo.RegisterParams;
import org.smartregister.chw.cdp.presenter.BaseCdpProfilePresenter;
import org.smartregister.chw.cdp.util.AppExecutors;
import org.smartregister.chw.cdp.util.CdpUtil;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

public class BaseCdpProfileInteractor implements BaseCdpProfileContract.Interactor {
    protected AppExecutors appExecutors;

    @VisibleForTesting
    BaseCdpProfileInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseCdpProfileInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void refreshProfileInfo() {
        //Implement
    }

    @Override
    public void saveRegistration(final String jsonString, final BaseCdpProfileContract.InteractorCallBack callback) {

        Runnable runnable = () -> {
            try {
                CdpUtil.saveFormEvent(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveRegistration(List<CdpOutletEventClient> cdpOutletEventClientList, String jsonString, RegisterParams registerParams, BaseCdpProfilePresenter baseCdpProfilePresenter) {
        try {
            List<String> currentFormSubmissionIds = new ArrayList<>();

            for (int i = 0; i < cdpOutletEventClientList.size(); i++) {
                try {

                    CdpOutletEventClient allClientEvent = cdpOutletEventClientList.get(i);
                    Client baseClient = allClientEvent.getClient();
                    Event baseEvent = allClientEvent.getEvent();
                    addClient(registerParams, baseClient);
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
