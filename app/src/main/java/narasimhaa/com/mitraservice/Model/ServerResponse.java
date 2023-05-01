package narasimhaa.com.mitraservice.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerResponse {

    @SerializedName("status")
    public boolean status;

    @SerializedName(value = "message", alternate = {"msg"})
    public String msg;

    @SerializedName(value = "code")
    public String code;

    @SerializedName("content")
    public List<ContentProfile> content;

    @SerializedName("data")
    public ContentProfile data;

    public boolean isStatus() {
        return status;
    }

    public ContentProfile getData() {
        return data;
    }

    public void setData(ContentProfile data) {
        this.data = data;
    }

    public List<ContentProfile> getContent() {
        return content;
    }

    public void setContent(List<ContentProfile> content) {
        this.content = content;
    }

    public ServerResponse(boolean status, String msg) {
        this.status = status;
        this.msg = msg;
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
