package org.smartregister.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;
import org.smartregister.chw.lab.activity.BaseCdpProfileActivity;
import org.smartregister.chw.lab.contract.BaseCdpProfileContract;
import org.smartregister.domain.AlertStatus;
import org.smartregister.lab.R;

import static org.mockito.Mockito.validateMockitoUsage;

public class BaseTestProfileActivityCdp {
    @Mock
    public BaseCdpProfileActivity baseCdpProfileActivity;

    @Mock
    public BaseCdpProfileContract.Presenter profilePresenter;

    @Mock
    public View view;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }

    @Test
    public void assertNotNull() {
        Assert.assertNotNull(baseCdpProfileActivity);
    }

    @Test
    public void setOverDueColor() {
        baseCdpProfileActivity.setOverDueColor();
        Mockito.verify(view, Mockito.never()).setBackgroundColor(Color.RED);
    }

    @Test
    public void formatTime() {
        BaseCdpProfileActivity activity = new BaseCdpProfileActivity();
        try {
            Assert.assertEquals("25 Oct 2019", Whitebox.invokeMethod(activity, "formatTime", "25-10-2019"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkHideView() {
        baseCdpProfileActivity.hideView();
        Mockito.verify(view, Mockito.never()).setVisibility(View.GONE);
    }

    @Test
    public void checkProgressBar() {
        baseCdpProfileActivity.showProgressBar(true);
        Mockito.verify(view, Mockito.never()).setVisibility(View.VISIBLE);
    }

    @Test
    public void medicalHistoryRefresh() {
        baseCdpProfileActivity.refreshMedicalHistory(true);
        Mockito.verify(view, Mockito.never()).setVisibility(View.VISIBLE);
    }

    @Test
    public void onClickBackPressed() {
        baseCdpProfileActivity = Mockito.spy(new BaseCdpProfileActivity());
        Mockito.when(view.getId()).thenReturn(R.id.title_layout);
        Mockito.doNothing().when(baseCdpProfileActivity).onBackPressed();
        baseCdpProfileActivity.onClick(view);
        Mockito.verify(baseCdpProfileActivity).onBackPressed();
    }

    @Test
    public void onClickOpenMedicalHistory() {
        baseCdpProfileActivity = Mockito.spy(new BaseCdpProfileActivity());
        Mockito.when(view.getId()).thenReturn(R.id.rlLastVisit);
        Mockito.doNothing().when(baseCdpProfileActivity).openMedicalHistory();
        baseCdpProfileActivity.onClick(view);
        Mockito.verify(baseCdpProfileActivity).openMedicalHistory();
    }

    @Test
    public void onClickOpenUpcomingServices() {
        baseCdpProfileActivity = Mockito.spy(new BaseCdpProfileActivity());
        Mockito.when(view.getId()).thenReturn(R.id.rlUpcomingServices);
        Mockito.doNothing().when(baseCdpProfileActivity).openUpcomingService();
        baseCdpProfileActivity.onClick(view);
        Mockito.verify(baseCdpProfileActivity).openUpcomingService();
    }

    @Test
    public void onClickOpenFamlilyServicesDue() {
        baseCdpProfileActivity = Mockito.spy(new BaseCdpProfileActivity());
        Mockito.when(view.getId()).thenReturn(R.id.rlFamilyServicesDue);
        Mockito.doNothing().when(baseCdpProfileActivity).openFamilyDueServices();
        baseCdpProfileActivity.onClick(view);
        Mockito.verify(baseCdpProfileActivity).openFamilyDueServices();
    }

    @Test(expected = Exception.class)
    public void refreshFamilyStatusComplete() throws Exception {
        baseCdpProfileActivity = Mockito.spy(new BaseCdpProfileActivity());
        TextView textView = view.findViewById(R.id.textview_family_has);
        Whitebox.setInternalState(baseCdpProfileActivity, "tvFamilyStatus", textView);
        Mockito.doNothing().when(baseCdpProfileActivity).showProgressBar(false);
        baseCdpProfileActivity.refreshFamilyStatus(AlertStatus.complete);
        Mockito.verify(baseCdpProfileActivity).showProgressBar(false);
        PowerMockito.verifyPrivate(baseCdpProfileActivity).invoke("setFamilyStatus", "Family has nothing due");
    }

    @Test(expected = Exception.class)
    public void refreshFamilyStatusNormal() throws Exception {
        baseCdpProfileActivity = Mockito.spy(new BaseCdpProfileActivity());
        TextView textView = view.findViewById(R.id.textview_family_has);
        Whitebox.setInternalState(baseCdpProfileActivity, "tvFamilyStatus", textView);
        Mockito.doNothing().when(baseCdpProfileActivity).showProgressBar(false);
        baseCdpProfileActivity.refreshFamilyStatus(AlertStatus.complete);
        Mockito.verify(baseCdpProfileActivity).showProgressBar(false);
        PowerMockito.verifyPrivate(baseCdpProfileActivity).invoke("setFamilyStatus", "Family has services due");
    }

    @Test(expected = Exception.class)
    public void onActivityResult() throws Exception {
        baseCdpProfileActivity = Mockito.spy(new BaseCdpProfileActivity());
        Whitebox.invokeMethod(baseCdpProfileActivity, "onActivityResult", 2244, -1, null);
        Mockito.verify(profilePresenter).saveForm(null);
    }

}
