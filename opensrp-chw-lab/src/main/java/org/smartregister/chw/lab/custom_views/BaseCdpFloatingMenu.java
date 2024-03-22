package org.smartregister.chw.lab.custom_views;

import android.app.Activity;
import android.content.Context;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.View;
import android.widget.LinearLayout;

import org.smartregister.chw.lab.domain.OutletObject;
import org.smartregister.lab.R;

public class BaseCdpFloatingMenu extends LinearLayout implements View.OnClickListener {
    private OutletObject outletObject;

    public BaseCdpFloatingMenu(Context context, OutletObject outletObject) {
        super(context);
        initUi();
        this.outletObject = outletObject;
    }

    protected void initUi() {
        inflate(getContext(), R.layout.view_cdp_floating_menu, this);
        FloatingActionButton fab = findViewById(R.id.cdp_fab);
        if (fab != null)
            fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cdp_fab) {
            Activity activity = (Activity) getContext();
        }
    }
}