package org.smartregister.chw.lab.presenter;

import android.util.Log;

import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONObject;
import org.smartregister.chw.lab.contract.BaseLabRegisterContract;
import org.smartregister.chw.lab.pojo.RegisterParams;
import org.smartregister.lab.R;

import java.lang.ref.WeakReference;
import java.util.List;

import timber.log.Timber;

public class BaseLabRegisterPresenter implements BaseLabRegisterContract.Presenter, BaseLabRegisterContract.InteractorCallBack {

    public static final String TAG = BaseLabRegisterPresenter.class.getName();

    protected WeakReference<BaseLabRegisterContract.View> viewReference;
    protected BaseLabRegisterContract.Model model;
    private BaseLabRegisterContract.Interactor interactor;

    public BaseLabRegisterPresenter(BaseLabRegisterContract.View view, BaseLabRegisterContract.Model model, BaseLabRegisterContract.Interactor interactor) {
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
    public void saveForm(String jsonString) {
        try {
            getView().showProgressDialog(R.string.saving_dialog_title);
            interactor.saveRegistration(jsonString, this);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    @Override
    public void saveManifestForm(String jsonString, String encounterType) {

    }

    @Override
    public void saveForm(String jsonString, RegisterParams registerParams) {
        //Use this for registration form
        try {
            interactor.saveRegistration(jsonString, this);
        } catch (Exception e) {
            Timber.e(e);
        }
    }


    @Override
    public void onRegistrationSaved() {
        getView().hideProgressDialog();

    }

    @Override
    public void onRegistrationSaved(boolean editMode) {
        getView().hideProgressDialog();
    }

    @Override
    public void onNoUniqueId() {
        if (getView() != null)
            getView().displayShortToast(R.string.no_unique_id);
    }

    @Override
    public void onUniqueIdFetched(Triple<String, String, String> triple, String entityId) {
        if (getView() != null) {
            try {
                startForm(triple.getLeft(), entityId, triple.getMiddle(), triple.getRight());
            } catch (Exception e) {
                Timber.e(e);
                getView().displayToast(R.string.error_unable_to_start_form);
            }
        }
    }

    @Override
    public void registerViewConfigurations(List<String> list) {
//        implement
    }

    @Override
    public void unregisterViewConfiguration(List<String> list) {
//        implement
    }

    @Override
    public void onDestroy(boolean b) {
//        implement
    }

    @Override
    public void updateInitials() {
//        implement
    }

    private BaseLabRegisterContract.View getView() {
        if (viewReference != null)
            return viewReference.get();
        else
            return null;
    }
}
