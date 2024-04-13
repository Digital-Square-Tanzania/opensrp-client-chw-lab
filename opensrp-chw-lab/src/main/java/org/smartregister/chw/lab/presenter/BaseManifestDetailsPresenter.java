package org.smartregister.chw.lab.presenter;

import android.util.Log;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.smartregister.chw.lab.contract.BaseManifestDetailsActivityContract;

import java.lang.ref.WeakReference;

import timber.log.Timber;

public class BaseManifestDetailsPresenter implements BaseManifestDetailsActivityContract.Presenter, BaseManifestDetailsActivityContract.InteractorCallBack {

    public static final String TAG = BaseManifestDetailsPresenter.class.getName();

    protected WeakReference<BaseManifestDetailsActivityContract.View> viewReference;
    protected BaseManifestDetailsActivityContract.Model model;
    private BaseManifestDetailsActivityContract.Interactor interactor;

    public BaseManifestDetailsPresenter(BaseManifestDetailsActivityContract.View view, BaseManifestDetailsActivityContract.Model model, BaseManifestDetailsActivityContract.Interactor interactor) {
        viewReference = new WeakReference<>(view);
        this.interactor = interactor;
        this.model = model;
    }

    @Override
    public void startForm(String formName, String entityId, String metadata, String currentLocationId) throws Exception {
        JSONObject form = model.getFormAsJson(formName, entityId, currentLocationId);
        getView().startFormActivity(form);
    }

    @Override
    public BaseManifestDetailsActivityContract.@Nullable View getView() {
        if (viewReference != null)
            return viewReference.get();
        else
            return null;
    }

    @Override
    public void saveForm(String jsonString) {
        try {
            interactor.saveRegistration(jsonString, this);
        } catch (Exception e) {
            Timber.tag(TAG).e(Log.getStackTraceString(e));
        }
    }

    @Override
    public void onRegistrationSaved() {

    }
}
