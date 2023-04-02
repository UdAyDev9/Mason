package narasimhaa.com.mitraservice;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class AddNewServiceActivity extends AppCompatActivity {
    TextView et_username;
    Spinner spinner;
    AddNewServiceActivity context;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_service);

        context=this;

    /*    spinner = (Spinner) findViewById(R.id.sp_servicertype);
        et_username = (TextView) findViewById(R.id.et_username);
        et_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddNewServiceActivity.this, AddMyServiceActivity.class);
                startActivity(i);

            }
        });*/


    }








}
