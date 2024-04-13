package org.smartregister.chw.lab.activity;

import static org.smartregister.chw.lab.util.LabUtil.startClientProcessing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartregister.chw.lab.contract.BaseManifestDetailsActivityContract;
import org.smartregister.chw.lab.dao.LabDao;
import org.smartregister.chw.lab.domain.Manifest;
import org.smartregister.chw.lab.interactor.BaseManifestDetailsInteractor;
import org.smartregister.chw.lab.model.BaseManifestDetailsModel;
import org.smartregister.chw.lab.presenter.BaseManifestDetailsPresenter;
import org.smartregister.chw.lab.util.Constants;
import org.smartregister.lab.R;
import org.smartregister.view.activity.BaseProfileActivity;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class BaseManifestDetailsActivity extends BaseProfileActivity implements BaseManifestDetailsActivityContract.View, View.OnClickListener {
    protected BaseManifestDetailsActivityContract.Presenter presenter;
    protected Toolbar toolbar;
    protected TextView tvTitle;
    protected TextView batchNumberTv;
    protected TextView manifestTypeTv;
    protected TextView destinationHubTv;
    protected ImageView edit;
    protected Button dispatchBtn;
    protected ListView sampleList;

    protected LinearLayout dispatchDateLayout;

    protected TextView dispatchDateTv;

    protected LinearLayout dispatchTimeLayout;

    protected TextView dispatchTimeTv;

    protected String batchNumber;


    public static void startProfileActivity(Activity activity, String batchNumber) {
        Intent intent = new Intent(activity, BaseLabTestRequestDetailsActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BATCH_NUMBER, batchNumber);
        activity.startActivity(intent);
    }

    public static List<String> convertToList(String input) {
        List<String> resultList = new ArrayList<>();

        // Remove brackets and split the string by commas
        String[] elements = input.substring(1, input.length() - 1).split(",");

        // Trim each element and add it to the result list
        for (String element : elements) {
            resultList.add(element.trim());
        }

        return resultList;
    }

    @Override
    protected void onCreation() {
        setContentView(getMainContentView());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            batchNumber = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.BATCH_NUMBER);
        }
        setupViews();
        initializePresenter();
    }

    @Override
    public int getMainContentView() {
        return R.layout.activity_manifest_details;
    }

    @Override
    protected void onResumption() {
        setupViews();
    }

    @Override
    public void setDetailViewWithData(String batchNumber) {
        Manifest manifest = LabDao.getManifestByBatchNumber(batchNumber);

        if (manifest != null) {
            batchNumberTv.setText(batchNumber);
            manifestTypeTv.setText(manifest.getManifestType());
            destinationHubTv.setText(manifest.getDestinationHubName());

            if (!manifest.getDispatchDate().isEmpty()) {
                dispatchTimeLayout.setVisibility(View.VISIBLE);
                dispatchDateLayout.setVisibility(View.VISIBLE);

                dispatchDateTv.setText(manifest.getDispatchDate());
                dispatchTimeTv.setText(manifest.getDispatchTime());
            }

            List<String> arrayList = convertToList(manifest.getSampleList());
            ArrayAdapter<String> arrayAdapter = new
                    ArrayAdapter<>(BaseManifestDetailsActivity.this, android.R.layout.simple_list_item_1,
                    arrayList);
            sampleList.setAdapter(arrayAdapter);
        }
    }

    @Override
    public void startFormActivity(JSONObject jsonForm) {
        Intent intent = new Intent(this, BaseManifestDetailsActivity.class);
        intent.putExtra(Constants.JSON_FORM_EXTRA.JSON, jsonForm.toString());
        startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    protected void setupViews() {
        toolbar = findViewById(R.id.collapsing_toolbar);
        tvTitle = findViewById(R.id.tvTitle);
        batchNumberTv = findViewById(R.id.batch_number);
        manifestTypeTv = findViewById(R.id.manifest_type);
        destinationHubTv = findViewById(R.id.destination_hub);
        edit = findViewById(R.id.edit);
        dispatchBtn = findViewById(R.id.btn_dispatch);
        sampleList = findViewById(R.id.sample_list);

        dispatchDateLayout = findViewById(R.id.dispatch_date_layout);
        dispatchTimeLayout = findViewById(R.id.dispatch_time_layout);

        dispatchDateTv = findViewById(R.id.dispatch_date);
        dispatchTimeTv = findViewById(R.id.dispatch_time);

        dispatchBtn.setOnClickListener(this);

        setDetailViewWithData(batchNumber);
        setUpActionBar();
    }

    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        return null;
    }

    private void setUpActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.text_blue), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_dispatch) {
            dispatchManifest();
        }
    }

    @Override
    protected void fetchProfileData() {

    }


    public void dispatchManifest() {
    }

    @Override
    protected void initializePresenter() {
        presenter = new BaseManifestDetailsPresenter(this, new BaseManifestDetailsModel(), new BaseManifestDetailsInteractor());
        fetchProfileData();
        updateDispatchBtn();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_GET_JSON && resultCode == RESULT_OK) {
            String jsonString = data.getStringExtra(Constants.JSON_FORM_EXTRA.JSON);
            try {
                presenter.saveForm(jsonString);
            } catch (Exception e) {
                Timber.e(e);
            }
            startClientProcessing();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new android.os.Handler().postDelayed(this::updateDispatchBtn, 500);
    }

    private void updateDispatchBtn() {
        if (StringUtils.isNotBlank(LabDao.getDispatchDate(batchNumber))) {
            dispatchBtn.setVisibility(View.GONE);
            setDetailViewWithData(batchNumber);
        }
    }
}
