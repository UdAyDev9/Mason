package narasimhaa.com.mitraservice.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import narasimhaa.com.mitraservice.Model.MaterialFilter.DataItem;

public class AllOrdersResponse {

    @SerializedName("data")
    private List<OrdersDataItem> data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public List<OrdersDataItem> getData(){
        return data;
    }

    public String getMessage(){
        return message;
    }

    public boolean isStatus(){
        return status;
    }
}
