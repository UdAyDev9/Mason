package narasimhaa.com.mitraservice;

import android.content.Intent;
import android.os.Bundle;


import android.view.MenuItem;
import android.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ShareActivity extends AppCompatActivity {
    private Toolbar toolbar;
   // private ShareActivity context;
    //TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_share);
       //text = (TextView)findViewById(R.id.text);
        //context = this;
        setupActionBar();
       // Intent intent = getIntent();
        //String str = intent.getStringExtra("message");
        //text.setText(str);
        //text.setBackgroundColor(Color.parseColor("#FF00FF"));
        share();
    }

    public void share() {
        String shareBody = "https://play.google.com/store/apps/details?id=************************";

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "APP NAME (Open it in Google Play Store to Download the Application)");

        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
        super.onBackPressed();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if ( id == android.R.id.home) {

            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
           // actionBar.setTitle(context.getResources().getString(R.string.title_App_share));
        }
    }


}
