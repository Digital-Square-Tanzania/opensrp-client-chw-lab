package org.smartregister.chw.lab.provider;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.smartregister.chw.lab.LabLibrary;
import org.smartregister.chw.lab.dao.LabDao;
import org.smartregister.chw.lab.fragment.BaseLabManifestsRegisterFragment;
import org.smartregister.chw.lab.holders.FooterViewHolder;
import org.smartregister.chw.lab.holders.ManifestViewHolder;
import org.smartregister.chw.lab.util.DBConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.cursoradapter.RecyclerViewProvider;
import org.smartregister.lab.R;
import org.smartregister.repository.LocationRepository;
import org.smartregister.util.Utils;
import org.smartregister.view.contract.SmartRegisterClient;
import org.smartregister.view.contract.SmartRegisterClients;
import org.smartregister.view.dialog.FilterOption;
import org.smartregister.view.dialog.ServiceModeOption;
import org.smartregister.view.dialog.SortOption;
import org.smartregister.view.viewholder.OnClickFormLauncher;

import java.text.MessageFormat;

import timber.log.Timber;

public class BaseManifestsRegisterProvider implements RecyclerViewProvider<ManifestViewHolder> {
    private final LayoutInflater inflater;
    protected Context context;
    protected View.OnClickListener onClickListener;
    protected LocationRepository locationRepository;
    private View.OnClickListener paginationClickListener;

    public BaseManifestsRegisterProvider(Context context, View.OnClickListener onClickListener, View.OnClickListener paginationClickListener) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.onClickListener = onClickListener;
        this.paginationClickListener = paginationClickListener;
        this.locationRepository = LabLibrary.getInstance().context().getLocationRepository();
    }

    @Override
    public void getView(Cursor cursor, SmartRegisterClient client, ManifestViewHolder viewHolder) {
        CommonPersonObjectClient pc = (CommonPersonObjectClient) client;
        populateOrderDetailColumn(pc, viewHolder);
    }

    protected void populateOrderDetailColumn(CommonPersonObjectClient pc, ManifestViewHolder viewHolder) {
        try {
            String batchNumber = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.BATCH_NUMBER, true);
            String manifestType = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.MANIFEST_TYPE, true);
            String destinationHub = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.DESTINATION_HUB_NAME, true);
            String dispatchDate = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.DISPATCH_DATE, true);

            viewHolder.registerColumns.setOnClickListener(onClickListener);
            viewHolder.registerColumns.setTag(pc);
            viewHolder.registerColumns.setTag(R.id.VIEW_ID, BaseLabManifestsRegisterFragment.CLICK_VIEW_NORMAL);

            viewHolder.batchNumber.setText(batchNumber);
            viewHolder.manifestType.setText(manifestType);
            viewHolder.destinationHub.setText(destinationHub);



            if (dispatchDate == null || dispatchDate.isEmpty()) {
                viewHolder.status.setText(context.getString(R.string.draft));
                viewHolder.status.setTextColor(context.getResources().getColor(R.color.alert_in_progress_blue));
            }else {
                viewHolder.status.setText(context.getString(R.string.dispatched));
                viewHolder.status.setTextColor(context.getResources().getColor(R.color.alert_complete_green));
            }


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
    public ManifestViewHolder createViewHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.manifest_list_row, parent, false);
        return new ManifestViewHolder(view);
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

