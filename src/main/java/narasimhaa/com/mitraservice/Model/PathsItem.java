package narasimhaa.com.mitraservice.Model;

import com.google.gson.annotations.SerializedName;

public class PathsItem{

	@SerializedName("IMAGE_PATH")
	private String iMAGEPATH;

	@SerializedName("CREATED_DATETIME")
	private String cREATEDDATETIME;

	public void setIMAGEPATH(String iMAGEPATH){
		this.iMAGEPATH = iMAGEPATH;
	}

	public String getIMAGEPATH(){
		return iMAGEPATH;
	}

	public void setCREATEDDATETIME(String cREATEDDATETIME){
		this.cREATEDDATETIME = cREATEDDATETIME;
	}

	public String getCREATEDDATETIME(){
		return cREATEDDATETIME;
	}
}