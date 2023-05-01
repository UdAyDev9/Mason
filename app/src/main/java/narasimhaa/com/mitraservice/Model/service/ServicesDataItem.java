package narasimhaa.com.mitraservice.Model.service;

import com.google.gson.annotations.SerializedName;

public class ServicesDataItem{

	@SerializedName("ID")
	private String iD;

	@SerializedName(value = "MATERIAL", alternate = {"SERVICE_NAME"})
	private String sERVICETYPE;

	public void setID(String iD){
		this.iD = iD;
	}

	public String getID(){
		return iD;
	}

	public void setSERVICETYPE(String sERVICETYPE){
		this.sERVICETYPE = sERVICETYPE;
	}

	public String getSERVICE_NAME(){
		return sERVICETYPE;
	}
}