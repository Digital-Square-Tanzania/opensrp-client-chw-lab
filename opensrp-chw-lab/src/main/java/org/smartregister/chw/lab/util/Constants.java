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

        String LAB_MANIFEST_SETTINGS = "lab_manifest_settings";

        String CDP_OUTLET_REGISTRATION = "cdp_outlet_registration";
        String EDIT_CDP_OUTLET = "edit_cdp_outlet";
        String CDP_OUTLET_RESTOCK = "cdp_outlet_restock";
        String CD_OUTLET_VISIT = "cdp_outlet_visit";
        String CDP_CONDOM_ORDER = "cdp_condom_order";
        String CDP_CONDOM_DISTRIBUTION = "cdp_condom_distribution_outside";
        String CDP_CONDOM_ORDER_FACILITY = "cdp_facility_order_form";
        String CDP_RECEIVE_CONDOM_FROM_ORGANIZATIONS = "cdp_receive_condom_from_organizations";
        String CDP_RECEIVE_CONDOM_FACILITY = "cdp_receive_condom_facility";
        String CDP_CONDOM_DISTRIBUTION_WITHIN = "cdp_condom_distribution_within";
    }

    interface TABLES {
        String LAB_TEST_REQUESTS = "ec_lab_requests";

        String LAB_MANIFESTS = "ec_lab_manifests";

        String LAB_MANIFEST_SETTINGS = "ec_lab_settings";
    }

    interface ACTIVITY_PAYLOAD {
        String BASE_ENTITY_ID = "BASE_ENTITY_ID";
        String TEST_SAMPLE_ID = "TEST_SAMPLE_ID";
        String FAMILY_BASE_ENTITY_ID = "FAMILY_BASE_ENTITY_ID";
        String ACTION = "ACTION";
        String LAB_FORM_NAME = "LAB_FORM_NAME";
        String CLIENT = "CLIENT";
        String MANIFEST_TYPE = "MANIFEST_TYPE";
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

        String DESTINATION_HUB_NAME = "destination_hub_name";

        String DESTINATION_HUB_UUID = "destination_hub_uuid";
    }

}