package org.smartregister.chw.cdp.provider;

import android.content.Context;
import android.view.View;

import org.smartregister.cdp.R;
import org.smartregister.chw.cdp.CdpLibrary;
import org.smartregister.chw.cdp.fragment.BaseCdpRegisterFragment;
import org.smartregister.chw.cdp.holders.OrdersViewHolder;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.chw.cdp.util.DBConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Location;
import org.smartregister.domain.LocationTag;
import org.smartregister.repository.LocationRepository;
import org.smartregister.repository.LocationTagRepository;
import org.smartregister.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class BaseReceivedOrdersRegisterProvider extends BaseOrdersRegisterProvider {
    private Context context;
    private View.OnClickListener onClickListener;
    private LocationRepository locationRepository;

    public BaseReceivedOrdersRegisterProvider(Context context, View.OnClickListener onClickListener, View.OnClickListener paginationClickListener) {
        super(context, onClickListener, paginationClickListener);
        this.context = context;
        this.onClickListener = onClickListener;
        this.locationRepository = CdpLibrary.getInstance().context().getLocationRepository();
    }

    @Override
    protected void populateOrderDetailColumn(CommonPersonObjectClient pc, OrdersViewHolder viewHolder) {
        try {
            String healthFacilityId = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.LOCATION_ID, true);
            String requestedAt = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.REQUESTED_AT, true);
            String requester = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.REQUESTER, true);
            String condomType = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.CONDOM_TYPE, true);
            String condomBrand = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.CONDOM_BRAND, true);
            String condomQuantity = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.QUANTITY_REQ, false);
            String orderStatus = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.STATUS, false);

            Location location = locationRepository.getLocationById(healthFacilityId);

            String healthFacilityName = location.getProperties().getName();
            viewHolder.health_facility_label.setText(R.string.requester_name);
            viewHolder.health_facility.setText(String.format("%s - %s", requester, healthFacilityName));

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            long timestamp = Long.parseLong(requestedAt);
            Date date = new Date(timestamp);
            String formattedDate = outputFormat.format(date);
            viewHolder.request_date.setText(formattedDate);


            viewHolder.condom_type.setText(condomType);
            viewHolder.condom_brand.setText(condomBrand);
            viewHolder.quantity.setText(condomQuantity);
            viewHolder.status.setText(getStatusString(context, orderStatus));
            switch (orderStatus) {
                case Constants.OrderStatus.FAILED:
                    viewHolder.status.setTextColor(context.getResources().getColor(R.color.error_color));
                    break;
                case Constants.OrderStatus.READY:
                case Constants.OrderStatus.IN_TRANSIT:
                    viewHolder.status.setTextColor(context.getResources().getColor(R.color.alert_in_progress_blue));
                    break;
                case Constants.OrderStatus.COMPLETE:
                    viewHolder.status.setTextColor(context.getResources().getColor(R.color.alert_complete_green));
                    break;
            }
            viewHolder.registerColumns.setOnClickListener(onClickListener);
            viewHolder.registerColumns.setTag(pc);
            viewHolder.registerColumns.setTag(R.id.VIEW_ID, BaseCdpRegisterFragment.CLICK_VIEW_NORMAL);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

}

