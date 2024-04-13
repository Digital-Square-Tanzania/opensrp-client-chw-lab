package org.smartregister.chw.lab.interactor;

import androidx.annotation.VisibleForTesting;

import org.jetbrains.annotations.NotNull;
import org.smartregister.chw.lab.LabLibrary;
import org.smartregister.chw.lab.contract.BaseManifestDetailsActivityContract;
import org.smartregister.chw.lab.util.AppExecutors;
import org.smartregister.chw.lab.util.LabUtil;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.sync.helper.ECSyncHelper;

public class BaseManifestDetailsInteractor implements BaseManifestDetailsActivityContract.Interactor {

    private final AppExecutors appExecutors;

    @VisibleForTesting
    BaseManifestDetailsInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseManifestDetailsInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void saveRegistration(final String jsonString, final BaseManifestDetailsActivityContract.InteractorCallBack callBack) {

        Runnable runnable = () -> {
            try {
                LabUtil.saveFormEvent(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            appExecutors.mainThread().execute(callBack::onRegistrationSaved);
        };
        appExecutors.diskIO().execute(runnable);
    }

    @NotNull
    public ECSyncHelper getSyncHelper() {
        return LabLibrary.getInstance().getEcSyncHelper();
    }

    @NotNull
    public AllSharedPreferences getAllSharedPreferences() {
        return LabLibrary.getInstance().context().allSharedPreferences();
    }

}
