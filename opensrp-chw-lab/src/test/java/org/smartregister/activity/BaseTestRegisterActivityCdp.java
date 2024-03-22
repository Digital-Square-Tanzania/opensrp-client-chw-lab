package org.smartregister.activity;

import android.content.Intent;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.smartregister.chw.lab.activity.BaseCdpRegisterActivity;

public class BaseTestRegisterActivityCdp {
    @Mock
    public Intent data;
    @Mock
    private BaseCdpRegisterActivity baseCdpRegisterActivity = new BaseCdpRegisterActivity();

    @Test
    public void assertNotNull() {
        Assert.assertNotNull(baseCdpRegisterActivity);
    }

    @Test
    public void testFormConfig() {
        Assert.assertNull(baseCdpRegisterActivity.getFormConfig());
    }

    @Test
    public void checkIdentifier() {
        Assert.assertNotNull(baseCdpRegisterActivity.getViewIdentifiers());
    }

    @Test(expected = Exception.class)
    public void onActivityResult() throws Exception {
        Whitebox.invokeMethod(baseCdpRegisterActivity, "onActivityResult", 2244, -1, data);
        Mockito.verify(baseCdpRegisterActivity.presenter()).saveForm(null);
    }

}
