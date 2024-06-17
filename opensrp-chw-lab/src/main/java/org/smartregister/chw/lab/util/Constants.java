package org.smartregister.chw.lab.util;

public interface Constants {

    int REQUEST_CODE_GET_JSON = 2244;
    String ENCOUNTER_TYPE = "encounter_type";
    String STEP_ONE = "step1";
    String STEP_TWO = "step2";

    interface JSON_FORM_EXTRA {
        String JSON = "json";
        String ENCOUNTER_TYPE = "encounter_type";
        String ENTITY_ID = "ENTITY_ID";
    }

    interface EVENT_TYPE {
        String LAB_TEST_REQUEST_REGISTRATION = "LAB Test Request Registration";

        String LAB_MANIFEST_GENERATION = "LAB Manifest Generation";

        String LAB_MANIFEST_DISPATCH = "LAB Manifest Dispatch";

        String LAB_SET_MANIFEST_SETTINGS = "Set Manifest Settings";

        String LAB_TEST_REQUEST_PREPARATION = "LAB Test Request Preparation";
    }

    interface FORMS {
        String LAB_HVL_SAMPLE_COLLECTION = "lab_hvl_sample_collection";

        String LAB_HEID_SAMPLE_COLLECTION = "lab_heid_sample_collection";

        String LAB_HVL_PROCESSING = "lab_hvl_processing";

        String LAB_HVL_RESULTS = "lab_hvl_results";

        String LAB_HEID_RESULTS = "lab_heid_results";

        String LAB_MANIFEST_SETTINGS = "lab_manifest_settings";

        String LAB_MANIFEST_DISPATCH = "lab_manifest_dispatch";

        String CDP_OUTLET_REGISTRATION = "cdp_outlet_registration";

    }

    interface TABLES {
        String LAB_TEST_REQUESTS = "ec_lab_requests";

        String LAB_MANIFESTS = "ec_lab_manifests";

        String LAB_MANIFEST_SETTINGS = "ec_lab_settings";
    }

    interface ACTIVITY_PAYLOAD {
        String BASE_ENTITY_ID = "BASE_ENTITY_ID";
        String TEST_SAMPLE_ID = "TEST_SAMPLE_ID";
        String BATCH_NUMBER = "BATCH_NUMBER";
        String FAMILY_BASE_ENTITY_ID = "FAMILY_BASE_ENTITY_ID";
        String ACTION = "ACTION";
        String LAB_FORM_NAME = "LAB_FORM_NAME";
        String CLIENT = "CLIENT";
        String MANIFEST_TYPE = "MANIFEST_TYPE";
        String PROVIDE_RESULTS_TO_CLIENT = "PROVIDE_RESULTS_TO_CLIENT";
    }

    interface ACTIVITY_PAYLOAD_TYPE {
        String REGISTRATION = "REGISTRATION";
        String FOLLOW_UP_VISIT = "FOLLOW_UP_VISIT";
    }

    interface MANIFEST_TYPE {
        String HVL = "HVL";
        String HEID = "HEID";
    }

    interface CONFIGURATION {
        String LAB_CONFIGURATION = "lab_configuration";
    }

    interface CDP_MEMBER_OBJECT {
        String MEMBER_OBJECT = "memberObject";
    }

    interface JSON_FORM_KEY {
        String BATCH_NUMBER = "batch_number";

        String MANIFEST_TYPE = "manifest_type";

        String SAMPLES_LIST = "samples_list";

        String DISPATCH_DATE = "dispatch_date";

        String DISPATCH_TIME = "dispatch_time";

        String DISPATCHER_NAME = "dispatcher_name";

        String DESTINATION_HUB_NAME = "destination_hub_name";

        String DESTINATION_HUB_UUID = "destination_hub_uuid";

        String SOURCE_FACILITY = "source_facility";
    }

    interface SAMPLE_TYPES {
        String HEID = "HEID";
        String HVL = "HVL";
    }
}