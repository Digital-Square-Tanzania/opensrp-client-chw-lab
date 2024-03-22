package org.smartregister.chw.lab.interactor;

import android.content.Context;

import org.smartregister.chw.lab.contract.RestockingHistoryContract;
import org.smartregister.chw.lab.domain.Visit;
import org.smartregister.chw.lab.util.AppExecutors;
import org.smartregister.chw.lab.util.LabUtil;
import org.smartregister.chw.lab.util.VisitUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.VisibleForTesting;
import timber.log.Timber;

public class BaseRestockingHistoryInteractor implements RestockingHistoryContract.Interactor {

    protected AppExecutors appExecutors;

    @VisibleForTesting
    BaseRestockingHistoryInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseRestockingHistoryInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void saveRegistration(String jsonString, RestockingHistoryContract.InteractorCallBack callBack) {
        Runnable runnable = () -> {
            try {
                LabUtil.saveFormEvent(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            appExecutors.mainThread().execute(() -> callBack.onRegistrationSaved());
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getMemberHistory(String memberID, Context context, RestockingHistoryContract.InteractorCallBack callBack) {
        final Runnable runnable = () -> {
            LabUtil.startClientProcessing();
            final List<Visit> visits = new ArrayList<>();
            try {
                visits.addAll(VisitUtils.getVisits(memberID));
            } catch (Exception e) {
                Timber.e(e);
            }
            appExecutors.mainThread().execute(() -> callBack.onDataFetched(visits));
        };

        appExecutors.diskIO().execute(runnable);
    }
}
