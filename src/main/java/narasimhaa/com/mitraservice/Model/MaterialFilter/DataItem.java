package narasimhaa.com.mitraservice.Model.MaterialFilter;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("PRICE")
	private String pRICE;

	@SerializedName("BUSINESS_NAME")
	private String bUSINESSNAME;

	@SerializedName("UPDATED")
	private String uPDATED;

	@SerializedName("MRP")
	private String mRP;

	@SerializedName("HEIGHT")
	private String hEIGHT;

	@SerializedName("NAME")
	private String nAME;

	@SerializedName("MOBILE_NO")
	private String mOBILENO;

	@SerializedName("STATUS")
	private String sTATUS;

	@SerializedName("CREATED")
	private String cREATED;

	@SerializedName("CITY")
	private String cITY;

	@SerializedName("DESCRIPTION")
	private String dESCRIPTION;

	@SerializedName("BUSINESS_TYPE")
	private String bUSINESSTYPE;

	@SerializedName("BRAND_NAME")
	private String bRANDNAME;

	@SerializedName("ADDRESS")
	private String aDDRESS;

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

	@SerializedName("PINCODE_NO")
	private String pINCODENO;

	@SerializedName("USER_TYPE")
	private String userType;



	public String getPRICE(){
		return pRICE;
	}

	public String getBUSINESSNAME(){
		return bUSINESSNAME;
	}

	public String getUPDATED(){
		return uPDATED;
	}

	public String getMRP(){
		return mRP;
	}

	public String getHEIGHT(){
		return hEIGHT;
	}

	public String getNAME(){
		return nAME;
	}

	public String getMOBILENO(){
		return mOBILENO;
	}

	public String getSTATUS(){
		return sTATUS;
	}

	public String getCREATED(){
		return cREATED;
	}

	public String getCITY(){
		return cITY;
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

	public String getADDRESS(){
		return aDDRESS;
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

	public String getPINCODENO(){
		return pINCODENO;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
}