package org.smartregister.chw.lab.interactor;

import androidx.annotation.VisibleForTesting;

import org.smartregister.chw.lab.contract.BaseLabTestRequestsProfileContract;
import org.smartregister.chw.lab.util.AppExecutors;
import org.smartregister.chw.lab.util.LabUtil;

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
}
