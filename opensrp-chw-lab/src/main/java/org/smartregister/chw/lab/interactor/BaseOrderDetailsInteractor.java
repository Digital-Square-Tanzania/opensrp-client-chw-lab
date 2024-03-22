package org.smartregister.chw.lab.interactor;

import androidx.annotation.VisibleForTesting;

import org.smartregister.chw.lab.contract.BaseOrderDetailsContract;
import org.smartregister.chw.lab.util.AppExecutors;
import org.smartregister.commonregistry.CommonPersonObjectClient;

public class BaseOrderDetailsInteractor implements BaseOrderDetailsContract.Interactor {
    protected AppExecutors appExecutors;

    @VisibleForTesting
    BaseOrderDetailsInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseOrderDetailsInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void saveForm(CommonPersonObjectClient pc, String jsonString, BaseOrderDetailsContract.InteractorCallBack callBack) throws Exception {
    }

}
