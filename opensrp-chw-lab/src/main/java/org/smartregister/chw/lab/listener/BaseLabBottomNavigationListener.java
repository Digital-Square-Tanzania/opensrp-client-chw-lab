package org.smartregister.chw.lab.listener;

import android.app.Activity;
import androidx.annotation.NonNull;
import android.view.MenuItem;

import org.smartregister.chw.lab.activity.BaseLabRegisterActivity;
import org.smartregister.listener.BottomNavigationListener;
import org.smartregister.lab.R;

public class BaseLabBottomNavigationListener extends BottomNavigationListener {
    private Activity context;

    public BaseLabBottomNavigationListener(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        super.onNavigationItemSelected(item);

        BaseLabRegisterActivity baseRegisterActivity = (BaseLabRegisterActivity) context;
        int itemId =item.getItemId();
        if ( itemId == R.id.action_family) {
            baseRegisterActivity.switchToBaseFragment();
        } else if (itemId == R.id.action_manifest) {
            baseRegisterActivity.switchToFragment(1);
        }

        return true;
    }
}