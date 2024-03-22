package org.smartregister.chw.lab.domain;

import java.io.Serializable;

public class OutletObject implements Serializable {

    private String outletName;
    private String outletType;
    private String otherOutletType;
    private String outletWardName;
    private String outletVillageStreetName;
    private String outletId;
    private String focalPersonName;
    private String focalPersonNumber;
    private String baseEntityId;

    private boolean isClosed;

    public OutletObject() {
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getOutletType() {
        return outletType;
    }

    public void setOutletType(String outletType) {
        this.outletType = outletType;
    }

    public String getOutletWardName() {
        return outletWardName;
    }

    public void setOutletWardName(String outletWardName) {
        this.outletWardName = outletWardName;
    }

    public String getOutletVillageStreetName() {
        return outletVillageStreetName;
    }

    public void setOutletVillageStreetName(String outletVillageStreetName) {
        this.outletVillageStreetName = outletVillageStreetName;
    }

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public String getOtherOutletType() {
        return otherOutletType;
    }

    public void setOtherOutletType(String otherOutletType) {
        this.otherOutletType = otherOutletType;
    }

    public String getFocalPersonName() {
        return focalPersonName;
    }

    public void setFocalPersonName(String focalPersonName) {
        this.focalPersonName = focalPersonName;
    }

    public String getFocalPersonNumber() {
        return focalPersonNumber;
    }

    public void setFocalPersonNumber(String focalPersonNumber) {
        this.focalPersonNumber = focalPersonNumber;
    }

    public String getBaseEntityId() {
        return baseEntityId;
    }

    public void setBaseEntityId(String baseEntityId) {
        this.baseEntityId = baseEntityId;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }
}
