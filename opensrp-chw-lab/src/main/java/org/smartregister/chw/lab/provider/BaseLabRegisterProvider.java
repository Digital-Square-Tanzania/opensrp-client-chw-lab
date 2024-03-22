package org.smartregister.chw.lab.provider;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.smartregister.chw.lab.dao.LabDao;
import org.smartregister.chw.lab.fragment.BaseLabRequestsRegisterFragment;
import org.smartregister.chw.lab.holders.FooterViewHolder;
import org.smartregister.chw.lab.util.DBConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.cursoradapter.RecyclerViewProvider;
import org.smartregister.lab.R;
import org.smartregister.util.Utils;
import org.smartregister.view.contract.SmartRegisterClient;
import org.smartregister.view.contract.SmartRegisterClients;
import org.smartregister.view.dialog.FilterOption;
import org.smartregister.view.dialog.ServiceModeOption;
import org.smartregister.view.dialog.SortOption;
import org.smartregister.view.viewholder.OnClickFormLauncher;

import java.text.MessageFormat;
import java.util.Set;

import timber.log.Timber;

public class BaseLabRegisterProvider implements RecyclerViewProvider<BaseLabRegisterProvider.SampleRequestViewHolder> {

    private final LayoutInflater inflater;
    protected View.OnClickListener onClickListener;
    private View.OnClickListener paginationClickListener;
    private Context context;
    private Set<org.smartregister.configurableviews.model.View> visibleColumns;

    public BaseLabRegisterProvider(Context context, View.OnClickListener paginationClickListener, View.OnClickListener onClickListener, Set visibleColumns) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.paginationClickListener = paginationClickListener;
        this.onClickListener = onClickListener;
        this.visibleColumns = visibleColumns;
        this.context = context;
    }

    @Override
    public void getView(Cursor cursor, SmartRegisterClient smartRegisterClient, SampleRequestViewHolder sampleRequestViewHolder) {
        CommonPersonObjectClient pc = (CommonPersonObjectClient) smartRegisterClient;
        if (visibleColumns.isEmpty()) {
            populatePatientColumn(pc, sampleRequestViewHolder);
        }
    }


    private void populatePatientColumn(CommonPersonObjectClient pc, final SampleRequestViewHolder viewHolder) {
        try {

            String sampleId = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.SAMPLE_ID, true);
            String sampleType = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.SAMPLE_TYPE, true);
            String sampleRequestDate = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.SAMPLE_REQUEST_DATE, true);
            String sampleCollectionDate = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.SAMPLE_COLLECTION_DATE, true);
            String patientId = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.PATIENT_ID, false);


            String sampleProcessed = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.SAMPLE_PROCESSED, false);


            viewHolder.sampleId.setText(sampleId);
            viewHolder.requestDate.setText(sampleRequestDate);
            viewHolder.sampleType.setText(sampleType);
            viewHolder.sampleTakenDate.setText(sampleCollectionDate);
            viewHolder.patientId.setText(patientId);

            if (sampleProcessed == null || sampleProcessed.isEmpty()) {
                viewHolder.status.setText(context.getString(R.string.unprocessed));
                viewHolder.status.setTextColor(context.getResources().getColor(R.color.error_color));
            } else if (LabDao.isSampleUploaded(sampleId)) {
                viewHolder.status.setText(context.getString(R.string.uploaded));
                viewHolder.status.setTextColor(context.getResources().getColor(R.color.alert_complete_green));
            } else {
                viewHolder.status.setText(context.getString(R.string.processed));
                viewHolder.status.setTextColor(context.getResources().getColor(R.color.alert_in_progress_blue));
            }

            viewHolder.sampleRequestColumn.setOnClickListener(onClickListener);
            viewHolder.sampleRequestColumn.setTag(pc);
            viewHolder.sampleRequestColumn.setTag(R.id.VIEW_ID, BaseLabRequestsRegisterFragment.CLICK_VIEW_NORMAL);

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
    public SampleRequestViewHolder createViewHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.sample_requests_list_row, parent, false);
        return new SampleRequestViewHolder(view);
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

    public static class SampleRequestViewHolder extends RecyclerView.ViewHolder {
        public TextView sampleId;
        public TextView sampleType;
        public TextView requestDate;
        public TextView sampleTakenDate;

        public TextView status;

        public TextView patientId;
        public View sampleRequestColumn;

        public SampleRequestViewHolder(View itemView) {
            super(itemView);

            sampleId = itemView.findViewById(R.id.sample_id);
            sampleType = itemView.findViewById(R.id.sample_type);
            requestDate = itemView.findViewById(R.id.request_date);
            sampleTakenDate = itemView.findViewById(R.id.sample_taken_date);
            patientId = itemView.findViewById(R.id.patient_id);
            status = itemView.findViewById(R.id.status);
            sampleRequestColumn = itemView.findViewById(R.id.manifest_column);
        }
    }

}
