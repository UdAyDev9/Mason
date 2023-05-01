package narasimhaa.com.mitraservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;



public class SplashActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 2000);

    }
}


