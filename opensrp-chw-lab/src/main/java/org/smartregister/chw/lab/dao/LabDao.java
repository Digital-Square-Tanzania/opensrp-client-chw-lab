package org.smartregister.chw.lab.dao;

import org.smartregister.chw.lab.domain.Manifest;
import org.smartregister.chw.lab.domain.TestSample;
import org.smartregister.chw.lab.util.Constants;
import org.smartregister.dao.AbstractDao;

import java.util.List;

public class LabDao extends AbstractDao {
    private static final String manifestsTable = Constants.TABLES.LAB_MANIFESTS;


    private static final DataMap<TestSample> dataMap = cursor -> {
        TestSample testSample = new TestSample();

        testSample.setSampleId(getCursorValue(cursor, "sample_id", ""));
        testSample.setPatientId(getCursorValue(cursor, "patient_id", ""));
        testSample.setSampleProcessed(getCursorValue(cursor, "sample_processed", ""));
        testSample.setSampleType(getCursorValue(cursor, "sample_type", ""));
        testSample.setResults(getCursorValue(cursor, "results", ""));
        testSample.setSampleRequestDate(getCursorValue(cursor, "sample_request_date", ""));
        testSample.setTypeOfSample(getCursorValue(cursor, "type_of_sample", ""));
        testSample.setSampleCollectionDate(getCursorValue(cursor, "sample_collection_date", ""));
        testSample.setSampleCollectionTime(getCursorValue(cursor, "sample_collection_time", ""));
        testSample.setSeparationDoneAtTheFacility(getCursorValue(cursor, "separation_done_at_the_facility", ""));
        testSample.setSampleSeparationDate(getCursorValue(cursor, "sample_separation_date", ""));
        testSample.setSampleSeparationTime(getCursorValue(cursor, "sample_separation_time", ""));

        testSample.setTestedBy(getCursorValue(cursor, "tested_by", ""));
        testSample.setAuthorizedBy(getCursorValue(cursor, "authorized_by", ""));
        testSample.setTestedDate(getCursorValue(cursor, "tested_date", ""));
        testSample.setAuthorizedDate(getCursorValue(cursor, "authorized_date", ""));
        testSample.setReasonsForRejection(getCursorValue(cursor, "reasons_for_rejection", ""));
        testSample.setRejectionDate(getCursorValue(cursor, "rejection_date", ""));
        testSample.setRejectedBy(getCursorValue(cursor, "rejected_by", ""));
        testSample.setRejectionContactInfo(getCursorValue(cursor, "rejection_contact_info", ""));

        testSample.setDateResultsProvidedToClient(getCursorValue(cursor, "date_results_provided_to_client", ""));
        testSample.setTimeResultsProvidedToClient(getCursorValue(cursor, "time_results_provided_to_client", ""));

        return testSample;
    };


    private static final DataMap<Manifest> manifestDataMap = cursor -> {
        Manifest manifest = new Manifest();

        manifest.setBatchNumber(getCursorValue(cursor, "batch_number", ""));
        manifest.setManifestType(getCursorValue(cursor, "manifest_type", ""));
        manifest.setDestinationHubName(getCursorValue(cursor, "destination_hub_name", ""));
        manifest.setDestinationHubUuid(getCursorValue(cursor, "destination_hub_uuid", ""));
        manifest.setDispatchDate(getCursorValue(cursor, "dispatch_date", ""));
        manifest.setDispatchTime(getCursorValue(cursor, "dispatch_time", ""));
        manifest.setSampleList(getCursorValue(cursor, "samples_list", ""));


        return manifest;
    };


    /**
     * Saves a destination hub into the database. If the destination hub already exists, it updates the existing record.
     *
     * @param destinationHubName The name of the destination hub.
     * @param destinationHubUuid The unique identifier of the destination hub.
     */
    public static void saveDestinationHub(String destinationHubName, String destinationHubUuid) {
        String sql = "INSERT INTO " + Constants.TABLES.LAB_MANIFEST_SETTINGS +
                "    (base_entity_id, destination_hub_name, destination_hub_uuid) " +
                "         VALUES ('" + destinationHubUuid + "','" + destinationHubName + "', '" + destinationHubUuid + "')" +
                "       ON CONFLICT (id) DO UPDATE" +
                "       SET destination_hub_name = '" + destinationHubName + "'," +
                "           destination_hub_uuid = '" + destinationHubUuid + "'" +
                "       ";
        updateDB(sql);
    }

    /**
     * Retrieves the name of the destination hub from the database.
     *
     * @return The name of the destination hub or an empty string if not found.
     */
    public static String getDestinationHubName() {
        String sql = "SELECT destination_hub_name FROM " + Constants.TABLES.LAB_MANIFEST_SETTINGS + " p ";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "destination_hub_name");
        List<String> res = readData(sql, dataMap);

        if (res != null && !res.isEmpty()) {
            return res.get(res.size() - 1);
        }
        return "";
    }


    /**
     * Retrieves the unique identifier (UUID) of the destination hub from the database.
     * This method queries the {@code LAB_MANIFEST_SETTINGS} table for the {@code destination_hub_uuid} column,
     * returning the UUID of the destination hub stored in the database. It's designed to return the UUID
     * of the first destination hub found if multiple entries exist. This method should be used when there
     * is an expectation of a single or default destination hub in the database. If no destination hub is
     * found, this method returns an empty string.
     *
     * @return The UUID of the destination hub as a {@link String}. If no hub is found, returns an empty string.
     */
    public static String getDestinationHubUuid() {
        String sql = "SELECT destination_hub_uuid FROM " + Constants.TABLES.LAB_MANIFEST_SETTINGS + " p ";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "destination_hub_uuid");
        List<String> res = readData(sql, dataMap);

        if (res != null && !res.isEmpty()) {
            return res.get(res.size() - 1);
        }
        return "";
    }

    /**
     * Inserts a new manifest record into the database or updates an existing one based on the batch number.
     * This method uses a conflict resolution strategy to either insert a new record or update an existing record
     * if the batch number already exists in the {@code LAB_MANIFESTS} table. The manifest includes details such
     * as the batch number, manifest type, destination hub, and a list of samples.
     *
     * @param batchNumber    The batch number of the manifest, used as a unique identifier.
     * @param manifestType   The type of manifest.
     * @param destinationHub The name of the destination hub associated with this manifest.
     * @param samplesList    A comma-separated list of sample IDs included in this manifest.
     */
    public static void insertManifest(String batchNumber,
                                      String manifestType,
                                      String destinationHub,
                                      String samplesList,
                                      String lastInteractedWith) {
        String sql = "INSERT INTO " + Constants.TABLES.LAB_MANIFESTS +
                "    (id,base_entity_id, batch_number, manifest_type, destination_hub_name, samples_list, last_interacted_with) " +
                "         VALUES ('" + batchNumber + "', '" + batchNumber + "', '" + batchNumber + "', '" + manifestType + "', '" + destinationHub + "', '" + samplesList + "', '" + lastInteractedWith + "')" +
                "       ON CONFLICT (id) DO UPDATE" +
                "       SET batch_number = '" + batchNumber + "'," +
                "           manifest_type = '" + manifestType + "', " +
                "           destination_hub_name = '" + destinationHub + "', " +
                "           samples_list = '" + samplesList + "', " +
                "           last_interacted_with = '" + lastInteractedWith + "'" +
                "       ";
        updateDB(sql);
    }

    /**
     * Updates the dispatch details of an existing manifest identified by the batch number. This method modifies
     * the dispatch date, dispatch time, and dispatcher's name in the {@code LAB_MANIFESTS} table for the specified
     * manifest. It's designed to be used when the manifest needs to be dispatched or when updating dispatch information.
     *
     * @param batchNumber    The unique batch number of the manifest to update. This acts as the identifier to locate
     *                       the specific manifest record in the database.
     * @param dispatchDate   The new dispatch date to be set for the manifest. Format should match the database schema,
     *                       typically "YYYY-MM-DD".
     * @param dispatchTime   The new dispatch time to be set. Format should be "HH:MM" assuming a 24-hour clock.
     * @param dispatcherName The name of the person or entity dispatching the manifest.
     */
    public static void updateManifest(String batchNumber,
                                      String dispatchDate,
                                      String dispatchTime,
                                      String dispatcherName) {


        String sql = "UPDATE " + Constants.TABLES.LAB_MANIFESTS +
                "       SET dispatch_date = '" + dispatchDate + "'," +
                "           dispatch_time = '" + dispatchTime + "'," +
                "           dispatcher_name = '" + dispatcherName + "'" +
                "       WHERE batch_number = '" + batchNumber + "'";
        updateDB(sql);
    }

    /**
     * Retrieves a list of test sample requests from the database filtered by the specified sample type. This method
     * queries the {@code LAB_TEST_REQUESTS} table to find all test samples that match the given sample type and have
     * been processed. Each matching record is converted into a {@code TestSample} object, which is then added to the list
     * returned by this method.
     *
     * @param sampleType The type of sample to filter requests by. Only test samples with this specified type and
     *                   marked as processed will be returned.
     * @return A {A list of TestSample} objects representing the test samples requests that match the specified
     * sample type and have been processed. Returns an empty list if no matches are found.
     */
    public static List<TestSample> getTestSamplesRequestsBySampleType(String sampleType) {
        String sql = "SELECT * FROM " + Constants.TABLES.LAB_TEST_REQUESTS + " ts " + "WHERE sample_type = '" + sampleType + "' AND sample_processed = 'true'";


        return readData(sql, dataMap);
    }

    /**
     * Fetches test sample requests from the database that match a specific sample ID. This method looks up
     * the {@code LAB_TEST_REQUESTS} table to find all entries where the sample ID matches the provided parameter.
     * Each found entry is transformed into a {@code TestSample} object. This is particularly useful for retrieving
     * detailed information about a specific test sample, including patient details, sample processing status,
     * and collection dates among others.
     *
     * @param sampleId The unique identifier of the sample to retrieve requests for. This matches against the
     *                 {@code sample_id} column in the {@code LAB_TEST_REQUESTS} table.
     * @return A list of {@link TestSample} objects representing the test sample requests that match the specified
     * sample ID. It returns an empty list if no such sample is found.
     */
    public static List<TestSample> getTestSamplesRequestsBySampleId(String sampleId) {
        String sql = "SELECT * FROM " + Constants.TABLES.LAB_TEST_REQUESTS + " ts " + "WHERE sample_id = '" + sampleId + "'";

        return readData(sql, dataMap);
    }


    /**
     * Retrieves a manifest record from the database based on the provided batch number. This method queries
     * the {@code LAB_MANIFESTS} table to find a manifest with a matching batch number. If found, the method
     * constructs a {@code Manifest} object with the retrieved data, including manifest details like
     * manifest type, destination hub information, dispatch dates, and the list of samples associated with it.
     * <p>
     * This method is particularly useful for operations requiring detailed information about a specific manifest,
     * such as tracking, updating, or displaying manifest details within an application.
     *
     * @param batchNumber The unique batch number of the manifest to retrieve. This is used to identify
     *                    the specific manifest record in the {@code LAB_MANIFESTS} table.
     * @return A {@link Manifest} object containing the details of the corresponding manifest. If no manifest
     * with the provided batch number is found, this method returns {@code null}.
     */
    public static Manifest getManifestByBatchNumber(String batchNumber) {
        String sql = "SELECT * FROM " + Constants.TABLES.LAB_MANIFESTS + " ts " + "WHERE batch_number = '" + batchNumber + "'";

        List<Manifest> res = readData(sql, manifestDataMap);
        if (res != null && !res.isEmpty())
            return res.get(0);

        return null;
    }


    public static Manifest getManifestByTestSampleId(String testSampleId) {
        String sql = "SELECT * FROM " + Constants.TABLES.LAB_MANIFESTS + " ts " + "WHERE samples_list LIKE '%" + testSampleId + "%'";
        List<Manifest> res = readData(sql, manifestDataMap);
        if (res != null && !res.isEmpty())
            return res.get(0);
        return null;
    }

    public static List<String> getTestSamplesRequestsByBatchNumber(String batchNumber) {
        String sql = "SELECT sample_id FROM " + Constants.TABLES.LAB_MANIFESTS + " ts " + "WHERE batch_number = '" + batchNumber + "' ";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "sample_id", "");

        return readData(sql, dataMap);
    }

    public static String getDispatchDate(String batchNumber) {
        String sql = "SELECT dispatch_date FROM " + Constants.TABLES.LAB_MANIFESTS + " ts " + "WHERE batch_number = '" + batchNumber + "' ";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "dispatch_date", "");

        return readData(sql, dataMap).get(0);
    }

    public static boolean isSampleProcessed(String sampleId) {
        String sql = "SELECT sample_processed FROM " + Constants.TABLES.LAB_TEST_REQUESTS + " p WHERE p.sample_id = '" + sampleId + "'";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "sample_processed");
        List<String> res = readData(sql, dataMap);

        if (res != null && !res.isEmpty() && res.get(0) != null) {
            return res.get(0).equalsIgnoreCase("true");
        }
        return false;
    }

    public static List<TestSample> getTestSamplesRequestsWithNoResultsBySampleTypeAndPatientId(String sampleType, String patientId) {
        String sql = "SELECT * FROM " + Constants.TABLES.LAB_TEST_REQUESTS + " ts " + "WHERE sample_type = '" + sampleType + "' AND patient_id = '" + patientId + "' AND results IS NULL ";
        return readData(sql, dataMap);
    }

    public static List<TestSample> getTestSamplesRequestsBySampleTypeAndPatientId(String sampleType, String patientId) {
        String sql = "SELECT * FROM " + Constants.TABLES.LAB_TEST_REQUESTS + " ts " + "WHERE sample_type = '" + sampleType + "' AND patient_id = '" + patientId + "' ";
        return readData(sql, dataMap);
    }


    public static List<TestSample> getTestSamplesRequestsNotInManifests(String sampleType) {
        String sql = "SELECT * FROM " + Constants.TABLES.LAB_TEST_REQUESTS + " ts " + "WHERE sample_type = '" + sampleType + "' AND sample_processed = 'true' AND " +
                "printf(\"%.0f\", sample_id) NOT IN ( " +
                "    SELECT printf(\"%.0f\", value) " +
                "    FROM " + Constants.TABLES.LAB_MANIFESTS + ", json_each(" + Constants.TABLES.LAB_MANIFESTS + ".samples_list) " +
                ")";

        return readData(sql, dataMap);
    }


    public static boolean isSampleUploaded(String sampleId) {
        String sql = "SELECT * FROM " + Constants.TABLES.LAB_TEST_REQUESTS + " ts " + "WHERE sample_id = '" + sampleId + "' AND sample_processed = 'true' AND " +
                "printf(\"%.0f\", sample_id) IN ( " +
                "    SELECT printf(\"%.0f\", value) " +
                "    FROM " + Constants.TABLES.LAB_MANIFESTS + ", json_each(" + Constants.TABLES.LAB_MANIFESTS + ".samples_list) WHERE ec_lab_manifests.dispatch_date IS NOT NULL" +
                ")";

        List<TestSample> testSamples = readData(sql, dataMap);
        return testSamples != null && !testSamples.isEmpty();
    }
}
