package org.smartregister.chw.cdp.holders;

import android.view.View;
import android.widget.TextView;

import org.smartregister.cdp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrdersViewHolder extends RecyclerView.ViewHolder {

    public TextView condom_type;
    public TextView condom_brand;
    public TextView health_facility;
    public TextView health_facility_label;
    public TextView request_date;
    public TextView request_date_label;
    public TextView quantity;
    public TextView status;

    public View registerColumns;

    public OrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        condom_type = itemView.findViewById(R.id.condom_type);
        condom_brand = itemView.findViewById(R.id.condom_brand);
        health_facility = itemView.findViewById(R.id.health_facility);
        health_facility_label = itemView.findViewById(R.id.health_facility_label);
        request_date_label = itemView.findViewById(R.id.request_date_label);
        request_date = itemView.findViewById(R.id.request_date);
        quantity = itemView.findViewById(R.id.condom_quantity);
        status = itemView.findViewById(R.id.order_status);
        registerColumns = itemView.findViewById(R.id.register_columns);
    }
}
