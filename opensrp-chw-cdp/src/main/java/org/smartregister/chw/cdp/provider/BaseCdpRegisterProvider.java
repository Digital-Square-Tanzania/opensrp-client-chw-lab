package org.smartregister.chw.cdp.provider;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.smartregister.cdp.R;
import org.smartregister.chw.cdp.fragment.BaseCdpRegisterFragment;
import org.smartregister.chw.cdp.holders.FooterViewHolder;
import org.smartregister.chw.cdp.util.DBConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.cursoradapter.RecyclerViewProvider;
import org.smartregister.util.Utils;
import org.smartregister.view.contract.SmartRegisterClient;
import org.smartregister.view.contract.SmartRegisterClients;
import org.smartregister.view.customcontrols.FontVariant;
import org.smartregister.view.dialog.FilterOption;
import org.smartregister.view.dialog.ServiceModeOption;
import org.smartregister.view.dialog.SortOption;
import org.smartregister.view.viewholder.OnClickFormLauncher;

import java.text.MessageFormat;
import java.util.Set;

import timber.log.Timber;

public class BaseCdpRegisterProvider implements RecyclerViewProvider<BaseCdpRegisterProvider.OutletViewHolder> {

    private final LayoutInflater inflater;
    protected View.OnClickListener onClickListener;
    private View.OnClickListener paginationClickListener;
    private Context context;
    private Set<org.smartregister.configurableviews.model.View> visibleColumns;

    public BaseCdpRegisterProvider(Context context, View.OnClickListener paginationClickListener, View.OnClickListener onClickListener, Set visibleColumns) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.paginationClickListener = paginationClickListener;
        this.onClickListener = onClickListener;
        this.visibleColumns = visibleColumns;
        this.context = context;
    }

    @Override
    public void getView(Cursor cursor, SmartRegisterClient smartRegisterClient, OutletViewHolder outletViewHolder) {
        CommonPersonObjectClient pc = (CommonPersonObjectClient) smartRegisterClient;
        if (visibleColumns.isEmpty()) {
            populatePatientColumn(pc, outletViewHolder);
        }
    }


    private void populatePatientColumn(CommonPersonObjectClient pc, final OutletViewHolder viewHolder) {
        try {

            String outletName = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.OUTLET_NAME, true);
            String outletLocation = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.OUTLET_WARD_NAME, true);
            String outletType = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.OUTLET_TYPE, true);
            String otherOutletType = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.OTHER_OUTLET_TYPE, true);
            String isClosed = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.IS_CLOSED, true);

            if (isClosed.equals("1")) {
                viewHolder.outlet_name.setTextColor(Color.GRAY);
                viewHolder.outlet_name.setTypeface(viewHolder.outlet_name.getTypeface(), Typeface.ITALIC);

                viewHolder.outlet_location.setTextColor(Color.GRAY);

                viewHolder.outlet_type.setTextColor(Color.GRAY);
            }

            viewHolder.outlet_name.setText(outletName);
            viewHolder.outlet_location.setText(outletLocation);

            if (outletType.equalsIgnoreCase("other"))
                viewHolder.outlet_type.setText(otherOutletType);
            else
                viewHolder.outlet_type.setText(outletType);

            viewHolder.outlet_column.setOnClickListener(onClickListener);
            viewHolder.outlet_column.setTag(pc);
            viewHolder.outlet_column.setTag(R.id.VIEW_ID, BaseCdpRegisterFragment.CLICK_VIEW_NORMAL);

            viewHolder.registerColumns.setOnClickListener(onClickListener);

            viewHolder.registerColumns.setOnClickListener(v -> viewHolder.outlet_column.performClick());
            viewHolder.registerColumns.setOnClickListener(v -> viewHolder.outlet_column.performClick());

        } catch (Exception e) {
            Timber.e(e);
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
    public SmartRegisterClients updateClients(FilterOption filterOption, ServiceModeOption serviceModeOption, FilterOption filterOption1, SortOption sortOption) {
        return null;
    }

    @Override
    public void onServiceModeSelected(ServiceModeOption serviceModeOption) {
//        implement
    }

    @Override
    public OnClickFormLauncher newFormLauncher(String s, String s1, String s2) {
        return null;
    }

    @Override
    public LayoutInflater inflater() {
        return inflater;
    }

    @Override
    public OutletViewHolder createViewHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.cdp_outlet_register_list_row, parent, false);
        return new OutletViewHolder(view);
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

    public class OutletViewHolder extends RecyclerView.ViewHolder {
        public TextView outlet_name;
        public TextView outlet_type;
        public TextView outlet_location;
        public View outlet_column;

        public View registerColumns;

        public OutletViewHolder(View itemView) {
            super(itemView);

            outlet_name = itemView.findViewById(R.id.outlet_name);
            outlet_type = itemView.findViewById(R.id.outlet_type);
            outlet_location = itemView.findViewById(R.id.outlet_location);
            outlet_column = itemView.findViewById(R.id.outlet_column);
            registerColumns = itemView.findViewById(R.id.outlet_register_columns);
        }
    }

}
