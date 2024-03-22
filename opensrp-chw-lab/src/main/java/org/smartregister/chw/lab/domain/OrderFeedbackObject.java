package org.smartregister.chw.lab.domain;

public class OrderFeedbackObject {

    private String condomType;
    private String condomBrand;
    private String responseStatus;
    private String responseQuantity;
    private String responseDate;

    public String getCondomType() {
        return condomType;
    }

    public void setCondomType(String condomType) {
        this.condomType = condomType;
    }

    public String getCondomBrand() {
        return condomBrand;
    }

    public void setCondomBrand(String condomBrand) {
        this.condomBrand = condomBrand;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponseQuantity() {
        return responseQuantity;
    }

    public void setResponseQuantity(String responseQuantity) {
        this.responseQuantity = responseQuantity;
    }

    public String getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(String responseDate) {
        this.responseDate = responseDate;
    }

}
