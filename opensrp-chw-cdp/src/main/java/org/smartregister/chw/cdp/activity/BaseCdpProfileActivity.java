package org.smartregister.chw.cdp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartregister.cdp.R;
import org.smartregister.chw.cdp.contract.BaseCdpProfileContract;
import org.smartregister.chw.cdp.custom_views.BaseCdpFloatingMenu;
import org.smartregister.chw.cdp.dao.CdpDao;
import org.smartregister.chw.cdp.domain.OutletObject;
import org.smartregister.chw.cdp.interactor.BaseCdpProfileInteractor;
import org.smartregister.chw.cdp.model.BaseCdpProfileModel;
import org.smartregister.chw.cdp.presenter.BaseCdpProfilePresenter;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.helper.ImageRenderHelper;
import org.smartregister.view.activity.BaseProfileActivity;

import de.hdodenhof.circleimageview.CircleImageView;


public class BaseCdpProfileActivity extends BaseProfileActivity implements BaseCdpProfileContract.View, BaseCdpProfileContract.InteractorCallBack {
    protected BaseCdpProfileContract.Presenter profilePresenter;
    protected CircleImageView imageView;
    protected TextView textViewName;
    protected TextView textViewUniqueID;
    protected TextView textViewLocation;
    protected TextView textViewOutletType;
    protected TextView tvLastRecordedStock;
    protected RelativeLayout rlLastRecordedStock;
    protected TextView tvLastVisitSub;
    protected TextView tvLastVisitTitle;
    protected RelativeLayout rlVisitHistory;
    protected Button btnRecordFollowup;
    protected OutletObject outletObject;
    protected BaseCdpFloatingMenu baseCdpFloatingMenu;
    protected String mBaseEntityId;


    public static void startProfileActivity(Activity activity, String baseEntityId) {
        Intent intent = new Intent(activity, BaseCdpProfileActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreation() {
        setContentView(R.layout.activity_cdp_profile);
        Toolbar toolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);

        mBaseEntityId = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID);
        outletObject = CdpDao.getOutlet(mBaseEntityId);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.text_blue), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
        }

        toolbar.setNavigationOnClickListener(v -> BaseCdpProfileActivity.this.finish());
        appBarLayout = this.findViewById(R.id.collapsing_toolbar_appbarlayout);
        if (Build.VERSION.SDK_INT >= 21) {
            appBarLayout.setOutlineProvider(null);
        }

        imageRenderHelper = new ImageRenderHelper(this);
        setupViews();
        initializePresenter();
        updateFollowupButton();
    }

    @Override
    protected void onResumption() {
        super.onResumption();
        new android.os.Handler().postDelayed(this::initializePresenter, 1000);
    }

    @Override
    protected void setupViews() {
        initializeFloatingMenu();
        textViewName = findViewById(R.id.textview_name);
        textViewUniqueID = findViewById(R.id.textview_id);
        imageView = findViewById(R.id.imageview_profile);
        textViewLocation = findViewById(R.id.textview_location);
        textViewOutletType = findViewById(R.id.textview_outlet_type);
        tvLastRecordedStock = findViewById(R.id.tv_lastRecordedStock);
        rlLastRecordedStock = findViewById(R.id.rlLastRecordedStock);
        tvLastVisitSub = findViewById(R.id.tv_visitHistory_sub);
        tvLastVisitTitle = findViewById(R.id.tv_visitHistory_title);
        rlVisitHistory = findViewById(R.id.rlVisitHistory);
        btnRecordFollowup = findViewById(R.id.btn_record_visit);

        btnRecordFollowup.setOnClickListener(this);
        rlVisitHistory.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.title_layout) {
            onBackPressed();
        }
        if (id == R.id.btn_record_visit) {
            startOutletVisit();
        }
        if (id == R.id.rlVisitHistory) {
            startRestockingHistory();
        }
    }

    @Override
    protected void initializePresenter() {
        showProgressBar(true);
        profilePresenter = new BaseCdpProfilePresenter(this, new BaseCdpProfileInteractor(), new BaseCdpProfileModel(), outletObject);
        fetchProfileData();
        profilePresenter.refreshProfileBottom();
    }

    @Override
    public void updateFollowupButton() {
        if (CdpDao.getLastRecordedStockAtOutlet(outletObject.getBaseEntityId()) == 0 || (outletObject != null && outletObject.isClosed())) {
            hideView();
        } else {
            btnRecordFollowup.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideView() {
        btnRecordFollowup.setVisibility(View.GONE);
    }

    @Override
    public void updateLastRecordedStock() {
        tvLastRecordedStock.setText(getString(R.string.last_recorded_stock, CdpDao.getLastRecordedStockAtOutlet(outletObject.getBaseEntityId())));
    }

    protected void startRestockingHistory() {
        BaseRestockingHistoryActivity.startMe(this, outletObject);
    }

    @Override
    public void startFormActivity(JSONObject jsonForm) {
        Intent intent = new Intent(this, BaseCdpRegisterActivity.class);
        intent.putExtra(Constants.JSON_FORM_EXTRA.JSON, jsonForm.toString());
        startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void setProfileViewWithData(OutletObject outletObject) {
        textViewName.setText(outletObject.getOutletName());
        textViewLocation.setText(outletObject.getOutletWardName());
        textViewUniqueID.setText(outletObject.getOutletId());
        if (outletObject.getOutletType().equalsIgnoreCase("other"))
            textViewOutletType.setText(outletObject.getOtherOutletType());
        else
            textViewOutletType.setText(outletObject.getOutletType());
    }


    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        return null;
    }

    protected void startOutletVisit() {
        try {
            profilePresenter.startForm(Constants.FORMS.CD_OUTLET_VISIT, outletObject.getBaseEntityId(), null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void initializeFloatingMenu() {
        if (StringUtils.isNotBlank(outletObject.getFocalPersonNumber())) {
            baseCdpFloatingMenu = new BaseCdpFloatingMenu(this, outletObject);
            baseCdpFloatingMenu.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            addContentView(baseCdpFloatingMenu, linearLayoutParams);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_GET_JSON && resultCode == RESULT_OK) {
            profilePresenter.saveForm(data.getStringExtra(Constants.JSON_FORM_EXTRA.JSON));
        }
    }
}
