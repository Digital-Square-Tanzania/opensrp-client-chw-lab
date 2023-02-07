package org.smartregister.chw.cdp.interactor;

import android.content.Context;

import org.smartregister.chw.cdp.contract.RestockingHistoryContract;
import org.smartregister.chw.cdp.domain.Visit;
import org.smartregister.chw.cdp.util.AppExecutors;
import org.smartregister.chw.cdp.util.CdpUtil;
import org.smartregister.chw.cdp.util.VisitUtils;

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
                CdpUtil.saveFormEvent(jsonString);
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
            CdpUtil.startClientProcessing();
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
