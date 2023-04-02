package narasimhaa.com.mitraservice.Model.Filter;

import java.util.List;

public class FilterResponse {


    private List<FilterDataItem> DATA;

    private String BASE_URL;

    public List<FilterDataItem> getDATA() {
        return DATA;
    }

    public void setDATA(List<FilterDataItem> DATA) {
        this.DATA = DATA;
    }

    public String getBASE_URL() {
        return BASE_URL;
    }

    public void setBASE_URL(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    @Override
    public String toString() {
        return "ClassPojo [DATA = " + DATA + ", BASE_URL = " + BASE_URL + "]";
    }


}
