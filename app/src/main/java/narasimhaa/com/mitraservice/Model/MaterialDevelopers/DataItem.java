package narasimhaa.com.mitraservice.Model.MaterialDevelopers;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("PRICE")
	private String pRICE;

	@SerializedName("BUSINESS_NAME")
	private String bUSINESSNAME;

	@SerializedName("UPDATED")
	private Object uPDATED;

	@SerializedName("MRP")
	private String mRP;

	@SerializedName("STEEL_SHAPE")
	private String hEIGHT;

	@SerializedName("STATUS")
	private String sTATUS;

	@SerializedName("CREATED")
	private String cREATED;

	@SerializedName("DESCRIPTION")
	private String dESCRIPTION;

	@SerializedName("STEEL_SUB_CATEGORY")
	private String bUSINESSTYPE;

	@SerializedName("BRAND_NAME")
	private String bRANDNAME;

	@SerializedName("EMAIL_ID")
	private String eMAILID;

	@SerializedName("ID")
	private String iD;

	@SerializedName("SERVICE_TYPE")
	private String sERVICETYPE;

	@SerializedName("DOOR_DELIVERY")
	private String dOORDELIVERY;

	@SerializedName("WEIGHT")
	private String wEIGHT;

	@SerializedName("LENGTH")
	private String LENGTH;
	@SerializedName("THICKNESS")
	private String THICKNESS;

	@SerializedName("PERIMETER")
	private String PERIMETER;



	private boolean isSelected;

	private int quantity = 1;
	public String getPRICE(){
		return pRICE;
	}

	public String getBUSINESSNAME(){
		return bUSINESSNAME;
	}

	public Object getUPDATED(){
		return uPDATED;
	}

	public String getMRP(){
		return mRP;
	}

	public String getHEIGHT(){
		return hEIGHT;
	}

	public String getSTATUS(){
		return sTATUS;
	}

	public String getCREATED(){
		return cREATED;
	}

	public String getDESCRIPTION(){
		return dESCRIPTION;
	}

	public String getBUSINESSTYPE(){
		return bUSINESSTYPE;
	}

	public String getBRANDNAME(){
		return bRANDNAME;
	}

	public String getEMAILID(){
		return eMAILID;
	}

	public String getID(){
		return iD;
	}

	public String getSERVICETYPE(){
		return sERVICETYPE;
	}

	public String getDOORDELIVERY(){
		return dOORDELIVERY;
	}

	public String getWEIGHT(){
		return wEIGHT;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getwEIGHT() {
		return wEIGHT;
	}

	public void setwEIGHT(String wEIGHT) {
		this.wEIGHT = wEIGHT;
	}

	public String getLENGTH() {
		return LENGTH;
	}

	public void setLENGTH(String LENGTH) {
		this.LENGTH = LENGTH;
	}

	public String getTHICKNESS() {
		return THICKNESS;
	}

	public void setTHICKNESS(String THICKNESS) {
		this.THICKNESS = THICKNESS;
	}

	public String getPERIMETER() {
		return PERIMETER;
	}

	public void setPERIMETER(String PERIMETER) {
		this.PERIMETER = PERIMETER;
	}
}