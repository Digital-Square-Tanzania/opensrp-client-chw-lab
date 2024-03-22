package org.smartregister.presenter;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.chw.lab.contract.BaseCdpProfileContract;
import org.smartregister.chw.lab.domain.MemberObject;
import org.smartregister.chw.lab.presenter.BaseCdpProfilePresenter;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class BaseTestProfilePresenterCdp {

    @Mock
    private BaseCdpProfileContract.View view = Mockito.mock(BaseCdpProfileContract.View.class);

    @Mock
    private BaseCdpProfileContract.Interactor interactor = Mockito.mock(BaseCdpProfileContract.Interactor.class);

    @Mock
    private MemberObject memberObject = new MemberObject();

    private BaseCdpProfilePresenter profilePresenter = new BaseCdpProfilePresenter(view, interactor, memberObject);


    @Test
    public void fillProfileDataCallsSetProfileViewWithDataWhenPassedMemberObject() {
        profilePresenter.fillProfileData(memberObject);
        verify(view).setProfileViewWithData();
    }

    @Test
    public void fillProfileDataDoesntCallsSetProfileViewWithDataIfMemberObjectEmpty() {
        profilePresenter.fillProfileData(null);
        verify(view, never()).setProfileViewWithData();
    }

    @Test
    public void cdpTestDatePeriodIsLessThanSeven() {
        profilePresenter.recordCDPButton("");
        verify(view).hideView();
    }

    @Test
    public void cdpTestDatePeriodGreaterThanTen() {
        profilePresenter.recordCDPButton("OVERDUE");
        verify(view).setOverDueColor();
    }

    @Test
    public void cdpTestDatePeriodIsMoreThanFourteen() {
        profilePresenter.recordCDPButton("EXPIRED");
        verify(view).hideView();
    }

    @Test
    public void refreshProfileBottom() {
        profilePresenter.refreshProfileBottom();
        verify(interactor).refreshProfileInfo(memberObject, profilePresenter.getView());
    }

    @Test
    public void saveForm() {
        profilePresenter.saveForm(null);
        verify(interactor).saveRegistration(null, view);
    }
}
