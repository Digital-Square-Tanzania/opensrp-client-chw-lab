package org.smartregister.chw.lab.domain;

public class Manifest {
    private String batchNumber;
    private String manifestType;
    private String destinationHubName;
    private String destinationHubUuid;
    private String dispatchDate;
    private String dispatchTime;
    private String dispatcherName;
    private String sampleList;

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getManifestType() {
        return manifestType;
    }

    public void setManifestType(String manifestType) {
        this.manifestType = manifestType;
    }

    public String getDestinationHubName() {
        return destinationHubName;
    }

    public void setDestinationHubName(String destinationHubName) {
        this.destinationHubName = destinationHubName;
    }

    public String getDestinationHubUuid() {
        return destinationHubUuid;
    }

    public void setDestinationHubUuid(String destinationHubUuid) {
        this.destinationHubUuid = destinationHubUuid;
    }

    public String getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(String dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public String getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(String dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public String getDispatcherName() {
        return dispatcherName;
    }

    public void setDispatcherName(String dispatcherName) {
        this.dispatcherName = dispatcherName;
    }

    public String getSampleList() {
        return sampleList;
    }

    public void setSampleList(String sampleList) {
        this.sampleList = sampleList;
    }
}
