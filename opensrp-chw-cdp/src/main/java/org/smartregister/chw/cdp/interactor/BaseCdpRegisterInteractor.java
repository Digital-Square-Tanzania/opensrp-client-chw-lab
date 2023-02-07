package org.smartregister.chw.cdp.interactor;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.cdp.CdpLibrary;
import org.smartregister.chw.cdp.contract.BaseCdpRegisterContract;
import org.smartregister.chw.cdp.pojo.CdpOutletEventClient;
import org.smartregister.chw.cdp.pojo.RegisterParams;
import org.smartregister.chw.cdp.util.AppExecutors;
import org.smartregister.chw.cdp.util.CdpJsonFormUtils;
import org.smartregister.chw.cdp.util.CdpUtil;
import org.smartregister.chw.cdp.util.OutletJsonFormUtil;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.domain.UniqueId;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.UniqueIdRepository;
import org.smartregister.sync.helper.ECSyncHelper;
import org.smartregister.util.JsonFormUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import timber.log.Timber;

import static org.smartregister.chw.cdp.util.CdpUtil.addClient;
import static org.smartregister.chw.cdp.util.CdpUtil.addEvent;
import static org.smartregister.chw.cdp.util.CdpUtil.getClientProcessorForJava;
import static org.smartregister.chw.cdp.util.CdpUtil.getUniqueIdRepository;
import static org.smartregister.chw.cdp.util.CdpUtil.updateOpenSRPId;

public class BaseCdpRegisterInteractor implements BaseCdpRegisterContract.Interactor {

    private final AppExecutors appExecutors;

    @VisibleForTesting
    BaseCdpRegisterInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseCdpRegisterInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void saveRegistration(final String jsonString, final BaseCdpRegisterContract.InteractorCallBack callBack) {

        Runnable runnable = () -> {
            try {
                CdpUtil.saveFormEvent(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            appExecutors.mainThread().execute(callBack::onRegistrationSaved);
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void processSaveOrderForm(String jsonString, BaseCdpRegisterContract.InteractorCallBack callBack, String encounterType) {
        Runnable runnable = () -> {
            try {
                CdpUtil.saveTaskEvent(jsonString, encounterType);
            } catch (Exception e) {
                e.printStackTrace();
            }

            appExecutors.mainThread().execute(callBack::onRegistrationSaved);
        };
        appExecutors.diskIO().execute(runnable);
    }

    public void getNextUniqueId(final Triple<String, String, String> triple, final BaseCdpRegisterContract.InteractorCallBack callBack) {
        Runnable runnable = () -> {
            UniqueId uniqueId = getUniqueIdRepository().getNextUniqueId();
            final String entityId = uniqueId != null ? uniqueId.getOpenmrsId() : "";
            appExecutors.mainThread().execute(() -> {
                if (StringUtils.isBlank(entityId)) {
                    callBack.onNoUniqueId();
                } else {
                    callBack.onUniqueIdFetched(triple, entityId);
                }
            });
        };

        appExecutors.diskIO().execute(runnable);
    }

    public void saveRegistration(final List<CdpOutletEventClient> cdpOutletEventClientList, final String jsonString,
                                 final RegisterParams registerParams, final BaseCdpRegisterContract.InteractorCallBack callBack) {
        Runnable runnable = () -> {
            saveRegistration(cdpOutletEventClientList, jsonString, registerParams);
            appExecutors.mainThread().execute(() -> callBack.onRegistrationSaved(registerParams.isEditMode()));
        };

        appExecutors.diskIO().execute(runnable);
    }


    public void saveRegistration(@NonNull List<CdpOutletEventClient> allClientEventList, @NonNull String jsonString,
                                 @NonNull RegisterParams params) {
        try {
            List<String> currentFormSubmissionIds = new ArrayList<>();

            for (int i = 0; i < allClientEventList.size(); i++) {
                try {

                    CdpOutletEventClient allClientEvent = allClientEventList.get(i);
                    Client baseClient = allClientEvent.getClient();
                    Event baseEvent = allClientEvent.getEvent();
                    addClient(params, baseClient);
                    addEvent(params, currentFormSubmissionIds, baseEvent);
                    updateOpenSRPId(jsonString, params, baseClient);
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

    @NotNull
    public ECSyncHelper getSyncHelper() {
        return CdpLibrary.getInstance().getEcSyncHelper();
    }

    @NotNull
    public AllSharedPreferences getAllSharedPreferences() {
        return CdpLibrary.getInstance().context().allSharedPreferences();
    }

}
