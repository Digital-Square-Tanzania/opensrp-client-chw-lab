package org.smartregister.chw.cdp.dao;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.dao.AbstractDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
 * Handles all Stocking And Restocking issues
 * Created by dev-billy 29/08/2022
 * */
public class CdpStockingDao extends AbstractDao {
    private static final String stockLogTable = Constants.TABLES.CDP_STOCK_LOG;
    private static final String stockCountTable = Constants.TABLES.CDP_STOCK_COUNT;

    public static void updateStockLogData(String locationId, String formSubmissionId, String chwName, String maleCondomBrand, String femaleCondomBrand, String maleCondomsOffset, String femaleCondomsOffset, String stockEventType, String issuingOrganization, String eventType, String restockingDate) {


        String sqlUpdateStockLog = "INSERT INTO " + stockLogTable + "" + "    (id, entity_id, base_entity_id, chw_name, male_condom_brand, female_condom_brand, female_condoms_offset, male_condoms_offset, event_type, issuing_organization, stock_event_type, date_updated) " +
                "         VALUES ('" + formSubmissionId + "', '" + locationId + "', '" + formSubmissionId + "', '" + chwName + "','" + maleCondomBrand + "' ,'"+ femaleCondomBrand + "' ,'" + femaleCondomsOffset + "', '" + maleCondomsOffset + "', '" + eventType + "', '" + issuingOrganization + "', '" + stockEventType + "', '" + restockingDate + "')" +
                "       ON CONFLICT (id) DO UPDATE" +
                "       SET entity_id = '" + locationId + "'," +
                "           chw_name = '" + chwName + "', " +
                "           male_condom_brand = '" + maleCondomBrand + "', " +
                "           female_condom_brand = '" + femaleCondomBrand + "', " +
                "           female_condoms_offset = '" + femaleCondomsOffset + "', " +
                "           male_condoms_offset = '" + maleCondomsOffset + "', " +
                "           stock_event_type = '" + stockEventType + "', " +
                "           event_type = '" + eventType + "', " +
                "           issuing_organization = '" + issuingOrganization + "', " +
                "           date_updated = '" + restockingDate + "'" + "       ";
        updateDB(sqlUpdateStockLog);
    }

    public static void updateStockCountData(String locationId,
                                            String formSubmissionId,
                                            String chwName,
                                            String maleCondomsOffset,
                                            String femaleCondomsOffset,
                                            String stockEventType,
                                            String restockingDate) {

        /*
         * Needed to prevent recount when the client processor re runs the processing of the event
         * */
        if (!wasRecordProcessed(formSubmissionId, locationId)) {
            int femaleCondomsCount = 0;
            int maleCondomsCount = 0;

            Integer currentFemaleCondomCount = getCurrentFemaleCondomCount(locationId);
            Integer currentMaleCondomCount = getCurrentMaleCondomCount(locationId);

            String processedFormSubmissionId = getCurrentFormSubmissionIdInStockCount(locationId);

            if (StringUtils.isBlank(processedFormSubmissionId)) {
                processedFormSubmissionId = formSubmissionId;
            } else {
                processedFormSubmissionId += ", " + formSubmissionId;
            }

            if (stockEventType.equalsIgnoreCase(Constants.STOCK_EVENT_TYPES.DECREMENT)) {
                femaleCondomsCount = currentFemaleCondomCount != null ? currentFemaleCondomCount - Integer.parseInt(femaleCondomsOffset) : Integer.parseInt(femaleCondomsOffset);
                maleCondomsCount = currentMaleCondomCount != null ? currentMaleCondomCount - Integer.parseInt(maleCondomsOffset) : Integer.parseInt(maleCondomsOffset);
            } else if (stockEventType.equalsIgnoreCase(Constants.STOCK_EVENT_TYPES.INCREMENT)) {
                femaleCondomsCount = currentFemaleCondomCount != null ? currentFemaleCondomCount + Integer.parseInt(femaleCondomsOffset) : Integer.parseInt(femaleCondomsOffset);
                maleCondomsCount = currentMaleCondomCount != null ? currentMaleCondomCount + Integer.parseInt(maleCondomsOffset) : Integer.parseInt(maleCondomsOffset);
            }

            String sqlUpdateStockCount = "INSERT INTO " + stockCountTable + "" +
                    "    (id, base_entity_id, chw_name, form_submission_id, female_condoms_count, male_condoms_count, date_updated, processed_form_submission_ids) " +
                    "         VALUES ('" + locationId + "', '" + locationId + "', '" + chwName + "', '" + formSubmissionId + "', '" + femaleCondomsCount + "', '" + maleCondomsCount + "', '" + restockingDate + "', '" + processedFormSubmissionId + "')" +
                    "       ON CONFLICT (id) DO UPDATE" +
                    "          SET  chw_name = '" + chwName + "', " +
                    "               form_submission_id = '" + formSubmissionId + "', " +
                    "               female_condoms_count = '" + femaleCondomsCount + "', " +
                    "               male_condoms_count = '" + maleCondomsCount + "', " +
                    "               date_updated = '" + restockingDate + "', " +
                    "               processed_form_submission_ids = '" + processedFormSubmissionId + "'" +
                    "       ";
            updateDB(sqlUpdateStockCount);
        }
    }

    public static void updateOutletStockCountData(String baseEntityId, String formSubmissionId, String maleCondomsOffset, String femaleCondomsOffset, String stockEventType, String restockingDate) {
        int femaleCondomsCount = 0;
        int maleCondomsCount = 0;

        Integer currentFemaleCondomCount = getCurrentOutletFemaleCondomCount(baseEntityId);
        Integer currentMaleCondomCount = getCurrentOutletMaleCondomCount(baseEntityId);

        String processedFormSubmissionId = getCurrentFormSubmissionIdInOutletStockCount(baseEntityId);

        if (StringUtils.isBlank(processedFormSubmissionId)) {
            processedFormSubmissionId = formSubmissionId;
        } else {
            processedFormSubmissionId += ", " + formSubmissionId;
        }

        if (stockEventType.equalsIgnoreCase(Constants.STOCK_EVENT_TYPES.DECREMENT)) {
            femaleCondomsCount = currentFemaleCondomCount != null ? currentFemaleCondomCount - Integer.parseInt(femaleCondomsOffset) : Integer.parseInt(femaleCondomsOffset);
            maleCondomsCount = currentMaleCondomCount != null ? currentMaleCondomCount - Integer.parseInt(maleCondomsOffset) : Integer.parseInt(maleCondomsOffset);
        } else if (stockEventType.equalsIgnoreCase(Constants.STOCK_EVENT_TYPES.INCREMENT)) {
            femaleCondomsCount = currentFemaleCondomCount != null ? currentFemaleCondomCount + Integer.parseInt(femaleCondomsOffset) : Integer.parseInt(femaleCondomsOffset);
            maleCondomsCount = currentMaleCondomCount != null ? currentMaleCondomCount + Integer.parseInt(maleCondomsOffset) : Integer.parseInt(maleCondomsOffset);
        }

        String sqlUpdateStockCount = "INSERT INTO " + Constants.TABLES.CDP_OUTLET_STOCK_COUNT + "" + "    (id, base_entity_id, form_submission_id, female_condoms_count, male_condoms_count, last_interacted_with, processed_form_submission_ids) " + "         VALUES ('" + baseEntityId + "', '" + baseEntityId + "', '" + formSubmissionId + "', '" + femaleCondomsCount + "', '" + maleCondomsCount + "', '" + restockingDate + "', '" + processedFormSubmissionId + "')" + "       ON CONFLICT (id) DO UPDATE" + "          SET  form_submission_id = '" + formSubmissionId + "', " + "               female_condoms_count = '" + femaleCondomsCount + "', " + "               male_condoms_count = '" + maleCondomsCount + "', " + "               last_interacted_with = '" + restockingDate + "', " + "               processed_form_submission_ids = '" + processedFormSubmissionId + "'" + "       ";
        updateDB(sqlUpdateStockCount);
    }

    private static boolean wasRecordProcessed(String formSubmissionId, String locationId) {
        String currentFormSubmissionIds = getCurrentFormSubmissionIdInStockCount(locationId);
        return currentFormSubmissionIds.contains(formSubmissionId);
    }

    private static String getCurrentFormSubmissionIdInStockCount(String locationId) {
        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "processed_form_submission_ids");
        String sql = "SELECT processed_form_submission_ids FROM " + stockCountTable + " WHERE base_entity_id = '" + locationId + "' ";
        List<String> res = readData(sql, dataMap);
        if (res != null && res.size() > 0 && res.get(0) != null) {
            return res.get(0);
        }
        return "";
    }

    private static String getCurrentFormSubmissionIdInOutletStockCount(String baseEntityId) {
        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "processed_form_submission_ids");
        String sql = "SELECT processed_form_submission_ids FROM " + Constants.TABLES.CDP_OUTLET_STOCK_COUNT + " WHERE base_entity_id = '" + baseEntityId + "' ";
        List<String> res = readData(sql, dataMap);
        if (res != null && res.size() > 0 && res.get(0) != null) {
            return res.get(0);
        }
        return "";
    }

    public static Integer getCurrentMaleCondomCount(String locationId) {
        DataMap<Integer> dataMap = cursor -> getCursorIntValue(cursor, "male_condoms_count");
        String sql = "SELECT male_condoms_count FROM " + stockCountTable + " WHERE base_entity_id = '" + locationId + "' ";

        List<Integer> res = readData(sql, dataMap);

        if (res == null || res.size() == 0) return 0;
        return res.get(0);
    }

    public static Integer getCurrentFemaleCondomCount(String locationId) {
        DataMap<Integer> dataMap = cursor -> getCursorIntValue(cursor, "female_condoms_count");
        String sql = "SELECT female_condoms_count FROM " + stockCountTable + " WHERE base_entity_id = '" + locationId + "' ";

        List<Integer> res = readData(sql, dataMap);

        if (res == null || res.size() == 0) return 0;
        return res.get(0);
    }

    public static Integer getCurrentOutletMaleCondomCount(String baseEntityId) {
        DataMap<Integer> dataMap = cursor -> getCursorIntValue(cursor, "male_condoms_count");
        String sql = "SELECT male_condoms_count FROM " + Constants.TABLES.CDP_OUTLET_STOCK_COUNT + " WHERE base_entity_id = '" + baseEntityId + "' ";

        List<Integer> res = readData(sql, dataMap);

        if (res == null || res.size() == 0) return 0;
        return res.get(0);
    }

    public static Integer getCurrentOutletFemaleCondomCount(String baseEntityId) {
        DataMap<Integer> dataMap = cursor -> getCursorIntValue(cursor, "female_condoms_count");
        String sql = "SELECT female_condoms_count FROM " + Constants.TABLES.CDP_OUTLET_STOCK_COUNT + " WHERE base_entity_id = '" + baseEntityId + "' ";

        List<Integer> res = readData(sql, dataMap);

        if (res == null || res.size() == 0) return 0;
        return res.get(0);
    }


    public static List<String> getCondomBrands() {
        String sql = "SELECT DISTINCT condom_brand FROM (\n" +
                "   SELECT  DISTINCT male_condom_brand COLLATE NOCASE as condom_brand  FROM \n" +
                "       "+stockLogTable+" \n" +
                "   WHERE (male_condom_brand <> '' AND male_condom_brand IS NOT NULL)\n" +
                "   UNION \n" +
                "   SELECT  DISTINCT female_condom_brand COLLATE NOCASE as condom_brand  FROM \n" +
                "       "+stockLogTable+" \n" +
                "   WHERE (female_condom_brand <> '' AND female_condom_brand IS NOT NULL)\n" +
                ") ";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "condom_brand");

        List<String> res = readData(sql, dataMap);

        if (res == null || res.size() == 0) return new ArrayList<>();
        return res;
    }


    public static Integer getCurrentCondomCountByBrand(String condomBrand, CondomStockLog.CondomType condomType) {

        String sql= null;
        if(condomType.equals(CondomStockLog.CondomType.MALE)){
            sql = "SELECT *, male_condom_brand as condom_brand FROM " + stockLogTable + " WHERE male_condom_brand = '" + condomBrand + "' ";
        }else{
            sql = "SELECT  *, female_condom_brand as condom_brand  FROM " + stockLogTable + " WHERE female_condom_brand = '" + condomBrand + "' ";
        }


        AbstractDao.DataMap<CondomStockLog> dataMap = cursor -> {
            CondomStockLog condomStockLog = new CondomStockLog();

            condomStockLog.setCondomBrand(getCursorValue(cursor, "condom_brand", ""));
            condomStockLog.setStockEventType(getCursorValue(cursor, "stock_event_type", ""));
            condomStockLog.setCount(getCursorIntValue(cursor, condomType.name().toLowerCase(Locale.getDefault()) + "_condoms_offset", 0));

            return condomStockLog;
        };


        List<CondomStockLog> res = readData(sql, dataMap);

        if (res == null || res.size() == 0) return 0;
        else {
            int count = 0;
            for (CondomStockLog condomStockLog : res) {
                if (condomStockLog.getStockEventType().equalsIgnoreCase(Constants.STOCK_EVENT_TYPES.INCREMENT))
                    count += condomStockLog.getCount();
                else if (condomStockLog.getStockEventType().equalsIgnoreCase(Constants.STOCK_EVENT_TYPES.DECREMENT))
                    count -= condomStockLog.getCount();
            }
            return count;
        }
    }


    public static int getCurrentStockInHand(String locationId) {
        int currentStockInHand = 0;
        Integer currentFemaleCondomCount = getCurrentFemaleCondomCount(locationId);
        Integer currentMaleCondomCount = getCurrentMaleCondomCount(locationId);
        if (currentFemaleCondomCount != null) {
            currentStockInHand += currentFemaleCondomCount;
        }
        if (currentMaleCondomCount != null) {
            currentStockInHand += currentMaleCondomCount;
        }
        return currentStockInHand;
    }

    public static class CondomStockLog {
        private String condomBrand;
        private String stockEventType;
        private int count;

        public String getCondomBrand() {
            return condomBrand;
        }

        public void setCondomBrand(String condomBrand) {
            this.condomBrand = condomBrand;
        }

        public String getStockEventType() {
            return stockEventType;
        }

        public void setStockEventType(String stockEventType) {
            this.stockEventType = stockEventType;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public enum CondomType {
            MALE, FEMALE
        }
    }
}
