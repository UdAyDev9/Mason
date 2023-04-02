package narasimhaa.com.mitraservice;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


public class ContactsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ContactsActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contates);
        context = this;
        setupActionBar();

        setEvents();
    }
    private void setEvents() {
        ((TextView) findViewById(R.id.txt_dial)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri number = Uri.parse("tel:8333801642");
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                    startActivity(callIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ((TextView) findViewById(R.id.txt_dial1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri number = Uri.parse("tel:040-40191909");
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                    startActivity(callIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);

            //actionBar.setTitle(context.getResources().getString(R.string.title_Contact_No));

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;


            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }



}



