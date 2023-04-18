package org.smartregister.chw.cdp.provider;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.smartregister.cdp.R;
import org.smartregister.chw.cdp.CdpLibrary;
import org.smartregister.chw.cdp.fragment.BaseCdpRegisterFragment;
import org.smartregister.chw.cdp.holders.FooterViewHolder;
import org.smartregister.chw.cdp.holders.OrdersViewHolder;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.chw.cdp.util.DBConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.cursoradapter.RecyclerViewProvider;
import org.smartregister.domain.Location;
import org.smartregister.repository.LocationRepository;
import org.smartregister.util.Utils;
import org.smartregister.view.contract.SmartRegisterClient;
import org.smartregister.view.contract.SmartRegisterClients;
import org.smartregister.view.dialog.FilterOption;
import org.smartregister.view.dialog.ServiceModeOption;
import org.smartregister.view.dialog.SortOption;
import org.smartregister.view.viewholder.OnClickFormLauncher;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class BaseOrdersRegisterProvider implements RecyclerViewProvider<OrdersViewHolder> {
    private final LayoutInflater inflater;
    protected Context context;
    protected View.OnClickListener onClickListener;
    private View.OnClickListener paginationClickListener;
    protected LocationRepository locationRepository;

    public BaseOrdersRegisterProvider(Context context, View.OnClickListener onClickListener, View.OnClickListener paginationClickListener) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.onClickListener = onClickListener;
        this.paginationClickListener = paginationClickListener;
        this.locationRepository = CdpLibrary.getInstance().context().getLocationRepository();
    }

    @Override
    public void getView(Cursor cursor, SmartRegisterClient client, OrdersViewHolder viewHolder) {
        CommonPersonObjectClient pc = (CommonPersonObjectClient) client;
        populateOrderDetailColumn(pc, viewHolder);
    }

    protected void populateOrderDetailColumn(CommonPersonObjectClient pc, OrdersViewHolder viewHolder) {
        try {

            String healthFacilityId = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.RECEIVING_ORDER_FACILITY, true);
            String requestedAt = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.REQUESTED_AT, true);
            String condomType = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.CONDOM_TYPE, true);
            String condomBrand = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.CONDOM_BRAND, true);
            String condomQuantity = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.QUANTITY_REQ, false);
            String orderStatus = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.STATUS, false);

            if (healthFacilityId != null) {
                Location location = locationRepository.getLocationById(healthFacilityId.toLowerCase(Locale.ROOT));
                String healthFacilityName = location.getProperties().getName();
                viewHolder.health_facility.setText(healthFacilityName);
            }


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
                default:
                    viewHolder.status.setTextColor(context.getResources().getColor(R.color.black));
            }
            viewHolder.registerColumns.setOnClickListener(onClickListener);
            viewHolder.registerColumns.setTag(pc);
            viewHolder.registerColumns.setTag(R.id.VIEW_ID, BaseCdpRegisterFragment.CLICK_VIEW_NORMAL);

        } catch (Exception e) {
            Timber.e(e);
        }
    }

    protected String getStatusString(Context context, String st) {
        switch (st.toUpperCase(Locale.ROOT)) {
            case Constants.OrderStatus.FAILED:
                return context.getString(R.string.order_status_failed);
            case Constants.OrderStatus.COMPLETE:
                return context.getString(R.string.order_status_complete);
            case Constants.OrderStatus.IN_TRANSIT:
                return context.getString(R.string.order_status_in_transit);
            default:
                return context.getString(R.string.order_status_pending);
        }
    }

    @Override
    public void getFooterView(RecyclerView.ViewHolder viewHolder, int currentPageCount, int totalPageCount, boolean hasNext, boolean hasPrevious) {
        FooterViewHolder footerViewHolder = (FooterViewHolder) viewHolder;
        footerViewHolder.pageInfoView.setText(MessageFormat.format(context.getString(org.smartregister.R.string.str_page_info), currentPageCount, totalPageCount));

        footerViewHolder.nextPageView.setVisibility(hasNext ? View.VISIBLE : View.INVISIBLE);
        footerViewHolder.previousPageView.setVisibility(hasPrevious ? View.VISIBLE : View.INVISIBLE);

        footerViewHolder.nextPageView.setOnClickListener(paginationClickListener);
        footerViewHolder.previousPageView.setOnClickListener(paginationClickListener);
    }

    @Override
    public SmartRegisterClients updateClients(FilterOption villageFilter, ServiceModeOption serviceModeOption, FilterOption searchFilter, SortOption sortOption) {
        return null;
    }

    @Override
    public void onServiceModeSelected(ServiceModeOption serviceModeOption) {
        //implement
    }

    @Override
    public OnClickFormLauncher newFormLauncher(String formName, String entityId, String metaData) {
        return null;
    }

    @Override
    public LayoutInflater inflater() {
        return inflater;
    }

    @Override
    public OrdersViewHolder createViewHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.orders_list_row, parent, false);
        return new OrdersViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder createFooterHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.smart_register_pagination, parent, false);
        return new FooterViewHolder(view);
    }

    @Override
    public boolean isFooterViewHolder(RecyclerView.ViewHolder viewHolder) {
        return viewHolder instanceof FooterViewHolder;
    }
}

