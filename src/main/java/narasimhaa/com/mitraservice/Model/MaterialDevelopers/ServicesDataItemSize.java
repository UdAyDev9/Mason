package narasimhaa.com.mitraservice.Model.MaterialDevelopers;

import com.google.gson.annotations.SerializedName;

public class ServicesDataItemSize {

	@SerializedName("ID")
	private String iD;

	@SerializedName(value = "BRAND", alternate = {"SIZE","HEIGHT","BTYPE"})
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