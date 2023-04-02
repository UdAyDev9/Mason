package narasimhaa.com.mitraservice.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContentProfile {

    @SerializedName("BOD_SEQ_NO")
    @Expose
    private String bODSEQNO;
    @SerializedName("NAME")
    @Expose
    private String nAME;
    @SerializedName("MOBILE_NO")
    @Expose
    private String mOBILENO;
    @SerializedName("EMAIL_ID")
    @Expose
    private String eMAILID;
    @SerializedName("ADDRESS")
    @Expose
    private String aDDRESS;
    @SerializedName("CITY")
    @Expose
    private String cITY;
    @SerializedName("STATE")
    @Expose
    private String sTATE;
    @SerializedName("DISTRICT")
    @Expose
    private String dISTRICT;
    @SerializedName("PINCODE_NO")
    @Expose
    private String pINCODENO;
    @SerializedName("REGISTRATION_DATE")
    @Expose
    private String rEGISTRATIONDATE;
    @SerializedName("VERIFIED")
    @Expose
    private String vERIFIED;
    @SerializedName("STATUS")
    @Expose
    private String sTATUS;
    @SerializedName("USER_TYPE")
    @Expose
    private String uSERTYPE;

    @SerializedName("SERVICE_NAME")
    @Expose
    private String SERVICE_NAME;

    @SerializedName("EXPERIENCE")
    @Expose
    private String EXPERIENCE;

    @SerializedName("WITH_IN_RANGE")
    @Expose
    private String WITH_IN_RANGE;

    @SerializedName("QUALIFICATION")
    @Expose
    private String QUALIFICATION;

    @SerializedName(value = "SER_PER_SEQ_ID", alternate = {"ID"})
    @Expose
    private String SER_PER_SEQ_ID;


    public String getBODSEQNO() {
        return bODSEQNO;
    }

    public void setBODSEQNO(String bODSEQNO) {
        this.bODSEQNO = bODSEQNO;
    }

    public String getNAME() {
        return nAME;
    }

    public void setNAME(String nAME) {
        this.nAME = nAME;
    }

    public String getMOBILENO() {
        return mOBILENO;
    }

    public void setMOBILENO(String mOBILENO) {
        this.mOBILENO = mOBILENO;
    }

    public String getEMAILID() {
        return eMAILID;
    }

    public void setEMAILID(String eMAILID) {
        this.eMAILID = eMAILID;
    }

    public String getADDRESS() {
        return aDDRESS;
    }

    public void setADDRESS(String aDDRESS) {
        this.aDDRESS = aDDRESS;
    }

    public String getCITY() {
        return cITY;
    }

    public void setCITY(String cITY) {
        this.cITY = cITY;
    }

    public String getSTATE() {
        return sTATE;
    }

    public void setSTATE(String sTATE) {
        this.sTATE = sTATE;
    }

    public String getDISTRICT() {
        return dISTRICT;
    }

    public void setDISTRICT(String dISTRICT) {
        this.dISTRICT = dISTRICT;
    }

    public String getPINCODENO() {
        return pINCODENO;
    }

    public void setPINCODENO(String pINCODENO) {
        this.pINCODENO = pINCODENO;
    }

    public String getREGISTRATIONDATE() {
        return rEGISTRATIONDATE;
    }

    public void setREGISTRATIONDATE(String rEGISTRATIONDATE) {
        this.rEGISTRATIONDATE = rEGISTRATIONDATE;
    }

    public String getVERIFIED() {
        return vERIFIED;
    }

    public void setVERIFIED(String vERIFIED) {
        this.vERIFIED = vERIFIED;
    }

    public String getSTATUS() {
        return sTATUS;
    }

    public void setSTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }

    public String getUSERTYPE() {
        return uSERTYPE;
    }

    public void setUSERTYPE(String uSERTYPE) {
        this.uSERTYPE = uSERTYPE;
    }

    public String getSERVICE_NAME() {
        return SERVICE_NAME;
    }

    public void setSERVICE_NAME(String SERVICE_NAME) {
        this.SERVICE_NAME = SERVICE_NAME;
    }

    public String getEXPERIENCE() {
        return EXPERIENCE;
    }

    public void setEXPERIENCE(String EXPERIENCE) {
        this.EXPERIENCE = EXPERIENCE;
    }

    public String getWITH_IN_RANGE() {
        return WITH_IN_RANGE;
    }

    public void setWITH_IN_RANGE(String WITH_IN_RANGE) {
        this.WITH_IN_RANGE = WITH_IN_RANGE;
    }

    public String getQUALIFICATION() {
        return QUALIFICATION;
    }

    public void setQUALIFICATION(String QUALIFICATION) {
        this.QUALIFICATION = QUALIFICATION;
    }

    public String getSER_PER_SEQ_ID() {
        return SER_PER_SEQ_ID;
    }

    public void setSER_PER_SEQ_ID(String SER_PER_SEQ_ID) {
        this.SER_PER_SEQ_ID = SER_PER_SEQ_ID;
    }
}
