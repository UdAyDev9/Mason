package narasimhaa.com.mitraservice.Model.MaterialDevelopers;


import java.util.List;

public class ServicesResponseSizeBrand
{
    private List<ServicesDataItemSize> data;

    private String message;

    private boolean status;

    public List<ServicesDataItemSize> getData() {
        return data;
    }

    public void setData(List<ServicesDataItemSize> data) {
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
