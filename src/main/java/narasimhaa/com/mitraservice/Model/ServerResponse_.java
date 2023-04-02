package narasimhaa.com.mitraservice.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerResponse_ {

    @SerializedName("status")
    @Expose
    public boolean status;

    @SerializedName("message")
    @Expose
    public String msg;

      public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
