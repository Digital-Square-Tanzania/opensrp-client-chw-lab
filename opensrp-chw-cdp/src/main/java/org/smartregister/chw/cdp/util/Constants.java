package org.smartregister.chw.cdp.util;

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
        String CDP_OUTLET_REGISTRATION = "CDP Outlet Registration";
        String UPDATE_CDP_OUTLET_REGISTRATION = "Update CDP Outlet Registration";
        String DISABLE_CDP_OUTLET = "Disable CDP Outlet";
        String ENABLE_CDP_OUTLET = "Enable CDP Outlet";
        String CDP_FOLLOW_UP_VISIT = "CDP Follow-up Visit";
        String CDP_OUTLET_VISIT = "CDP Outlet Visit";
        String CDP_RECEIVE_FROM_FACILITY = "CDP Receive From Facility";
        String CDP_RESTOCK = "CDP Restock";
        String CDP_CONDOM_ORDER = "CDP Condom Order";
        String CDP_ORDER_FROM_FACILITY = "CDP Order From Facility";
        String CDP_ORDER_FEEDBACK = "CDP Order Feedback";
        String CDP_ORDER_FEEDBACK_OWN_COPY = "CDP Order Feedback Own Copy";
        String CDP_CONDOM_DISTRIBUTION_OUTSIDE = "CDP Distribution Outside Facility";
        String CDP_CONDOM_DISTRIBUTION_WITHIN = "CDP Distribution Within Facility";
    }

    interface FORMS {
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
        String CDP_OUTLET = "ec_cdp_outlet";
        String CDP_OUTLET_STOCK_COUNT = "ec_cdp_outlet_stock_count";
        String CDP_FOLLOW_UP = "ec_cdp_follow_up_visit";
        String CDP_STOCK_COUNT = "ec_cdp_stock_count";
        String CDP_STOCK_LOG = "ec_cdp_stock_log";
        String CDP_OUTLET_VISIT = "ec_cdp_outlet_visit";
        String CDP_ORDERS = "ec_cdp_orders";
        String TASK = "task";
        String CDP_ORDER_FEEDBACK = "ec_cdp_order_feedback";
    }

    interface ACTIVITY_PAYLOAD {
        String BASE_ENTITY_ID = "BASE_ENTITY_ID";
        String FAMILY_BASE_ENTITY_ID = "FAMILY_BASE_ENTITY_ID";
        String ACTION = "ACTION";
        String CDP_FORM_NAME = "CDP_FORM_NAME";
        String OUTLET_OBJECT = "OUTLET_OBJECT";
        String CLIENT = "CLIENT";
    }

    interface ACTIVITY_PAYLOAD_TYPE {
        String REGISTRATION = "REGISTRATION";
        String FOLLOW_UP_VISIT = "FOLLOW_UP_VISIT";
    }

    interface CONFIGURATION {
        String CDP_CONFIRMATION = "cdp_confirmation";
    }

    interface CDP_MEMBER_OBJECT {
        String MEMBER_OBJECT = "memberObject";
    }

    interface KEY {
        String PHOTO = "photo";
    }

    interface JSON_FORM_KEY {
        String UNIQUE_ID = "unique_id";
        String FEMALE_CONDOMS_OFFSET = "female_condoms_offset";
        String MALE_CONDOMS_OFFSET = "male_condoms_offset";
        String CONDOM_TYPE = "condom_type";
        String CONDOM_BRAND = "condom_brand";
        String CONDOMS_REQUESTED = "requested_condoms";
        String REQUEST_TYPE = "request_type";
        String QUANTITY_RES = "quantity_response";
        String RESPONSE_STATUS = "response_status";
        String REQUEST_REFERENCE = "request_reference";
        String RESPONSE_DATE = "response_date";
        String CONDOM_REQUEST_DATE = "condom_order_date";
        String CONDOM_RESTOCK_DATE = "condom_restock_date";
        String RECEIVING_ORDER_FACILITY = "receiving_order_facility";
        String STOCK_EVENT_TYPE = "stock_event_type";
        String ISSUING_ORGANIZATION = "issuing_organization";
    }

    interface STOCK_EVENT_TYPES {
        String INCREMENT = "increment";
        String DECREMENT = "decrement";
    }

    interface ORDER_TYPES {
        String COMMUNITY_TO_FACILITY_ORDER = "community_to_facility";
        String FACILITY_TO_FACILITY_ORDER = "facility_to_facility";
    }

    interface OrderStatus {
        String READY = "READY";
        String FAILED = "CANCELLED";
        String COMPLETE = "COMPLETED";
        String IN_TRANSIT = "IN_PROGRESS";
    }

    interface ResponseStatus {
        String OUT_OF_STOCK = "out_of_stock";
        String RESTOCKED = "restocked";
    }
}