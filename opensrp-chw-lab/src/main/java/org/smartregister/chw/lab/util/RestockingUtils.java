package org.smartregister.chw.lab.util;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.smartregister.lab.R;
import org.smartregister.chw.lab.domain.Visit;
import org.smartregister.chw.lab.domain.VisitDetail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;

public class RestockingUtils {
    public static void extractVisit(Visit visit, String[] params, List<Map<String, String>> visits_details) {
        Map<String, String> map = new HashMap<>();
        for (String param : params) {
            try {
                List<VisitDetail> details = visit.getVisitDetails().get(param);
                map.put(param, getTexts(details));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        visits_details.add(map);
    }

    public static void processRestockingVisit(List<Map<String, String>> visits_details, View view, Activity context) {
        if (visits_details != null && visits_details.size() > 0) {
            for (Map<String, String> vals : visits_details) {
                String foundCondomTypes = getMapValue(vals, "condom_type");
                if (foundCondomTypes.contains(",")) {
                    String[] condomTypes = foundCondomTypes.split(",");
                    for (String condomType : condomTypes) {
                        populateData(view, vals, condomType.trim(), context);
                    }
                } else {
                    populateData(view, vals, foundCondomTypes, context);
                }

            }
        }
    }

    private static void populateData(View view, Map<String, String> vals, String condomType, Activity context) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat newFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());

        if (condomType.equalsIgnoreCase("male_condom")) {
            view.findViewById(R.id.male_condoms_details).setVisibility(View.VISIBLE);
            TextView tvType = view.findViewById(R.id.tv_type_male);
            TextView tvBrand = view.findViewById(R.id.tv_brand_male);
            TextView tvRestockingDate = view.findViewById(R.id.tv_restocking_date_male);
            TextView tvQuantity = view.findViewById(R.id.tv_quantity_male);
            TextView tvIssuingOrganization = view.findViewById(R.id.tv_issuing_organization_male);
            TextView tvIssuingOrganizationLabel = view.findViewById(R.id.tv_issuing_organization_male_label);


            tvType.setText(context.getString(context.getResources().getIdentifier(condomType, "string", context.getPackageName())));
            tvBrand.setText(context.getString(context.getResources().getIdentifier(getMapValue(vals, "male_condom_brand"), "string", context.getPackageName())));

            String condomRestockDate = getMapValue(vals, "condom_restock_date");

            Date date = null;
            try {
                date = originalFormat.parse(condomRestockDate);
            } catch (ParseException e) {
                Timber.e(e);
            }

            tvRestockingDate.setText(newFormat.format(date));
            tvQuantity.setText(getMapValue(vals, "restocked_male_condoms"));
            if (StringUtils.isNotBlank(getMapValue(vals, "issuing_organization"))) {
                tvIssuingOrganization.setText(getMapValue(vals, "issuing_organization").toUpperCase(Locale.ROOT));
            } else {
                tvIssuingOrganization.setVisibility(View.GONE);
                tvIssuingOrganizationLabel.setVisibility(View.GONE);
            }

        }
        if (condomType.equalsIgnoreCase("female_condom")) {
            view.findViewById(R.id.female_condoms_details).setVisibility(View.VISIBLE);
            TextView tvType = view.findViewById(R.id.tv_type_female);
            TextView tvBrand = view.findViewById(R.id.tv_brand_female);
            TextView tvRestockingDate = view.findViewById(R.id.tv_restocking_date_female);
            TextView tvQuantity = view.findViewById(R.id.tv_quantity_female);
            TextView tvIssuingOrganization = view.findViewById(R.id.tv_issuing_organization_female);
            TextView tvIssuingOrganizationLabel = view.findViewById(R.id.tv_issuing_organization_female_label);

            tvType.setText(context.getString(context.getResources().getIdentifier(condomType, "string", context.getPackageName())));
            tvBrand.setText(context.getString(context.getResources().getIdentifier(getMapValue(vals, "female_condom_brand"), "string", context.getPackageName())));

            String condomRestockDate = getMapValue(vals, "condom_restock_date");
            Date date = null;
            try {
                date = originalFormat.parse(condomRestockDate);
            } catch (ParseException e) {
                Timber.e(e);
            }
            tvRestockingDate.setText(newFormat.format(date));

            tvQuantity.setText(getMapValue(vals, "restocked_female_condoms"));
            if (StringUtils.isNotBlank(getMapValue(vals, "issuing_organization"))) {
                tvIssuingOrganization.setText(getMapValue(vals, "issuing_organization").toUpperCase(Locale.ROOT));
            } else {
                tvIssuingOrganization.setVisibility(View.GONE);
                tvIssuingOrganizationLabel.setVisibility(View.GONE);
            }
        }
    }

    public static String getTexts(List<VisitDetail> visitDetails) {
        if (visitDetails == null) return "";

        List<String> texts = new ArrayList<>();
        for (VisitDetail vd : visitDetails) {
            String val = getText(vd);
            if (StringUtils.isNotBlank(val)) texts.add(val.trim());
        }
        return toCSV(texts);
    }

    @NotNull
    public static String getText(@Nullable List<VisitDetail> visitDetails) {
        if (visitDetails == null) return "";

        List<String> vals = new ArrayList<>();
        for (VisitDetail vd : visitDetails) {
            String val = getText(vd);
            if (StringUtils.isNotBlank(val)) vals.add(val);
        }

        return toCSV(vals);
    }

    @NotNull
    public static String getText(@Nullable VisitDetail visitDetail) {
        if (visitDetail == null) return "";

        String val = visitDetail.getHumanReadable();
        if (StringUtils.isNotBlank(val)) return val.trim();

        return (StringUtils.isNotBlank(visitDetail.getDetails())) ? visitDetail.getDetails().trim() : "";
    }

    public static String toCSV(List<String> list) {
        String result = "";
        if (list.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (String s : list) {
                sb.append(s).append(", ");
            }
            result = sb.deleteCharAt(sb.length() - 2).toString();
        }
        return result.trim();
    }

    private static String getMapValue(Map<String, String> map, String key) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return "";
    }
}
