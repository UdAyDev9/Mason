package narasimhaa.com.mitraservice.Model;


public class ResponseStatus {
	private String Status;
	private String Mssg;
	private String Content;
	private String rawData;


	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}


	public boolean isSucces() {
		if (Status != null && Status.equalsIgnoreCase("1")) {
			return true;
		} else {
			return false;
		}
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getMssg() {
		return Mssg;
	}

	public void setMssg(String mssg) {
		Mssg = mssg;
	}


	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}
}
