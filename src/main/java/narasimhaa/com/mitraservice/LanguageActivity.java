package narasimhaa.com.mitraservice;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import narasimhaa.com.mitraservice.Listener.GenericSelectListner;

public class LanguageActivity extends AppCompatActivity {
    private GenericSelectListner listner;
    TextView tx_language;
    LanguageActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        activity=this;
     /*  setEvents();

        listner = new GenericSelectListner() {
            public void onClick(View v) {
                Intent i = new Intent(LanguageActivity.this, MainActivity.class);
                startActivity(i);
            }
            @Override
            public void setItem(String name) {
                if (MyUtilities.isNull(name)) {
                    String[] code = name.split("\\;");

                    tx_language.setText(code[0]);


                }

            }

        };

    }

    private void setEvents() {

        tx_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //  if (MyUtilities.isOnline(context)) {

                Dialog d = MyUtilities.showMyPoup(activity, MyUtilities.getRegistrationType(activity), listner, "User Type", "dropdown", "");
                if (!d.isShowing()) {
                    d.show();
                }
                //  } else {

                //  MyUtilities.showToastNoInternet(context);

                //  }
            }
        });*/

    }
}