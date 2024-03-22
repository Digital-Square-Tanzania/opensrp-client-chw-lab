package org.smartregister.chw.lab.adapter;

import static org.smartregister.chw.lab.activity.CreateManifestActivity.selectedSamples;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.smartregister.chw.lab.domain.TestSampleItem;
import org.smartregister.lab.R;

import java.util.ArrayList;
import java.util.List;

public class TestSampleAdapter extends ArrayAdapter<TestSampleItem> {

    private final List<TestSampleItem> dataSet;
    Context mContext;

    public TestSampleAdapter(List<TestSampleItem> data, Context context) {
        super(context, R.layout.view_test_sample_item, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TestSampleItem dataModel = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.view_test_sample_item, parent, false);
            viewHolder.patientId = convertView.findViewById(R.id.patient_id);
            viewHolder.testSampleId = convertView.findViewById(R.id.sample_id);
            viewHolder.checkbox = convertView.findViewById(R.id.checkbox);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.testSampleId.setText(dataModel.getSampleId());
        viewHolder.patientId.setText(dataModel.getPatientId());

        viewHolder.checkbox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                selectedSamples.add(dataModel.getSampleId());
            } else {
                selectedSamples.remove(dataModel.getSampleId());
            }
        });


        return convertView;
    }

    @Nullable
    @Override
    public TestSampleItem getItem(int position) {
        return dataSet.get(position);
    }

    // View lookup cache
    private static class ViewHolder {
        TextView patientId;
        TextView testSampleId;
        CheckBox checkbox;
    }
}