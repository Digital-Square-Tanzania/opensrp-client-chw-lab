package org.smartregister.chw.lab.dao;

import org.smartregister.chw.lab.domain.OrderFeedbackObject;
import org.smartregister.chw.lab.domain.TestSampleItem;
import org.smartregister.chw.lab.util.Constants;
import org.smartregister.dao.AbstractDao;

import java.util.List;

public class LabDao extends AbstractDao {
    private static final String manifestsTable = Constants.TABLES.LAB_MANIFESTS;

    public static void insertManifest(String batchNumber,
                                       String manifestType,
                                       String destinationHub,
                                       String samplesList) {


        String sql = "INSERT INTO " + Constants.TABLES.LAB_MANIFESTS +
                "    (base_entity_id, batch_number, manifest_type, destination_hub, samples_list) " +
                "         VALUES ('" + batchNumber + "', '" + batchNumber + "', '" + manifestType + "', '" + destinationHub + "', '" + samplesList + "')" +
                "       ON CONFLICT (id) DO UPDATE" +
                "       SET batch_number = '" + batchNumber + "'," +
                "           manifest_type = '" + manifestType + "', " +
                "           destination_hub = '" + destinationHub + "', " +
                "           samples_list = '" + samplesList + "'" +
                "       ";
        updateDB(sql);
    }

    public static void updateManifest(String batchNumber,
                                       String dispatchDate,
                                       String dispatchTime) {


        String sql = "UPDATE" + Constants.TABLES.LAB_MANIFESTS +
                "       SET dispatch_date = '" + dispatchDate + "'," +
                "           dispatch_time = '" + dispatchTime + "'" +
                "       WHERE batch_number = '"+batchNumber+"'";
        updateDB(sql);
    }


    public static OrderFeedbackObject getFeedbackObject(String requestReference) {
        String sql = "SELECT * FROM  " + manifestsTable +
                "           WHERE request_reference = '" + requestReference + "'";

        DataMap<OrderFeedbackObject> dataMap = cursor -> {
            OrderFeedbackObject orderFeedbackObject = new OrderFeedbackObject();

            orderFeedbackObject.setCondomType(getCursorValue(cursor, "condom_type"));
            orderFeedbackObject.setCondomBrand(getCursorValue(cursor, "condom_brand"));
            orderFeedbackObject.setResponseDate(getCursorValue(cursor, "response_at"));
            orderFeedbackObject.setResponseQuantity(getCursorValue(cursor, "quantity_response"));
            orderFeedbackObject.setResponseStatus(getCursorValue(cursor, "response_status"));


            return orderFeedbackObject;
        };

        List<OrderFeedbackObject> res = readData(sql, dataMap);
        if (res == null || res.size() != 1)
            return null;

        return res.get(0);
    }

    public static boolean isSampleUploaded(String sampleId) {
        return false;
    }


    public static List<TestSampleItem> getTestSamplesRequests(String sampleType) {
        String sql = "SELECT * FROM " + Constants.TABLES.LAB_TEST_REQUESTS + " ts " + "WHERE sample_type = '" + sampleType + "' AND sample_processed = 'true'";


        DataMap<TestSampleItem> dataMap = cursor -> {
            TestSampleItem testSampleItem = new TestSampleItem();

            testSampleItem.setSampleId(getCursorValue(cursor, "sample_id", ""));
            testSampleItem.setPatientId(getCursorValue(cursor, "patient_id", ""));


            return testSampleItem;
        };


        return readData(sql, dataMap);
    }

    public static boolean isSampleProcessed(String sampleId) {
        String sql = "SELECT sample_processed FROM " + Constants.TABLES.LAB_TEST_REQUESTS + " p WHERE p.sample_id = '" + sampleId + "'";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "sample_processed");
        List<String> res = readData(sql, dataMap);

        if (res != null && res.size() > 0 && res.get(0) != null) {
            return res.get(0).equalsIgnoreCase("true");
        }
        return false;
    }

}
