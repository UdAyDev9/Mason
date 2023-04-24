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

	@SerializedName("STEEL_SIZE")
	private String wEIGHT;

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
}