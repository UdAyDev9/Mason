package narasimhaa.com.mitraservice.Model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("paths")
	private List<PathsItem> paths;

	@SerializedName("base_url")
	private String baseUrl;

	public void setPaths(List<PathsItem> paths){
		this.paths = paths;
	}

	public List<PathsItem> getPaths(){
		return paths;
	}

	public void setBaseUrl(String baseUrl){
		this.baseUrl = baseUrl;
	}

	public String getBaseUrl(){
		return baseUrl;
	}
}