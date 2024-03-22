package org.smartregister.chw.lab.activity;

import static org.smartregister.chw.lab.util.LabUtil.startClientProcessing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;

import org.json.JSONObject;
import org.smartregister.chw.lab.contract.BaseOrderDetailsContract;
import org.smartregister.chw.lab.domain.OrderFeedbackObject;
import org.smartregister.chw.lab.interactor.BaseOrderDetailsInteractor;
import org.smartregister.chw.lab.model.BaseOrderDetailsModel;
import org.smartregister.chw.lab.presenter.BaseOrderDetailsPresenter;
import org.smartregister.chw.lab.util.LabUtil;
import org.smartregister.chw.lab.util.Constants;
import org.smartregister.chw.lab.util.DBConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.lab.R;
import org.smartregister.util.Utils;
import org.smartregister.view.activity.SecuredActivity;

public class BaseManifestDetailsActivity extends SecuredActivity implements BaseOrderDetailsContract.View, View.OnClickListener {
    protected BaseOrderDetailsContract.Presenter presenter;
    protected Toolbar toolbar;
    protected TextView tvTitle;
    protected CommonPersonObjectClient client;
    protected TextView condomType;
    protected TextView condomBrand;
    protected TextView quantity;
    protected TextView requestDate;
    protected TextView requesterName;
    protected TextView feedbackCondomType;
    protected TextView feedbackCondomBrand;
    protected TextView feedbackQuantity;
    protected TextView responseDate;
    protected TextView responseStatus;
    protected Button outOfStockBtn;
    protected Button stockDistributionBtn;
    protected Button markAsReceivedBtn;
    protected Group tvRestockGroup;
    protected ConstraintLayout responseLayout;

    protected Long requestedAtMillis;


    public static void startMe(Activity activity, CommonPersonObjectClient pc) {
        Intent intent = new Intent(activity, BaseManifestDetailsActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.CLIENT, pc);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreation() {
        setContentView(getMainContentView());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            client = (CommonPersonObjectClient) getIntent().getSerializableExtra(Constants.ACTIVITY_PAYLOAD.CLIENT);
        }
        setupViews();
        initializePresenter();
    }

    @Override
    public int getMainContentView() {
        return R.layout.activity_order_details;
    }

    @Override
    protected void onResumption() {
        setupViews();
    }

    @Override
    public void setDetailViewWithData(CommonPersonObjectClient pc) {
        condomType.setText(Utils.getValue(pc, DBConstants.KEY.CONDOM_TYPE, true));
        condomBrand.setText(Utils.getValue(pc, DBConstants.KEY.CONDOM_BRAND, true));
        quantity.setText(Utils.getValue(pc, DBConstants.KEY.QUANTITY_REQ, false));
        requestedAtMillis = Long.parseLong(Utils.getValue(pc, DBConstants.KEY.REQUESTED_AT, false));
        requestDate.setText(LabUtil.formatTimeStamp(requestedAtMillis));
        String requester = Utils.getValue(pc, DBConstants.KEY.REQUESTER, false);
        requesterName.setText(requester);
    }

    @Override
    public void setDetailViewWithFeedbackData(OrderFeedbackObject feedbackObject) {
        responseLayout.setVisibility(View.VISIBLE);

        setTranslatedStringFromResources(feedbackCondomType, feedbackObject.getCondomType());
        setTranslatedStringFromResources(feedbackCondomBrand, feedbackObject.getCondomBrand());
        setTranslatedStringFromResources(responseStatus, "rs_" + feedbackObject.getResponseStatus());

        feedbackQuantity.setText(feedbackObject.getResponseQuantity());
        Long responseAtMillis = Long.parseLong(feedbackObject.getResponseDate());
        responseDate.setText(LabUtil.formatTimeStamp(responseAtMillis));
    }

    private void setTranslatedStringFromResources(TextView tv, String identifier) {
        int tvVal = LabUtil.getStringFromResources(identifier, this);
        if (tvVal != 0) {
            tv.setText(this.getString(tvVal));
        } else {
            tv.setText(identifier);
        }
    }

    @Override
    public void startFormActivity(JSONObject jsonForm) {
        Intent intent = new Intent(this, BaseManifestDetailsActivity.class);
        intent.putExtra(Constants.JSON_FORM_EXTRA.JSON, jsonForm.toString());
        startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    @Override
    public void initializePresenter() {
        presenter = new BaseOrderDetailsPresenter(this, new BaseOrderDetailsInteractor(), new BaseOrderDetailsModel(), client);
    }


    @Override
    public void hideButtons() {
        stockDistributionBtn.setVisibility(View.GONE);
        outOfStockBtn.setVisibility(View.GONE);
    }

    protected void setupViews() {
        toolbar = findViewById(R.id.collapsing_toolbar);
        tvTitle = findViewById(R.id.tvTitle);
        condomBrand = findViewById(R.id.condom_brand);
        condomType = findViewById(R.id.condom_type);
        quantity = findViewById(R.id.condom_quantity);
        requestDate = findViewById(R.id.request_date);
        requesterName = findViewById(R.id.requester_name);
        feedbackCondomBrand = findViewById(R.id.feedback_condom_brand);
        feedbackCondomType = findViewById(R.id.feedback_condom_type);
        feedbackQuantity = findViewById(R.id.feedback_condom_quantity);
        responseDate = findViewById(R.id.response_date);
        responseStatus = findViewById(R.id.response_status);
        outOfStockBtn = findViewById(R.id.btn_out_of_stock);
        stockDistributionBtn = findViewById(R.id.btn_stock_distribution);
        tvRestockGroup = findViewById(R.id.tv_restock_group);
        responseLayout = findViewById(R.id.response_details);
        markAsReceivedBtn = findViewById(R.id.btn_mark_as_received);

        stockDistributionBtn.setVisibility(View.GONE);
        outOfStockBtn.setVisibility(View.GONE);

        outOfStockBtn.setOnClickListener(this);
        stockDistributionBtn.setOnClickListener(this);
        markAsReceivedBtn.setOnClickListener(this);


        setUpActionBar();
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
        if (id == R.id.btn_stock_distribution) {
            startStockDistributionForm();
        }
        if (id == R.id.btn_mark_as_received) {
            startReceivedForm();
        }
    }

    private void startStockDistributionForm() {
        try {
            String condomType = Utils.getValue(client, DBConstants.KEY.CONDOM_TYPE, false);
            presenter.startForm(Constants.FORMS.CDP_CONDOM_DISTRIBUTION, null, condomType, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startReceivedForm() {
        try {
            String condomType = Utils.getValue(client, DBConstants.KEY.CONDOM_TYPE, false);
            presenter.startForm(Constants.FORMS.CDP_RECEIVE_CONDOM_FACILITY, null, condomType, requestedAtMillis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_GET_JSON && resultCode == RESULT_OK) {
            String jsonString = data.getStringExtra(Constants.JSON_FORM_EXTRA.JSON);
//            try {
//                JSONObject jsonObject = new JSONObject(jsonString);
//                String encounter_type = jsonObject.getString("encounter_type");
//                if (encounter_type.equalsIgnoreCase(Constants.EVENT_TYPE.CDP_CONDOM_DISTRIBUTION_OUTSIDE)) {
//                    presenter.saveForm(jsonString);
//                    finish();
//                }
//                if (encounter_type.equalsIgnoreCase(Constants.EVENT_TYPE.CDP_RECEIVE_FROM_FACILITY)) {
//                    presenter.saveMarkAsReceivedForm(jsonString);
//                    finish();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            startClientProcessing();
        }
    }
}
