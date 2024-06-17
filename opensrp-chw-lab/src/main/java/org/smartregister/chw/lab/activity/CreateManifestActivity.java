package org.smartregister.chw.lab.activity;

import static org.smartregister.chw.lab.util.LabUtil.generateManifestId;
import static org.smartregister.chw.lab.util.LabUtil.persistEvent;
import static org.smartregister.util.JsonFormUtils.generateRandomUUIDString;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.smartregister.chw.lab.LabLibrary;
import org.smartregister.chw.lab.adapter.TestSampleAdapter;
import org.smartregister.chw.lab.dao.LabDao;
import org.smartregister.chw.lab.util.Constants;
import org.smartregister.chw.lab.util.LabUtil;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;
import org.smartregister.domain.Location;
import org.smartregister.lab.R;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.LocationRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

public class CreateManifestActivity extends AppCompatActivity {
    public static List<String> selectedSamples = new ArrayList<>();
    protected String manifestType;

    private boolean selectedAll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manifestType = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.MANIFEST_TYPE);

        setContentView(R.layout.activity_create_manifest);

        View closeView = findViewById(R.id.close);
        closeView.setOnClickListener(view -> finish());

        View selectAllView = findViewById(R.id.select_all);

        TextView textView = findViewById(R.id.customFontTextViewName);
        textView.setText(R.string.create_manifest);

        TextView submit = findViewById(R.id.customFontTextViewSubmit);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            submit.setOnClickListener(view -> generateManifest());
        }

        ListView listView = findViewById(R.id.multiple_list_view);

        TestSampleAdapter arrayAdapter = new TestSampleAdapter(LabDao.getTestSamplesRequestsNotInManifests(manifestType), CreateManifestActivity.this);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            String items = (String) adapterView.getItemAtPosition(i);
            Toast.makeText(CreateManifestActivity.this, "select by list of item" + items, Toast.LENGTH_SHORT).show();
        });

        selectAllView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedAll) {
                    arrayAdapter.deselectAll();
                    selectedAll = false;
                } else {
                    arrayAdapter.selectAll();
                    selectedAll = true;
                }
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void generateManifest() {
        AllSharedPreferences allSharedPreferences = LabLibrary.getInstance().context().allSharedPreferences();
        Event baseEvent = (Event) new Event().withBaseEntityId(UUID.randomUUID().toString()).withEventDate(new Date()).withFormSubmissionId(generateRandomUUIDString()).withEventType(Constants.EVENT_TYPE.LAB_MANIFEST_GENERATION).withEntityType(Constants.TABLES.LAB_MANIFESTS).withProviderId(allSharedPreferences.fetchRegisteredANM()).withLocationId(allSharedPreferences.fetchUserLocalityId(allSharedPreferences.fetchRegisteredANM())).withTeamId(allSharedPreferences.fetchDefaultTeamId(allSharedPreferences.fetchRegisteredANM())).withTeam(allSharedPreferences.fetchDefaultTeam(allSharedPreferences.fetchRegisteredANM())).withClientDatabaseVersion(LabLibrary.getInstance().getDatabaseVersion()).withClientApplicationVersion(LabLibrary.getInstance().getApplicationVersion()).withDateCreated(new Date());

        baseEvent.addObs(new Obs().withFormSubmissionField(Constants.JSON_FORM_KEY.BATCH_NUMBER).withValue(generateManifestId()).withFieldCode(Constants.JSON_FORM_KEY.BATCH_NUMBER).withFieldType("formsubmissionField").withFieldDataType("text").withParentCode("").withHumanReadableValues(new ArrayList<>()));

        baseEvent.addObs(new Obs().withFormSubmissionField(Constants.JSON_FORM_KEY.MANIFEST_TYPE).withValue(manifestType).withFieldCode(Constants.JSON_FORM_KEY.MANIFEST_TYPE).withFieldType("formsubmissionField").withFieldDataType("text").withParentCode("").withHumanReadableValues(new ArrayList<>()));

        baseEvent.addObs(new Obs().withFormSubmissionField(Constants.JSON_FORM_KEY.DESTINATION_HUB_NAME).withValue(LabDao.getDestinationHubName()).withFieldCode(Constants.JSON_FORM_KEY.DESTINATION_HUB_NAME).withFieldType("formsubmissionField").withFieldDataType("text").withParentCode("").withHumanReadableValues(new ArrayList<>()));

        baseEvent.addObs(new Obs().withFormSubmissionField(Constants.JSON_FORM_KEY.DESTINATION_HUB_UUID).withValue(LabDao.getDestinationHubUuid()).withFieldCode(Constants.JSON_FORM_KEY.DESTINATION_HUB_UUID).withFieldType("formsubmissionField").withFieldDataType("text").withParentCode("").withHumanReadableValues(new ArrayList<>()));

        baseEvent.addObs(new Obs().withFormSubmissionField(Constants.JSON_FORM_KEY.SOURCE_FACILITY).withValue(LabUtil.getFacilityHfrCode()).withFieldCode(Constants.JSON_FORM_KEY.SOURCE_FACILITY).withFieldType("formsubmissionField").withFieldDataType("text").withParentCode("").withHumanReadableValues(new ArrayList<>()));

        (new LinkedHashSet<>(selectedSamples)).toString();

        JSONArray selectedSamplesString = new JSONArray();

        for(String sample : selectedSamples){
            selectedSamplesString.put(sample);
        }

        baseEvent.addObs(new Obs().withFormSubmissionField(Constants.JSON_FORM_KEY.SAMPLES_LIST).withValue((selectedSamplesString.toString())).withFieldCode(Constants.JSON_FORM_KEY.SAMPLES_LIST).withFieldType("formsubmissionField").withFieldDataType("text").withParentCode("").withHumanReadableValues(new ArrayList<>()));

        persistEvent(baseEvent);
        LabUtil.startClientProcessing();
        finish();
    }
}