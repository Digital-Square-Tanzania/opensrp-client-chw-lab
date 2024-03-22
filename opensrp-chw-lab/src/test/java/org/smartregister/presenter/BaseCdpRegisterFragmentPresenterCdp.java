package org.smartregister.presenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.smartregister.chw.lab.contract.BaseCdpRegisterFragmentContract;
import org.smartregister.chw.lab.presenter.BaseCdpRegisterFragmentPresenter;
import org.smartregister.chw.lab.util.Constants;
import org.smartregister.chw.lab.util.DBConstants;
import org.smartregister.configurableviews.model.View;

import java.util.Set;
import java.util.TreeSet;

public class BaseCdpRegisterFragmentPresenterCdp {
    @Mock
    protected BaseCdpRegisterFragmentContract.View view;

    @Mock
    protected BaseCdpRegisterFragmentContract.Model model;

    private BaseCdpRegisterFragmentPresenter baseCdpRegisterFragmentPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        baseCdpRegisterFragmentPresenter = new BaseCdpRegisterFragmentPresenter(view, model, "");
    }

    @Test
    public void assertNotNull() {
        Assert.assertNotNull(baseCdpRegisterFragmentPresenter);
    }

    @Test
    public void getMainCondition() {
        Assert.assertEquals("", baseCdpRegisterFragmentPresenter.getMainCondition());
    }

    @Test
    public void getDueFilterCondition() {
        Assert.assertEquals(" (cast( julianday(STRFTIME('%Y-%m-%d', datetime('now'))) -  julianday(IFNULL(SUBSTR(cdp_test_date,7,4)|| '-' || SUBSTR(cdp_test_date,4,2) || '-' || SUBSTR(cdp_test_date,1,2),'')) as integer) between 7 and 14) ", baseCdpRegisterFragmentPresenter.getDueFilterCondition());
    }

    @Test
    public void getDefaultSortQuery() {
        Assert.assertEquals(Constants.TABLES.CDP_REGISTER + "." + DBConstants.KEY.LAST_INTERACTED_WITH + " DESC ", baseCdpRegisterFragmentPresenter.getDefaultSortQuery());
    }

    @Test
    public void getMainTable() {
        Assert.assertEquals(Constants.TABLES.CDP_REGISTER, baseCdpRegisterFragmentPresenter.getMainTable());
    }

    @Test
    public void initializeQueries() {
        Set<View> visibleColumns = new TreeSet<>();
        baseCdpRegisterFragmentPresenter.initializeQueries(null);
        Mockito.doNothing().when(view).initializeQueryParams("ec_cdp_confirmation", null, null);
        Mockito.verify(view).initializeQueryParams("ec_cdp_confirmation", null, null);
        Mockito.verify(view).initializeAdapter(visibleColumns);
        Mockito.verify(view).countExecute();
        Mockito.verify(view).filterandSortInInitializeQueries();
    }

}