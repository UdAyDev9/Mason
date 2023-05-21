package narasimhaa.com.mitraservice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kinda.alert.KAlertDialog;

import java.util.concurrent.TimeUnit;

import narasimhaa.com.mitraservice.Adapater.DashboardTabsAdapater;
import narasimhaa.com.mitraservice.Model.ContentProfile;
import narasimhaa.com.mitraservice.Model.ServerResponse;
import narasimhaa.com.mitraservice.Utility.MyUtilities;
import narasimhaa.com.mitraservice.Utility.SharedPreferenceUtils;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DashBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    AlertDialog.Builder dialog;

    Button yes, no;
    androidx.appcompat.widget.Toolbar toolbar;
    private Menu menu;
    private TextView availableTv, tvHeaderName, tvHeaderMail, tvHeaderType;
    private ImageView ivNotavaialable;
    private boolean isAvailable = true;
    SharedPreferences sharedPreferences;
    public String servicePersonId = "";
    public String pincode = "";
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView imgNavClick;
    Switch switchChangeStatus;
    private boolean isDeveloper = false;
    NavigationView navigationView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
       /* toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("DashBoard");*/
        SharedPreferenceUtils.setLoginStatus(DashBoardActivity.this, true);
        availableTv = (TextView) findViewById(R.id.tv_available);
        ivNotavaialable = (ImageView) findViewById(R.id.not_available_iv);
        imgNavClick = (ImageView) findViewById(R.id.img_nav_click);
        switchChangeStatus = (Switch) findViewById(R.id.switch_action_bar);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        imgNavClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        sharedPreferences = getSharedPreferences("LoginPREFERENCES", MODE_PRIVATE);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navigationView.getMenu();

        if (SharedPreferenceUtils.getValue(DashBoardActivity.this, MyUtilities.PREF_USER_TYPE).equals("Retailer")) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.nav_menu_for_user);
            switchChangeStatus.setVisibility(View.INVISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            //viewPager.setVisibility(View.GONE);
            //tabLayout.setVisibility(View.GONE);
        } else if (SharedPreferenceUtils.getValue(DashBoardActivity.this, MyUtilities.PREF_USER_TYPE).equals("Developer")) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_dash_board_drawer);
            switchChangeStatus.setVisibility(View.GONE);
            MenuItem nav_add_developer_or_material = menu.findItem(R.id.AddMyService);
            nav_add_developer_or_material.setTitle("Add Service");
            isDeveloper = true;
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);

        } else if (SharedPreferenceUtils.getValue(DashBoardActivity.this, MyUtilities.PREF_USER_TYPE).equals("Admin")) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.nav_menu_for_admin);
            //switchChangeStatus.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);

        } else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_dash_board_drawer);
            //switchChangeStatus.setVisibility(View.GONE);
            MenuItem nav_add_developer_or_material = menu.findItem(R.id.AddMyService);
            MenuItem nav_add_update_developer_material = menu.findItem(R.id.nav_update_service_material);
            nav_add_developer_or_material.setTitle("Add Material");
            nav_add_update_developer_material.setTitle("Update Material");
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
        }

        /*tvHeaderName = (TextView) navigationView.findViewById(R.id.tv_header_name);
          tvHeaderMail = (TextView) navigationView.findViewById(R.id.tv_header_mail);*/

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {


                navigationView.removeOnLayoutChangeListener(this);

                tvHeaderName = (TextView) navigationView.findViewById(R.id.tv_header_name);
                tvHeaderMail = (TextView) navigationView.findViewById(R.id.tv_header_mail);
                tvHeaderType = (TextView) navigationView.findViewById(R.id.tv_header_type);

                tvHeaderName.setText(SharedPreferenceUtils.getValue(DashBoardActivity.this, MyUtilities.PREF_USER_NAME));
                tvHeaderMail.setText(SharedPreferenceUtils.getValue(DashBoardActivity.this, MyUtilities.PREF_EMAIL));
                tvHeaderType.setText(SharedPreferenceUtils.getValue(DashBoardActivity.this, MyUtilities.PREF_USER_TYPE));

            }
        });

        getData();
        getStaffData();

        tabLayout.addTab(tabLayout.newTab().setText("Developers"));
        tabLayout.addTab(tabLayout.newTab().setText("Materials"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final DashboardTabsAdapater adapter = new DashboardTabsAdapater(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        switchChangeStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {

                    updateStatus("ACTIVE");

                } else {

                    updateStatus("INACTIVE");

                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       /* getMenuInflater().inflate(R.menu.dash_board, menu);

        MenuItem itemSwitch = menu.findItem(R.id.switch_action_bar);
        itemSwitch.setActionView(R.layout.use_switch);

        Switch sw = (Switch) menu.findItem(R.id.switch_action_bar).getActionView().findViewById(R.id.switch_change_status);


        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(DashBoardActivity.this, "Available", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(DashBoardActivity.this, "Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // custom dialog
            final Dialog dialog = new Dialog(DashBoardActivity.this);
            dialog.setContentView(R.layout.custom_button);
            dialog.getWindow().setLayout(DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.WRAP_CONTENT);
            dialog.setTitle("Title...");

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            //text.setText("Android custom dialog example!");

            yes = (Button) dialog.findViewById(R.id.b1);

            // if button is clicked, close the custom dialog
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    availableTv.setVisibility(View.GONE);
                    availableTv.setText("Available");
                    availableTv.setTextColor(getColor(R.color.green));
                    ivNotavaialable.setVisibility(View.GONE);
                    isAvailable = true;
                    dialog.dismiss();

                }
            });


            no = (Button) dialog.findViewById(R.id.no);

            // if button is clicked, close the custom dialog
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                       /* availableTv.setVisibility(View.GONE);
                        availableTv.setText("Not Available");
                        availableTv.setTextColor(getColor(R.color.red));*/
                    ivNotavaialable.setVisibility(View.VISIBLE);
                    isAvailable = false;
                    dialog.dismiss();

                }
            });

            dialog.show();


            //alertMsg();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    //@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        // Handle navigation view item clicks here.


        if (id == R.id.MYProfile) {

            Intent i = new Intent(DashBoardActivity.this, MyprofileActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_images) {

            Intent i = new Intent(DashBoardActivity.this, ImagesActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_all_orders) {

            if (SharedPreferenceUtils.getValue(DashBoardActivity.this, MyUtilities.PREF_USER_TYPE).equals("Dealer")
                    || SharedPreferenceUtils.getValue(DashBoardActivity.this, MyUtilities.PREF_USER_TYPE).equals("Material Supplier")
                    || SharedPreferenceUtils.getValue(DashBoardActivity.this, MyUtilities.PREF_USER_TYPE).equals("Developer")) {


                Intent i = new Intent(DashBoardActivity.this, AllOrdersDetailsActivity.class);
                startActivity(i);
            } else if (SharedPreferenceUtils.getValue(DashBoardActivity.this, MyUtilities.PREF_USER_TYPE).equals("Individual")
                    || SharedPreferenceUtils.getValue(DashBoardActivity.this, MyUtilities.PREF_USER_TYPE).equals("Retailer")) {
                Intent i = new Intent(DashBoardActivity.this, ConsumerAllOrdersDetailsActivity.class);
                startActivity(i);
            }


        } else if (id == R.id.nav_update_service_material) {

            if (SharedPreferenceUtils.getValue(DashBoardActivity.this, MyUtilities.PREF_USER_TYPE).equals("Developer")) {

                Intent i = new Intent(DashBoardActivity.this, ServiceUpdateActivity.class);
                startActivity(i);

            } else {
                Intent i = new Intent(DashBoardActivity.this, MaterialUpdateActivity.class);
                startActivity(i);
            }


        } else if (id == R.id.nav_share) {
            Intent i = new Intent(DashBoardActivity.this, ShareActivity.class);
            startActivity(i);


        } else if (id == R.id.AddMyService) {

            if (SharedPreferenceUtils.getValue(DashBoardActivity.this, MyUtilities.PREF_USER_TYPE).equals("Developer")) {

                Intent i = new Intent(DashBoardActivity.this, AddMyServiceActivity.class);
                i.putExtra(MyUtilities.IS_DEVELOPER, isDeveloper);
                startActivity(i);
            } else {

                Intent i = new Intent(DashBoardActivity.this, AddMaterialActivity.class);
                i.putExtra(MyUtilities.IS_DEVELOPER, isDeveloper);
                startActivity(i);
            }


        } else if (id == R.id.nav_dealer_list) {

            Intent i = new Intent(DashBoardActivity.this, AllUserListAdminActivity.class);
            //i.putExtra(MyUtilities.IS_DEVELOPER, isDeveloper);
            startActivity(i);


        } else if (id == R.id.nav_add_brand_size) {

            Intent i = new Intent(DashBoardActivity.this, AddSizesAndBrandsActivity.class);
            //i.putExtra(MyUtilities.IS_DEVELOPER, isDeveloper);
            startActivity(i);

        } else if (id == R.id.nav_sevice) {

            Intent i = new Intent(DashBoardActivity.this, AddServicesType.class);
            //i.putExtra(MyUtilities.IS_DEVELOPER, isDeveloper);
            startActivity(i);

        } else if (id == R.id.nav_list) {

            Intent i = new Intent(DashBoardActivity.this, ServiceListActivity.class);
            //i.putExtra(MyUtilities.IS_DEVELOPER, isDeveloper);
            startActivity(i);
        } else if (id == R.id.nav_brand_list) {

            Intent i = new Intent(DashBoardActivity.this, BrandsActivity.class);
            //i.putExtra(MyUtilities.IS_DEVELOPER, isDeveloper);
            startActivity(i);


        } else if (id == R.id.nav_sizes_list) {

            Intent i = new Intent(DashBoardActivity.this, SizesActivity.class);
            //i.putExtra(MyUtilities.IS_DEVELOPER, isDeveloper);
            startActivity(i);
        } else if (id == R.id.nav_addsub) {

            Intent i = new Intent(DashBoardActivity.this, AddSubcategory.class);
            //i.putExtra(MyUtilities.IS_DEVELOPER, isDeveloper);
            startActivity(i);

        } else if (id == R.id.nav_addsublist) {

            Intent i = new Intent(DashBoardActivity.this, SubcategoryListActivity.class);
            //i.putExtra(MyUtilities.IS_DEVELOPER, isDeveloper);
            startActivity(i);
        } else if (id == R.id.nav_addshape) {

            Intent i = new Intent(DashBoardActivity.this, AddShapeActivity.class);
            //i.putExtra(MyUtilities.IS_DEVELOPER, isDeveloper);
            startActivity(i);

        } else if (id == R.id.nav_addOthers) {

            Intent i = new Intent(DashBoardActivity.this, AddOthersActivity.class);
            //i.putExtra(MyUtilities.IS_DEVELOPER, isDeveloper);
            startActivity(i);

        } else if (id == R.id.nav_shapelist) {

            Intent i = new Intent(DashBoardActivity.this, ShapeListActivity.class);
            //i.putExtra(MyUtilities.IS_DEVELOPER, isDeveloper);
            startActivity(i);

        } else if (id == R.id.nav_signout) {


            Intent i = new Intent(DashBoardActivity.this, MainActivity.class);
            startActivity(i);
            SharedPreferenceUtils.setLoginStatus(DashBoardActivity.this, false);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        invalidateOptionsMenu();
        MenuItem item = menu.findItem(R.id.action_settings);
        MenuItem itemSwitch = menu.findItem(R.id.switch_action_bar);
        itemSwitch.setActionView(R.layout.use_switch);
        Switch sw = (Switch) menu.findItem(R.id.switch_action_bar).getActionView().findViewById(R.id.switch_change_status);


        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(DashBoardActivity.this, "Available", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DashBoardActivity.this, "Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if (isAvailable == true) {

            //SpannableString s = new SpannableString("AVAILABLE");
            SpannableString s = new SpannableString("AVAILABLE");
            s.setSpan(new ForegroundColorSpan(Color.GREEN), 0, s.length(), 0);
            // getData();
            //pdateUserStatus("YES");
            //item.setTitle("AVAILABLE");
            item.setTitle(s);
        } else {
            //SpannableString s = new SpannableString("NOT AVAILABLE");
            SpannableString s2 = new SpannableString("NOT AVAILABLE");
            updateUserStatus("NO");
            s2.setSpan(new ForegroundColorSpan(Color.RED), 0, s2.length(), 0);
            //item.setTitle("NOT AVAILABLE");
            item.setTitle(s2);
        }
        return super.onPrepareOptionsMenu(menu);

    }


    public void getData() {

        try {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(ApiInterface.URL_BASE)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiInterface apiInterface = retrofit.create(ApiInterface.class);


            sharedPreferences = getSharedPreferences("LoginPREFERENCES", MODE_PRIVATE);

            JsonObject paramObject = new JsonObject();
            //paramObject.addProperty("EMAIL_ID", "krishna@appcare.co.in");
            paramObject.addProperty("EMAIL_ID", sharedPreferences.getString("userName", ""));


            Call<ServerResponse> userCall = apiInterface.getProfileResponse(paramObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateUserStatus(String availabilityStr) {

        try {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();


            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(ApiInterface.URL_BASE)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiInterface apiInterface = retrofit.create(ApiInterface.class);


            Log.d("ServiceId", "updateUserStatus: " + servicePersonId + pincode);
            JsonObject paramObject = new JsonObject();

            paramObject.addProperty("SERVICE_PERSON_ID", servicePersonId);
            paramObject.addProperty("AVAILABILITY", availabilityStr);

            Call<ServerResponse> userCall = apiInterface.updateAvailability(paramObject.toString());

            userCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {

                    MyUtilities.showToast(DashBoardActivity.this, MyUtilities.KAlertDialogTitleError);
                    MyUtilities.cancelAlertDialog(DashBoardActivity.this);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateStatus(String status) {

        MyUtilities.showAlertDialog(DashBoardActivity.this, KAlertDialog.PROGRESS_TYPE, MyUtilities.KAlertDialogTitleLoding);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.URL_BASE)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);


        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("EMAIL_ID", SharedPreferenceUtils.getValue(DashBoardActivity.this, MyUtilities.PREF_EMAIL));
        paramObject.addProperty("STATUS", status);

        Call<ServerResponse> userCall = apiInterface.changeStatus(paramObject.toString());

        userCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {


                if (response.body() != null) {

                    if (response.body().getStatus() == true) {

                        MyUtilities.cancelAlertDialog(DashBoardActivity.this);

                        getStaffData();
                    } else {

                        MyUtilities.cancelAlertDialog(DashBoardActivity.this);
                        MyUtilities.showToast(DashBoardActivity.this, MyUtilities.KAlertDialogTitleSwitch);
                        switchChangeStatus.setChecked(false);

                    }

                } else {

                    MyUtilities.cancelAlertDialog(DashBoardActivity.this);
                    MyUtilities.showToast(DashBoardActivity.this, MyUtilities.KAlertDialogTitleSwitch);
                    switchChangeStatus.setChecked(false);

                }
            }


            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                MyUtilities.cancelAlertDialog(DashBoardActivity.this);
                MyUtilities.showToast(DashBoardActivity.this, MyUtilities.KAlertDialogTitleError);
                switchChangeStatus.setChecked(false);
            }
        });
    }

    public void getStaffData() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.URL_BASE)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        sharedPreferences = getSharedPreferences("LoginPREFERENCES", MODE_PRIVATE);

        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("BOD_SEQ_NO", SharedPreferenceUtils.getValue(DashBoardActivity.this, MyUtilities.PREF_BOD_SEQ_NO));
        Call<ServerResponse> userCall = apiInterface.getStaffProfileResponse(SharedPreferenceUtils.getValue(DashBoardActivity.this, MyUtilities.PREF_EMAIL));

        userCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {


                if (response.body() != null) {

                    if (response.body().getStatus() == true) {

                        switchChangeStatus.setVisibility(View.GONE);

                        MyUtilities.cancelAlertDialog(DashBoardActivity.this);

                        ContentProfile homePageBeanData = response.body().getData();

                        SharedPreferenceUtils.setValue(DashBoardActivity.this, MyUtilities.PREF_SER_PER_SEQ_ID, homePageBeanData.getSER_PER_SEQ_ID());

                        if (homePageBeanData.getSTATUS().equals("ACTIVE")) {

                            switchChangeStatus.setChecked(true);
                            availableTv.setVisibility(View.GONE);
                            availableTv.setText("AVAILABLE");

                        } else {

                            switchChangeStatus.setChecked(false);
                            availableTv.setVisibility(View.GONE);
                            availableTv.setText("UNAVAILABLE");

                        }

                        if (homePageBeanData.getSERVICE_NAME().contains("POP")) {

                            navigationView.getMenu().clear();
                            navigationView.inflateMenu(R.menu.nav_menu_for_pop_designer);
                        }

                    } else {
                        MyUtilities.cancelAlertDialog(DashBoardActivity.this);

                        //MyUtilities.showToast(DashBoardActivity.this, MyUtilities.KAlertDialogTitleError);

                        switchChangeStatus.setVisibility(View.GONE);
                    }


                    //Log.i("Baba", "Res ponse is " + homePageBeanData.toString());

                } else {

                    MyUtilities.cancelAlertDialog(DashBoardActivity.this);
                    MyUtilities.showToast(DashBoardActivity.this, MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                MyUtilities.showToast(DashBoardActivity.this, MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(DashBoardActivity.this);

            }
        });
    }
}