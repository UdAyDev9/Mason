package narasimhaa.com.mitraservice.Model;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

   /* @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private User user;

    public Result(Boolean error, String message, User user) {
        this.error = error;
        this.message = message;
        this.user = user;
    }

    public Boolean getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
    */
   @SerializedName("msg")
   @Expose
   private String msg;
    @SerializedName("status")
    @Expose
    private Integer code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        Log.i("setcode", String.valueOf(this.code));
        if(code==null){
            code=500;
        }
        return code;
    }

    public void setCode(Integer code) {
        Log.i("setcode", String.valueOf(this.code));
        if(code==null){
            code=500;
        }

        this.code = code;
    }
}
