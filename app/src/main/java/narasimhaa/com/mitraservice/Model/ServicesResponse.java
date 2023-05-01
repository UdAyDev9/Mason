package narasimhaa.com.mitraservice.Model;


import java.util.List;

import narasimhaa.com.mitraservice.Model.service.ServicesDataItem;

public class ServicesResponse
{
    private List<ServicesDataItem> data;

    private String message;

    private boolean status;

    public List<ServicesDataItem> getData() {
        return data;
    }

    public void setData(List<ServicesDataItem> data) {
        this.data = data;
    }

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public boolean getStatus ()
    {
        return status;
    }

    public void setStatus (boolean status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [data = "+data+", message = "+message+", status = "+status+"]";
    }
}
