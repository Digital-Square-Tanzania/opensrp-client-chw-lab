package org.smartregister.chw.lab.activity;

import static org.smartregister.chw.lab.util.Constants.ENCOUNTER_TYPE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartregister.chw.lab.contract.BaseLabTestRequestsProfileContract;
import org.smartregister.chw.lab.dao.LabDao;
import org.smartregister.chw.lab.domain.TestSample;
import org.smartregister.chw.lab.interactor.BaseLabTestRequestDetailsInteractor;
import org.smartregister.chw.lab.model.BaseLabTestRequestModel;
import org.smartregister.chw.lab.presenter.BaseLabTestRequestDetailsPresenter;
import org.smartregister.chw.lab.util.Constants;
import org.smartregister.helper.ImageRenderHelper;
import org.smartregister.lab.R;
import org.smartregister.view.activity.BaseProfileActivity;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;


public class BaseLabTestRequestDetailsActivity extends BaseProfileActivity implements BaseLabTestRequestsProfileContract.View, BaseLabTestRequestsProfileContract.InteractorCallBack {
    protected BaseLabTestRequestsProfileContract.Presenter profilePresenter;

    protected CircleImageView imageView;

    protected Button btnRecordFollowup;

    protected String mBaseEntityId;

    protected String testSampleId;

    protected Boolean provideResultsToClient;

    protected TextView headerTestResultDetails;


    protected TextView sampleIdTv;

    protected TextView patientIdTv;

    protected TextView sampleRequestDateTv;

    protected TextView sampleTakenDateTv;

    protected TextView sampleTypeTv;

    protected TextView testedByTv;

    protected TextView authorizedByTv;

    protected TextView testedDateTv;

    protected TextView authorizedDateTv;

    protected TextView testingLocationTv;

    protected TextView testResultsTv;

    protected TextView testNameTv;

    protected TextView dateResultsProvidedToClientTv;

    protected TextView timeResultsProvidedToClientTv;

    protected TextView sampleSeparationTimeTv;

    protected TextView sampleSeparationDateTv;

    protected TextView reasonsForRejectionTv;

    protected TextView rejectionDateTv;

    protected TextView rejectedByTv;

    protected TextView contactInfoTv;

    protected LinearLayout testResultsLayout;

    protected LinearLayout provisionOfResultsToClientsLayout;

    protected LinearLayout sampleTypeLayout;

    protected LinearLayout separationDateLayout;

    protected LinearLayout separationTimeLayout;

    protected LinearLayout rejectionLayout;

    protected LinearLayout resultsLayout;

    protected TestSample testSample;

    public static void startProfileActivity(Activity activity, String baseEntityId, String testSampleId, Boolean provideResultsToClient) {
        Intent intent = new Intent(activity, BaseLabTestRequestDetailsActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityId);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.TEST_SAMPLE_ID, testSampleId);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.PROVIDE_RESULTS_TO_CLIENT, provideResultsToClient);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreation() {
        setContentView(R.layout.activity_lab_test_request_details);
        Toolbar toolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        mBaseEntityId = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID);
        testSampleId = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.TEST_SAMPLE_ID);
        provideResultsToClient = getIntent().getBooleanExtra(Constants.ACTIVITY_PAYLOAD.PROVIDE_RESULTS_TO_CLIENT, false);

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
    protected void setupViews() {
        imageView = findViewById(R.id.imageview_profile);
        headerTestResultDetails = findViewById(R.id.headerTestResultDetails);
        sampleIdTv = findViewById(R.id.sample_id);
        patientIdTv = findViewById(R.id.patient_id);
        sampleRequestDateTv = findViewById(R.id.sample_request_date);
        sampleTakenDateTv = findViewById(R.id.sample_taken_date);
        sampleTypeTv = findViewById(R.id.sample_type);
        testedByTv = findViewById(R.id.tested_by);
        authorizedByTv = findViewById(R.id.authorized_by);
        testedDateTv = findViewById(R.id.tested_date);
        authorizedDateTv = findViewById(R.id.authorized_date);
        testingLocationTv = findViewById(R.id.testing_location);
        testResultsTv = findViewById(R.id.test_results);
        testNameTv = findViewById(R.id.test_name);
        dateResultsProvidedToClientTv = findViewById(R.id.date_results_provided_to_client);
        timeResultsProvidedToClientTv = findViewById(R.id.time_results_provided_to_client);
        testResultsLayout = findViewById(R.id.test_results_layout);
        provisionOfResultsToClientsLayout = findViewById(R.id.provision_of_results_to_clients);
        btnRecordFollowup = findViewById(R.id.btn_record_visit);
        sampleSeparationDateTv = findViewById(R.id.sample_separation_date);
        sampleSeparationTimeTv = findViewById(R.id.sample_separation_time);

        reasonsForRejectionTv = findViewById(R.id.reasons_for_rejection);
        rejectionDateTv = findViewById(R.id.rejection_date);
        rejectedByTv = findViewById(R.id.rejected_by);
        contactInfoTv = findViewById(R.id.contact_info);
        rejectionLayout = findViewById(R.id.rejection_layout);

        resultsLayout = findViewById(R.id.results_layout);

        sampleTypeLayout = findViewById(R.id.sample_type_layout);
        separationDateLayout = findViewById(R.id.separation_date_layout);
        separationTimeLayout = findViewById(R.id.separation_time_layout);

        btnRecordFollowup.setOnClickListener(this);


        testSample = LabDao.getTestSamplesRequestsBySampleId(testSampleId).get(0);

        patientIdTv.setText(testSample.getPatientId());
        sampleIdTv.setText(testSampleId);
        sampleRequestDateTv.setText(testSample.getSampleRequestDate());
        sampleTakenDateTv.setText(testSample.getSampleCollectionDate());
        sampleTypeTv.setText(testSample.getSampleType());


        if (testSample.getSampleType().equalsIgnoreCase("hvl")) {
            headerTestResultDetails.setText(R.string.hvl_test_sample_title);
        } else {
            headerTestResultDetails.setText(R.string.heid_test_sample_title);
        }

        if (!testSample.getSampleSeparationDate().isEmpty()) {
            separationDateLayout.setVisibility(View.VISIBLE);
            separationTimeLayout.setVisibility(View.VISIBLE);

            sampleSeparationDateTv.setText(testSample.getSampleSeparationDate());
            sampleSeparationTimeTv.setText(testSample.getSampleSeparationTime());
        }

        if (!testSample.getTypeOfSample().isEmpty()) {
            sampleTypeLayout.setVisibility(View.VISIBLE);
            sampleTypeTv.setText(testSample.getTypeOfSample().toUpperCase(Locale.ROOT));
        }

        if (testSample.getResults() != null &&
                !testSample.getResults().isEmpty() &&
                !testSample.getResults().equalsIgnoreCase("invalid") &&
                !testSample.getResults().equalsIgnoreCase("rejected")
        ) {
            resultsLayout.setVisibility(View.VISIBLE);
            testResultsLayout.setVisibility(View.VISIBLE);
            rejectionLayout.setVisibility(View.GONE);
            testedByTv.setText(testSample.getTestedBy());
            authorizedByTv.setText(testSample.getAuthorizedBy());
            testedDateTv.setText(testSample.getTestedDate());
            authorizedDateTv.setText(testSample.getAuthorizedDate());
            if (testSample.getSampleType().equalsIgnoreCase("hvl")) {
                testResultsTv.setText(String.format(getResources().getString(R.string.test_results_copies_per_ml), testSample.getResults()));
            } else {
                testResultsTv.setText(testSample.getResults());
            }
        } else if (testSample.getResults() != null &&
                !testSample.getResults().isEmpty() &&
                testSample.getResults().equalsIgnoreCase("rejected")
        ) {
            resultsLayout.setVisibility(View.VISIBLE);
            testResultsLayout.setVisibility(View.GONE);
            rejectionLayout.setVisibility(View.VISIBLE);
            reasonsForRejectionTv.setText(testSample.getReasonsForRejection());
            rejectionDateTv.setText(testSample.getRejectionDate());
            rejectedByTv.setText(testSample.getRejectedBy());
            contactInfoTv.setText(testSample.getRejectionContactInfo());
            testResultsTv.setText(testSample.getResults().toUpperCase(Locale.ROOT));
        } else if (testSample.getResults() != null &&
                !testSample.getResults().isEmpty() &&
                testSample.getResults().equalsIgnoreCase("invalid")
        ) {
            resultsLayout.setVisibility(View.VISIBLE);
            testResultsTv.setText(testSample.getResults().toUpperCase(Locale.ROOT));
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.title_layout) {
            onBackPressed();
        } else if (id == R.id.btn_record_visit) {
            if (btnRecordFollowup.getText().toString().equals(getString(R.string.record_test_sample_results)))
                recordSampleRequestResults(mBaseEntityId, testSampleId);
            else if (btnRecordFollowup.getText().toString().equals(getString(R.string.record_test_sample_collection)))
                recordSampleRequestProcessing(mBaseEntityId, testSampleId);
            else if (btnRecordFollowup.getText().toString().equals(getString(R.string.record_provision_of_results_to_clients)))
                recordProvisionOfResultsToClient(mBaseEntityId, testSampleId);
        }
    }

    public void recordSampleRequestProcessing(String baseEntityId, String testRequestSampleId) {

    }

    public void recordSampleRequestResults(String baseEntityId, String testRequestSampleId) {

    }

    public void recordProvisionOfResultsToClient(String baseEntityId, String testRequestSampleId) {

    }

    @Override
    protected void initializePresenter() {
        showProgressBar(true);
        profilePresenter = new BaseLabTestRequestDetailsPresenter(this, new BaseLabTestRequestDetailsInteractor(), new BaseLabTestRequestModel());
        fetchProfileData();
        profilePresenter.refreshProfileBottom();

        List<TestSample> testSamples = LabDao.getTestSamplesRequestsBySampleId(testSampleId);
        TestSample testSample = null;
        if (!testSamples.isEmpty()) {
            testSample = testSamples.get(0);
        }


        if (provideResultsToClient && testSample != null &&
                StringUtils.isNotBlank(testSample.getResults()) &&
                !testSample.getResults().equalsIgnoreCase("invalid") &&
                !testSample.getResults().equalsIgnoreCase("rejected")
        ) {
            if (StringUtils.isNotBlank(testSample.getDateResultsProvidedToClient())) {
                btnRecordFollowup.setVisibility(View.GONE);
            } else {
                btnRecordFollowup.setText(R.string.record_provision_of_results_to_clients);
            }
        } else if (testSample != null && StringUtils.isNotBlank(testSample.getResults())) {
            btnRecordFollowup.setVisibility(View.GONE);
        } else if (LabDao.isSampleUploaded(testSampleId)) {
            btnRecordFollowup.setText(R.string.record_test_sample_results);
        } else if (LabDao.isSampleProcessed(testSampleId)) {
            btnRecordFollowup.setVisibility(View.GONE);
        } else {
            btnRecordFollowup.setText(R.string.record_test_sample_collection);
        }

        if (testSample != null && StringUtils.isNotBlank(testSample.getDateResultsProvidedToClient())) {
            provisionOfResultsToClientsLayout.setVisibility(View.VISIBLE);
            dateResultsProvidedToClientTv.setText(testSample.getDateResultsProvidedToClient());
            timeResultsProvidedToClientTv.setText(testSample.getTimeResultsProvidedToClient());
        }

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
    public void setProfileViewWithData() {

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
            btnRecordFollowup.setVisibility(View.GONE);

            String jsonString = data.getStringExtra(Constants.JSON_FORM_EXTRA.JSON);
            JSONObject formJson;
            String encounter_type = "";
            try {
                formJson = new JSONObject(jsonString);
                encounter_type = formJson.getString(ENCOUNTER_TYPE);
            } catch (Exception e) {
                Timber.e(e);
            }

            if (!encounter_type.equalsIgnoreCase("HEI HIV Test Results")) {
                profilePresenter.saveForm(data.getStringExtra(Constants.JSON_FORM_EXTRA.JSON));
            }

            new android.os.Handler().postDelayed(() -> {
                setupViews();
                initializePresenter();
            }, 500);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new android.os.Handler().postDelayed(this::initializePresenter, 1000);
    }
}
