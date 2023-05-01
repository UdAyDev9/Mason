package narasimhaa.com.mitraservice;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import narasimhaa.com.mitraservice.Model.ResponseStatus;
import narasimhaa.com.mitraservice.Model.TaskListeners;
import narasimhaa.com.mitraservice.Utility.MyUtilities;
import narasimhaa.com.mitraservice.db.UserT;

public class ForgetPasswordResultActivity extends AppCompatActivity implements TaskListeners {
    private Toolbar toolbar;
    private ForgetPasswordResultActivity context;
   EditText et_otp,newpassword,Conformpassword;
   Button confirmforget;

   String backendOTP="";
   String backendUID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_passwordresult);

        context = this;

        Bundle bundle=getIntent().getExtras();

        if(bundle!=null & bundle.containsKey("otp")) {

            backendOTP =bundle.getString("otp");
            backendUID = bundle.getString("uid");
        }
       // MyUtilities.showToast(context,backendOTP);

        initUI();
        setEvents();
        setupActionBar();

    }
    private void initUI(){

        confirmforget=(Button)findViewById(R.id.confirmforget);
        et_otp = findViewById(R.id.et_otp);
        newpassword = findViewById(R.id.newpassword);
        Conformpassword= findViewById(R.id.Conformpassword);

    }
    private void setEvents() {

        confirmforget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String frontendOTP=et_otp.getText()+"";
                String enterNewPassword=newpassword.getText()+"";
                String enterConfirmPassword=Conformpassword.getText()+"";

             if(!MyUtilities.isNull(frontendOTP)){
                    MyUtilities.showToast(context,"Please enter OTP.");

                }else if(!MyUtilities.isNull(enterNewPassword)){
                    MyUtilities.showToast(context,"Please enter new password.");

                }else if(!MyUtilities.isNull(enterConfirmPassword)){
                    MyUtilities.showToast(context,"Please enter confirm password.");

                }else if(!enterNewPassword.contains(enterConfirmPassword)){
                    MyUtilities.showToast(context,"Please new and confirm password not matched.");

                }else if(!MyUtilities.isOnline(context)){
                    MyUtilities.showToast(context,"Please check Internet.");
                } else if(MyUtilities.isNull(frontendOTP) && backendOTP.contains(frontendOTP) && MyUtilities.isOnline(context)){

                 UserT userT=new UserT();
                 userT.setUid(backendUID);
                 userT.setPassword(enterConfirmPassword);

                   // APIAsyncTaskWithProgressDialog.getInstance().start(context, URLUtilites.getInstance().url_password_verify,new Gson().toJson(userT),false,ForgetPasswordResultActivity.this,1);

                }

            }
        });
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setTitle(context.getResources().getString(R.string.title_ResetPassword));
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if ( id == android.R.id.home) {

            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void completedTask(ResponseStatus res, int type) {

        if(res.isSucces()) {

            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        }

        MyUtilities.showToast(context,res.getMssg());

    }
}


