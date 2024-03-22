package org.smartregister.chw.lab.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONObject;
import org.smartregister.chw.lab.contract.BaseLabTestRequestsProfileContract;
import org.smartregister.chw.lab.custom_views.BaseCdpFloatingMenu;
import org.smartregister.chw.lab.dao.LabDao;
import org.smartregister.chw.lab.domain.OutletObject;
import org.smartregister.chw.lab.interactor.BaseCdpProfileInteractor;
import org.smartregister.chw.lab.model.BaseCdpProfileModel;
import org.smartregister.chw.lab.presenter.BaseCdpProfilePresenter;
import org.smartregister.chw.lab.util.Constants;
import org.smartregister.helper.ImageRenderHelper;
import org.smartregister.lab.R;
import org.smartregister.view.activity.BaseProfileActivity;

import de.hdodenhof.circleimageview.CircleImageView;


public class BaseLabTestRequestDetailsActivity extends BaseProfileActivity implements BaseLabTestRequestsProfileContract.View, BaseLabTestRequestsProfileContract.InteractorCallBack {
    protected BaseLabTestRequestsProfileContract.Presenter profilePresenter;
    protected CircleImageView imageView;
    protected Button btnRecordFollowup;
    protected BaseCdpFloatingMenu baseCdpFloatingMenu;
    protected String mBaseEntityId;
    protected String testSampleId;


    public static void startProfileActivity(Activity activity, String baseEntityId, String testSampleId) {
        Intent intent = new Intent(activity, BaseLabTestRequestDetailsActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityId);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.TEST_SAMPLE_ID, testSampleId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreation() {
        setContentView(R.layout.activity_lab_test_request_profile);
        Toolbar toolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        mBaseEntityId = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID);
        testSampleId = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.TEST_SAMPLE_ID);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.text_blue), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
        }

        toolbar.setNavigationOnClickListener(v -> BaseLabTestRequestDetailsActivity.this.finish());
        appBarLayout = this.findViewById(R.id.collapsing_toolbar_appbarlayout);
        if (Build.VERSION.SDK_INT >= 21) {
            appBarLayout.setOutlineProvider(null);
        }

        imageRenderHelper = new ImageRenderHelper(this);
        setupViews();
        initializePresenter();
    }

    @Override
    protected void onResumption() {
        super.onResumption();
        new android.os.Handler().postDelayed(this::initializePresenter, 1000);
    }

    @Override
    protected void setupViews() {
        imageView = findViewById(R.id.imageview_profile);
        btnRecordFollowup = findViewById(R.id.btn_record_visit);
        btnRecordFollowup.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.title_layout) {
            onBackPressed();
        } else if (id == R.id.btn_record_visit) {
            recordSampleRequestProcessing(mBaseEntityId, testSampleId);
        }
    }

    public void recordSampleRequestProcessing(String baseEntityId, String testRequestSampleId) {

    }

    @Override
    protected void initializePresenter() {
        showProgressBar(true);
        profilePresenter = new BaseCdpProfilePresenter(this, new BaseCdpProfileInteractor(), new BaseCdpProfileModel(), null);
        fetchProfileData();
        profilePresenter.refreshProfileBottom();

        if (LabDao.isSampleProcessed(testSampleId))
            btnRecordFollowup.setVisibility(View.GONE);
    }

    @Override
    public void hideView() {
        btnRecordFollowup.setVisibility(View.GONE);
    }

    @Override
    public void startFormActivity(JSONObject jsonForm) {
        Intent intent = new Intent(this, BaseLabRegisterActivity.class);
        intent.putExtra(Constants.JSON_FORM_EXTRA.JSON, jsonForm.toString());
        startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void setProfileViewWithData(OutletObject outletObject) {

    }


    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        return null;
    }


    @Override
    protected void fetchProfileData() {
        //fetch profile data
    }

    @Override
    public void showProgressBar(boolean status) {
        //Implement
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_GET_JSON && resultCode == RESULT_OK) {
            profilePresenter.saveForm(data.getStringExtra(Constants.JSON_FORM_EXTRA.JSON));
        }
    }
}
