package org.smartregister.chw.lab.interactor;

import static org.smartregister.chw.lab.util.LabUtil.getUniqueIdRepository;

import androidx.annotation.VisibleForTesting;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;
import org.smartregister.chw.lab.LabLibrary;
import org.smartregister.chw.lab.contract.BaseLabRegisterContract;
import org.smartregister.chw.lab.util.AppExecutors;
import org.smartregister.chw.lab.util.LabUtil;
import org.smartregister.domain.UniqueId;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.sync.helper.ECSyncHelper;

public class BaseLabRegisterInteractor implements BaseLabRegisterContract.Interactor {

    private final AppExecutors appExecutors;

    @VisibleForTesting
    BaseLabRegisterInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseLabRegisterInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void saveRegistration(final String jsonString, final BaseLabRegisterContract.InteractorCallBack callBack) {

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

    public void getNextUniqueId(final Triple<String, String, String> triple, final BaseLabRegisterContract.InteractorCallBack callBack) {
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

    @NotNull
    public ECSyncHelper getSyncHelper() {
        return LabLibrary.getInstance().getEcSyncHelper();
    }

    @NotNull
    public AllSharedPreferences getAllSharedPreferences() {
        return LabLibrary.getInstance().context().allSharedPreferences();
    }

}
