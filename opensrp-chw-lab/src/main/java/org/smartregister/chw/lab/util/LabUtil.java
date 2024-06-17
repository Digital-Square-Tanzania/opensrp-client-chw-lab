package org.smartregister.chw.lab.util;

import static org.smartregister.util.Utils.getAllSharedPreferences;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.api.constants.Gender;
import org.smartregister.lab.R;
import org.smartregister.chw.lab.LabLibrary;
import org.smartregister.chw.lab.contract.BaseCdpCallDialogContract;
import org.smartregister.chw.lab.pojo.RegisterParams;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.domain.Location;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.LocationRepository;
import org.smartregister.repository.UniqueIdRepository;
import org.smartregister.sync.ClientProcessorForJava;
import org.smartregister.sync.helper.ECSyncHelper;
import org.smartregister.util.JsonFormUtils;
import org.smartregister.util.PermissionUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import timber.log.Timber;

public class LabUtil {

    public static void processEvent(AllSharedPreferences allSharedPreferences, Event baseEvent) throws Exception {
        if (baseEvent != null) {
            LabJsonFormUtils.tagEvent(allSharedPreferences, baseEvent);
            JSONObject eventJson = new JSONObject(LabJsonFormUtils.gson.toJson(baseEvent));

            getSyncHelper().addEvent(baseEvent.getBaseEntityId(), eventJson, BaseRepository.TYPE_Unprocessed);
            startClientProcessing();
        }
    }

    public static void startClientProcessing() {
        try {
            long lastSyncTimeStamp = getAllSharedPreferences().fetchLastUpdatedAtDate(0);
            Date lastSyncDate = new Date(lastSyncTimeStamp);
            getClientProcessorForJava().processClient(getSyncHelper().getEvents(lastSyncDate, BaseRepository.TYPE_Unprocessed));
            getAllSharedPreferences().saveLastUpdatedAtDate(lastSyncDate.getTime());
        } catch (Exception e) {
            Timber.d(e);
        }
    }

    public static ECSyncHelper getSyncHelper() {
        return LabLibrary.getInstance().getEcSyncHelper();
    }

    public static ClientProcessorForJava getClientProcessorForJava() {
        return LabLibrary.getInstance().getClientProcessorForJava();
    }

    public static Spanned fromHtml(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(text);
        }
    }

    @SuppressLint("HardwareIds")
    public static boolean launchDialer(final Activity activity, final BaseCdpCallDialogContract.View callView, final String phoneNumber) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            // set a pending call execution request
            if (callView != null) {
                callView.setPendingCallRequest(() -> LabUtil.launchDialer(activity, callView, phoneNumber));
            }

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, PermissionUtils.PHONE_STATE_PERMISSION_REQUEST_CODE);

            return false;
        } else {

            if (((TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number()
                    == null) {

                Timber.i("No dial application so we launch copy to clipboard...");

                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(activity.getText(R.string.copied_phone_number), phoneNumber);
                clipboard.setPrimaryClip(clip);

                CopyToClipboardDialog copyToClipboardDialog = new CopyToClipboardDialog(activity, R.style.copy_clipboard_dialog);
                copyToClipboardDialog.setContent(phoneNumber);
                copyToClipboardDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                copyToClipboardDialog.show();
                // no phone
                Toast.makeText(activity, activity.getText(R.string.copied_phone_number), Toast.LENGTH_SHORT).show();

            } else {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                activity.startActivity(intent);
            }
            return true;
        }
    }

    public static void saveFormEvent(final String jsonString) throws Exception {
        AllSharedPreferences allSharedPreferences = LabLibrary.getInstance().context().allSharedPreferences();
        Event baseEvent = LabJsonFormUtils.processJsonForm(allSharedPreferences, jsonString);
        if (baseEvent.getBaseEntityId().isEmpty()) {
            baseEvent.setBaseEntityId(UUID.randomUUID().toString());
        }
        LabUtil.processEvent(allSharedPreferences, baseEvent);
    }

    public static int getMemberProfileImageResourceIdentifier(String entityType) {
        return R.mipmap.ic_member;
    }

    /**
     * @param timestamp timestamp
     * @return 'dd-mm-yyyy'
     */
    public static String formatTimeStamp(Long timestamp) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ROOT);
        return df.format(timestamp);
    }

    /**
     * Get the location name from Id
     *
     * @param locationId Location uuid in string
     * @return location name
     */
    public static String getRequesterNameFromId(String locationId) {
        LocationRepository locationRepository = LabLibrary.getInstance().context().getLocationRepository();
        Location location = locationRepository.getLocationById(locationId);
        if (location != null && location.getProperties() != null) {
            return location.getProperties().getName();
        }
        return "";
    }


    public static String getGenderTranslated(Context context, String gender) {
        if (gender.equalsIgnoreCase(Gender.MALE.toString())) {
            return context.getResources().getString(R.string.male);
        } else if (gender.equalsIgnoreCase(Gender.FEMALE.toString())) {
            return context.getResources().getString(R.string.female);
        }
        return "";
    }

    public static int getStringFromResources(String identifier, Activity activity) {
        return activity.getResources().getIdentifier(identifier, "string", activity.getPackageName());
    }

    public static void addEvent(RegisterParams params, List<String> currentFormSubmissionIds, Event baseEvent) throws JSONException {
        if (baseEvent != null) {
            JSONObject eventJson = new JSONObject(LabJsonFormUtils.gson.toJson(baseEvent));
            getSyncHelper().addEvent(baseEvent.getBaseEntityId(), eventJson, params.getStatus());
            currentFormSubmissionIds.add(eventJson.getString(EventClientRepository.event_column.formSubmissionId.toString()));
        }
    }

    public static void updateOpenSRPId(String jsonString, RegisterParams params, Client baseClient) {
        if (params.isEditMode()) {
            // UnAssign current OpenSRP ID
            if (baseClient != null) {
                String newOpenSrpId = baseClient.getIdentifier(LabJsonFormUtils.OPENSRP_ID).replace("-", "");
                String currentOpenSrpId = JsonFormUtils.getString(jsonString, LabJsonFormUtils.CURRENT_OPENSRP_ID).replace("-", "");
                if (!newOpenSrpId.equals(currentOpenSrpId)) {
                    //OpenSRP ID was changed
                    getUniqueIdRepository().open(currentOpenSrpId);
                }
            }

        } else {
            if (baseClient != null) {
                String openSrpId = baseClient.getIdentifier(LabJsonFormUtils.OPENSRP_ID);
                if (StringUtils.isNotBlank(openSrpId)) {
                    //Mark OpenSRP ID as used
                    getUniqueIdRepository().close(openSrpId);
                }
            }
        }
    }

    public static UniqueIdRepository getUniqueIdRepository() {
        return LabLibrary.getInstance().context().getUniqueIdRepository();
    }

    public static void persistEvent(Event baseEvent) {
        JSONObject eventJson = null;
        try {
            eventJson = new JSONObject(LabJsonFormUtils.gson.toJson(baseEvent));
            LabUtil.getSyncHelper().addEvent(baseEvent.getBaseEntityId(), eventJson, BaseRepository.TYPE_Unprocessed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static String getHfrCode() {
        String attribute = LabLibrary.getInstance().context().allSharedPreferences().fetchUserLocationAttribute();
        if (attribute != null) {
            String[] attributesArray = attribute.split(",");
            for (String attributeName : attributesArray) {
                if (attributeName.trim().startsWith("HFR Code:")) {
                    return attributeName.trim().substring(9).trim().replace("-","");
                }
            }
        }
        return "";
    }

    public static String getFacilityHfrCode() {
        String attribute = LabLibrary.getInstance().context().allSharedPreferences().fetchUserLocationAttribute();
        if (attribute != null) {
            String[] attributesArray = attribute.split(",");
            for (String attributeName : attributesArray) {
                if (attributeName.trim().startsWith("HFR Code:")) {
                    return attributeName.trim().substring(9).trim();
                }
            }
        }
        return "";
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String generateManifestId() {
        // Get current date and time
        LocalDateTime now = LocalDateTime.now();

        // Extract year, month, and minutes
        int year = now.getYear() % 100; // Last two digits of the year
        int month = now.getMonthValue();
        int minutes = now.getMinute() + now.getHour() * 60; // Minutes since midnight

        // Format year and month with leading zeros if necessary
        String formattedYear = String.format(Locale.getDefault(),"%02d", year);
        String formattedMonth = String.format(Locale.getDefault(),"%02d", month);

        // Format minutes with leading zeros if necessary
        String formattedMinutes = String.format(Locale.getDefault(),"%05d", minutes);

        // Generate the number in the specified format
        return getHfrCode()+formattedYear + formattedMonth + formattedMinutes;
    }
}
