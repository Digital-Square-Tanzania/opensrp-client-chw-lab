package org.smartregister.chw.lab.holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.smartregister.lab.R;

public class ManifestViewHolder extends RecyclerView.ViewHolder {
    public TextView batchNumber;

    public TextView manifestType;

    public TextView destinationHub;

    public TextView status;

    public View registerColumns;

    public ManifestViewHolder(@NonNull View itemView) {
        super(itemView);

        batchNumber = itemView.findViewById(R.id.batch_number);
        manifestType = itemView.findViewById(R.id.manifest_type);
        destinationHub = itemView.findViewById(R.id.destination_hub);
        status = itemView.findViewById(R.id.status);
    }
}
