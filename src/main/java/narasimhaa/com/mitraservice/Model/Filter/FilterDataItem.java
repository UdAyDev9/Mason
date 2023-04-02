package narasimhaa.com.mitraservice.Model.Filter;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class FilterDataItem {

	@SerializedName("CONTACT_NO")
	private String cONTACTNO;

	@SerializedName("SERVICE_NAME")
	private String sERVICENAME;

	@SerializedName("CERTIFICATE")
	private String cERTIFICATE;

	@SerializedName("REGISTERED_BY")
	private String rEGISTEREDBY;

	@SerializedName("STATE")
	private String sTATE;

	@SerializedName("UPDATE_DATETIME")
	private Object uPDATEDATETIME;

	@SerializedName("PINCODE")
	private String pINCODE;

	@SerializedName("REGISTERED_DATETIME")
	private String rEGISTEREDDATETIME;

	@SerializedName("NAME")
	private String nAME;

	@SerializedName("DISTRICT")
	private String dISTRICT;

	@SerializedName("STATUS")
	private String sTATUS;

	@SerializedName("QUALIFICATION")
	private String qUALIFICATION;

	@SerializedName("SER_PER_SEQ_ID")
	private String sERPERSEQID;

	@SerializedName("CITY")
	private String cITY;

	@SerializedName("AVAILABLE_STATUS")
	private String aVAILABLESTATUS;

	@SerializedName("IMAGE_PATHS")
	private List<IMAGEPATHSItem> iMAGEPATHS;

	@SerializedName("EMAIL_ID")
	private String eMAILID;

	@SerializedName("EXPERIENCE")
	private String eXPERIENCE;

	@SerializedName("WITH_IN_RANGE")
	private String wITHINRANGE;

	public void setCONTACTNO(String cONTACTNO){
		this.cONTACTNO = cONTACTNO;
	}

	public String getCONTACTNO(){
		return cONTACTNO;
	}

	public void setSERVICENAME(String sERVICENAME){
		this.sERVICENAME = sERVICENAME;
	}

	public String getSERVICENAME(){
		return sERVICENAME;
	}

	public void setCERTIFICATE(String cERTIFICATE){
		this.cERTIFICATE = cERTIFICATE;
	}

	public String getCERTIFICATE(){
		return cERTIFICATE;
	}

	public void setREGISTEREDBY(String rEGISTEREDBY){
		this.rEGISTEREDBY = rEGISTEREDBY;
	}

	public String getREGISTEREDBY(){
		return rEGISTEREDBY;
	}

	public void setSTATE(String sTATE){
		this.sTATE = sTATE;
	}

	public String getSTATE(){
		return sTATE;
	}

	public void setUPDATEDATETIME(Object uPDATEDATETIME){
		this.uPDATEDATETIME = uPDATEDATETIME;
	}

	public Object getUPDATEDATETIME(){
		return uPDATEDATETIME;
	}

	public void setPINCODE(String pINCODE){
		this.pINCODE = pINCODE;
	}

	public String getPINCODE(){
		return pINCODE;
	}

	public void setREGISTEREDDATETIME(String rEGISTEREDDATETIME){
		this.rEGISTEREDDATETIME = rEGISTEREDDATETIME;
	}

	public String getREGISTEREDDATETIME(){
		return rEGISTEREDDATETIME;
	}

	public void setNAME(String nAME){
		this.nAME = nAME;
	}

	public String getNAME(){
		return nAME;
	}

	public void setDISTRICT(String dISTRICT){
		this.dISTRICT = dISTRICT;
	}

	public String getDISTRICT(){
		return dISTRICT;
	}

	public void setSTATUS(String sTATUS){
		this.sTATUS = sTATUS;
	}

	public String getSTATUS(){
		return sTATUS;
	}

	public void setQUALIFICATION(String qUALIFICATION){
		this.qUALIFICATION = qUALIFICATION;
	}

	public String getQUALIFICATION(){
		return qUALIFICATION;
	}

	public void setSERPERSEQID(String sERPERSEQID){
		this.sERPERSEQID = sERPERSEQID;
	}

	public String getSERPERSEQID(){
		return sERPERSEQID;
	}

	public void setCITY(String cITY){
		this.cITY = cITY;
	}

	public String getCITY(){
		return cITY;
	}

	public void setAVAILABLESTATUS(String aVAILABLESTATUS){
		this.aVAILABLESTATUS = aVAILABLESTATUS;
	}

	public String getAVAILABLESTATUS(){
		return aVAILABLESTATUS;
	}

	public void setIMAGEPATHS(List<IMAGEPATHSItem> iMAGEPATHS){
		this.iMAGEPATHS = iMAGEPATHS;
	}

	public List<IMAGEPATHSItem> getIMAGEPATHS(){
		return iMAGEPATHS;
	}

	public void setEMAILID(String eMAILID){
		this.eMAILID = eMAILID;
	}

	public String getEMAILID(){
		return eMAILID;
	}

	public void setEXPERIENCE(String eXPERIENCE){
		this.eXPERIENCE = eXPERIENCE;
	}

	public String getEXPERIENCE(){
		return eXPERIENCE;
	}

	public void setWITHINRANGE(String wITHINRANGE){
		this.wITHINRANGE = wITHINRANGE;
	}

	public String getWITHINRANGE(){
		return wITHINRANGE;
	}
}