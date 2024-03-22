package org.smartregister.chw.lab.util;

import org.smartregister.chw.lab.LabLibrary;
import org.smartregister.chw.lab.domain.Visit;
import org.smartregister.chw.lab.domain.VisitDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisitUtils {
    public static List<Visit> getVisits(String memberID) {

        List<Visit> visits = getVisitsOnly(memberID, Constants.EVENT_TYPE.LAB_TEST_REQUEST_REGISTRATION);
        for (Visit visit : visits) {
            List<VisitDetail> detailList = getVisitDetailsOnly(visit.getVisitId());
            visit.setVisitDetails(getVisitGroups(detailList));
        }

        return visits;
    }

    public static List<Visit> getVisitsOnly(String memberID, String visitName) {
        return new ArrayList<>(LabLibrary.getInstance().visitRepository().getVisits(memberID, visitName, "ASC"));
    }

    public static List<VisitDetail> getVisitDetailsOnly(String visitID) {
        return LabLibrary.getInstance().visitDetailsRepository().getVisits(visitID);
    }

    public static Map<String, List<VisitDetail>> getVisitGroups(List<VisitDetail> detailList) {
        Map<String, List<VisitDetail>> visitMap = new HashMap<>();

        for (VisitDetail visitDetail : detailList) {

            List<VisitDetail> visitDetailList = visitMap.get(visitDetail.getVisitKey());
            if (visitDetailList == null)
                visitDetailList = new ArrayList<>();

            visitDetailList.add(visitDetail);

            visitMap.put(visitDetail.getVisitKey(), visitDetailList);
        }
        return visitMap;
    }
}
