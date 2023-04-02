package narasimhaa.com.mitraservice.Utility;

/**
 * Created by srinivasaon 22-2-018.
 */

public class URLUtilites {

    private static URLUtilites utilites;
   // private String remoteUrl = "http://www.zorocabs.com/";


   // public String url_signup="http://zoroapp-env.ap-south-1.elasticbeanstalk.com/reg.php";
   // public String url_signin="http://zoroapp-env.ap-south-1.elasticbeanstalk.com/signin.php";
       public String url_signup="http://rebatee.in/zoro/registration.php";
       public String url_signin="http://rebatee.in/zoro/login.php";
      public String url_forgot="http://rebatee.in/zoro/login.php";
    public String url_password_verify="http://rebatee.in/zoro/login.php";



    public static URLUtilites getInstance()
    {
        if (utilites==null)
        utilites=new URLUtilites();
        return utilites;
    }

}
