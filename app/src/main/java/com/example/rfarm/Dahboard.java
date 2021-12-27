package com.example.rfarm;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rfarm.constructors.ExpenseInfo;
import com.example.rfarm.constructors.Rabbit_Products;
import com.example.rfarm.constructors.mating_records;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Dahboard extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ConstraintLayout frameLayout;
    private NavigationView navigationView;
    private SwitchCompat darkModeSwitch;
    private  TextView drawer_username,drawer_email;
    private ImageView drawer_pic;
    private DatabaseReference mUsersDatabase,mUsersDatabaseIntExp;
    private DatabaseReference bunniesDatabase;
    private PieChart SalespieChart,ExpensesPiechart;
    private PieData SalespieData,ExpensespieData;
    private List<PieEntry> SalespieEntryList = new ArrayList<>();
    private List<PieEntry> ExpensespieEntryList = new ArrayList<>();
    private int amount_bunnies,amount_others,amount_doer,amount_buck;
    private float exp_fds=0;
    private  float exp_housing=0,exp_medication=0;
    private  float sls_bunny=0,sls_drop=0,sls_skin=0,sls_meat=0,sls_cages=0,sls_rabbit=0;
    private float exp_others=0;
    private  float total_amount=0;
    private float total_amt_sales=0,total_exp_amount=0,total_sls_amount=0;
    private float convert,covert_sales;
    private TextView t,d,expt;
    private static final int REQUEST_PERMISSION = 0;
    private Button expense_summary,sal_summary,sal_exp_summary;



    private NavigationView nvDrawer;
    private ActionBarDrawerToggle toggle;
    private BarChart chart;
    private SharedPreferences sharedPreferences;
    private String themeName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Handler handler;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dahboard);

        expense_summary=findViewById(R.id.exp_summary);
        sal_summary=findViewById(R.id.sales_summary);
        sal_exp_summary=findViewById(R.id.exp_sales_summary);

        //permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasReadPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

            List<String> permissions = new ArrayList<String>();
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            } else {
//              preferencesUtility.setString("storage", "true");
            }

            if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);

            } else {
//              preferencesUtility.setString("storage", "true");
            }

            if (!permissions.isEmpty()) {
//              requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE_SOME_FEATURES_PERMISSIONS);

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                        REQUEST_PERMISSION);
            }}
//showing graphs one by one
        expense_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExpPie();
            }
        });
        sal_exp_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBar();
            }
        });
        sal_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSalesPie();
            }
        });


        //persistent

        chart = findViewById(R.id.barchart);
        SalespieChart = findViewById(R.id.sales_pieChart);
        ExpensesPiechart=findViewById(R.id.expenses_pieChart);
        //database reference
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getUid()).child("profile");

        toolbar = findViewById(R.id.toolbar_id);
        toolbar.setTitle(R.string.toolbar_title);
        setSupportActionBar(toolbar);
        expt=findViewById(R.id.total_exp_dash);
        drawerLayout = findViewById(R.id.drawer_layout_id);
        frameLayout = findViewById(R.id.framelayout_id);
        navigationView = findViewById(R.id.navigationview_id);
        d=findViewById(R.id.decision);
        //pie chart


        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //nav header elements
        View header = navigationView.getHeaderView(0);
       drawer_email=header.findViewById(R.id.nav_header_emailaddress_id);
        drawer_username=header.findViewById(R.id.nav_header_name_id);
        drawer_pic=header.findViewById(R.id.nav_header_circleimageview_id);
        t=findViewById(R.id.total_sales_dash);
    navigationView.setItemIconTintList(null);
    navigationView.setItemIconSize(95);
    header.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(),EditProfileActivity.class));
        }
    });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_message_id:
                       startActivity(new Intent(Dahboard.this,Doer_buck.class));
                        finish();
                        //closeDrawer();
                        break;
                    case R.id.nav_chat_id:
                        startActivity(new Intent(Dahboard.this,Bunnies.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_profile_id:
                        startActivity(new Intent(Dahboard.this,Sales.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_notebooks_id:
                        startActivity(new Intent(Dahboard.this,Expenses.class));
                        finish();
                        //closeDrawer();
                        break;
                    case R.id.nav_send_id:
                        startActivity(new Intent(Dahboard.this,Herd.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_share_id:
                        startActivity(new Intent(Dahboard.this,Expenses_Records.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_photos_id:
                        startActivity(new Intent(Dahboard.this,BreedingRabbits.class));
                       // closeDrawer();
                        finish();
                        break;
                    case R.id.nav_darkmode_id:
                        startActivity(new Intent(Dahboard.this,AboutRfarm.class));
                        // closeDrawer();
                        finish();
                        break;


                    case R.id.nav_trash_id:
                        startActivity(new Intent(Dahboard.this,BreedingHistory.class));
                       // closeDrawer();
                        finish();
                        break;
                    case R.id.nav_settings_id:
                        startActivity(new Intent(Dahboard.this,SalesRecords.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_signout_id:



                        AlertDialog alertDialog = new AlertDialog.Builder(Dahboard.this)
//set icon
                                .setIcon(R.drawable.app_log)
//set title
                                .setTitle("Are you sure to Exit")
//set message
                                .setMessage("Exit!")
//set positive button
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FirebaseAuth.getInstance().signOut();
                                        startActivity(new Intent(Dahboard.this,Home.class));
                                        finish();
                                    }
                                })
//set negative button
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //set what should happen when negative button is clicked
                                        //Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                                    }
                                })
                                .show();



                        break;
                }
                deSelectCheckedState();
                closeDrawer();
                return true;
            }
        });



        //add listener to database reference
        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    //email address from user authentication
                    drawer_email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

                    //user name from database
                    drawer_username.setText(snapshot.child("name").getValue().toString());
                    // Profile picture on drawer
                    Picasso.get().load(snapshot.child("image").getValue().toString()).resize(200,200).placeholder(R.drawable.bunnies2).into(drawer_pic);

                }else {
                    startActivity(new Intent(getApplicationContext(),Home.class));
                }


            }


            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }

        });
        //checking mating records to throw notification to user
        bunniesDatabase=FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mating_records");
        // Adding Add Value Event Listener to databaseReference.
        Query ref=bunniesDatabase.orderByChild("time_birth").equalTo(true);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    mating_records imageUploadInfo = postSnapshot.getValue(mating_records.class);
                    if(imageUploadInfo.getBirth_prep_date().equals(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()))){
                        //show notification
                        showBunnyDOBPrepNotification("Prepare Hatchery for: "+imageUploadInfo.getDoe_nm(),"Delivery date expected latest: "+imageUploadInfo.getBirth_date());
                    }else    if(imageUploadInfo.getBirth_date().equals(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()))){
                        showBunnyDOBnotification(imageUploadInfo.getDoe_nm(),"Record Bunnies born and update Date of birth if not accurate!",imageUploadInfo.getNumber_of_bunnies(),imageUploadInfo.getN_bunnies_alive(),imageUploadInfo.getN_bunnies_died(),imageUploadInfo.getBirth_date(),imageUploadInfo.getDoe_nm(),imageUploadInfo.getBuck_nm(),postSnapshot.getKey());
                    }
                    else {

                    }



                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        //sales pie chart data
        SalespieChart.setUsePercentValues(true);
        SalespieChart.setCenterText("Sales");
        SalespieChart.setCenterTextColor(Color.RED);
        DatabaseReference sales_ref = FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("my_sales");
        sales_ref.keepSynced(true);
        sales_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //total sales amount
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    Rabbit_Products imUpload=postSnapshot.getValue(Rabbit_Products.class);
                    total_amt_sales+=imUpload.getAmount();

                }
                covert_sales=100/total_amt_sales;
                Query ref=sales_ref.orderByChild("prdct_type").equalTo("bunny");
                Query ref1=sales_ref.orderByChild("prdct_type").equalTo("droppings and urine");
                Query ref2=sales_ref.orderByChild("prdct_type").equalTo("skins");
                Query ref3=sales_ref.orderByChild("prdct_type").equalTo("meat");
                Query ref4=sales_ref.orderByChild("prdct_type").equalTo("cages");
                Query ref5=sales_ref.orderByChild("prdct_type").equalTo("mature_rabbit");
                ref5.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot: snapshot.getChildren()){
                            Rabbit_Products imUpload=postSnapshot.getValue(Rabbit_Products.class);
                            sls_rabbit+=imUpload.getAmount();

                        }


                        if((sls_rabbit*convert)!=0){
                            SalespieEntryList.add(new PieEntry((sls_rabbit*convert),"Does and Bucks"));
                        }else {

                        }
                        PieDataSet pieDataSet = new PieDataSet(SalespieEntryList,"Product");
                        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                        SalespieData = new PieData(pieDataSet);
                        SalespieChart.setData(SalespieData);
                        SalespieChart.invalidate();

                    }


                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
                ref4.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot: snapshot.getChildren()){
                            Rabbit_Products imUpload=postSnapshot.getValue(Rabbit_Products.class);
                            sls_cages+=imUpload.getAmount();

                        }


                        if((sls_cages*convert)!=0){
                            SalespieEntryList.add(new PieEntry((sls_cages*convert),"Cages"));
                        }else {

                        }
                        PieDataSet pieDataSet = new PieDataSet(SalespieEntryList,"Product");
                        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                        SalespieData = new PieData(pieDataSet);
                        SalespieChart.setData(SalespieData);
                        SalespieChart.invalidate();

                    }


                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

                ref3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot: snapshot.getChildren()){
                            Rabbit_Products imUpload=postSnapshot.getValue(Rabbit_Products.class);
                            sls_meat+=imUpload.getAmount();

                        }


                        if((sls_meat*convert)!=0){
                            SalespieEntryList.add(new PieEntry((sls_meat*convert),"Meat"));
                        }else {

                        }
                        PieDataSet pieDataSet = new PieDataSet(SalespieEntryList,"Product");
                        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                        SalespieData = new PieData(pieDataSet);
                        SalespieChart.setData(SalespieData);
                        SalespieChart.invalidate();

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
                ref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot: snapshot.getChildren()){
                            Rabbit_Products imUpload=postSnapshot.getValue(Rabbit_Products.class);
                            sls_skin+=imUpload.getAmount();

                        }


                        if((sls_skin*convert)!=0){
                            SalespieEntryList.add(new PieEntry((sls_skin*convert),"Skins"));
                        }else {

                        }
                        PieDataSet pieDataSet = new PieDataSet(SalespieEntryList,"Product");
                        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                        SalespieData = new PieData(pieDataSet);
                        SalespieChart.setData(SalespieData);
                        SalespieChart.invalidate();

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
                ref1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot: snapshot.getChildren()){
                            Rabbit_Products imUpload=postSnapshot.getValue(Rabbit_Products.class);
                            sls_drop+=imUpload.getAmount();

                        }


                        if((sls_drop*convert)!=0){
                            SalespieEntryList.add(new PieEntry((sls_drop*convert),"Fertilizers"));
                        }else {

                        }
                        PieDataSet pieDataSet = new PieDataSet(SalespieEntryList,"Product");
                        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                        SalespieData = new PieData(pieDataSet);
                        SalespieChart.setData(SalespieData);
                        SalespieChart.invalidate();

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot: snapshot.getChildren()){
                            Rabbit_Products imUpload=postSnapshot.getValue(Rabbit_Products.class);
                            sls_bunny+=imUpload.getAmount();

                        }


                        if((sls_bunny*convert)!=0){
                            SalespieEntryList.add(new PieEntry((sls_bunny*convert),"Bunnies"));
                        }else {

                        }
                        PieDataSet pieDataSet = new PieDataSet(SalespieEntryList,"Product");
                        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                        SalespieData = new PieData(pieDataSet);
                        SalespieChart.setData(SalespieData);
                        SalespieChart.invalidate();

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });




        //expenses pie chart data
        DatabaseReference mUsersDatabaseIntExp=FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("my_expenses");
        mUsersDatabaseIntExp.keepSynced(true);
        ExpensesPiechart.setUsePercentValues(true);
        ExpensesPiechart.setCenterText("Expenses");
        ExpensesPiechart.setCenterTextColor(Color.RED);
        mUsersDatabaseIntExp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot:snapshot.getChildren()){
                    ExpenseInfo expenseInfo=postSnapshot.getValue(ExpenseInfo.class);
                    total_amount+=Float.parseFloat(expenseInfo.getExp_amount());


                }
                convert=100/total_amount;
                Query ref=mUsersDatabaseIntExp.orderByChild("exp_category").equalTo("Housing");
                Query ref1=mUsersDatabaseIntExp.orderByChild("exp_category").equalTo("Feeds");
                Query ref2=mUsersDatabaseIntExp.orderByChild("exp_category").equalTo("Others");
                Query ref3=mUsersDatabaseIntExp.orderByChild("exp_category").equalTo("Medication");


                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot: snapshot.getChildren()){
                            ExpenseInfo imageUploadInfo = postSnapshot.getValue(ExpenseInfo.class);
                            exp_housing+=Float.parseFloat(imageUploadInfo.getExp_amount());

                        }


                        if((exp_housing*convert)!=0){
                            ExpensespieEntryList.add(new PieEntry((int)(exp_housing*convert),"Cages"));
                        }else {

                        }

                        PieDataSet ExppieDataSet = new PieDataSet(ExpensespieEntryList,"Category");
                        ExppieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        ExpensespieData = new PieData(ExppieDataSet);
                        ExpensesPiechart.setData(ExpensespieData);
                        ExpensesPiechart.invalidate();

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
                ref1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot: snapshot.getChildren()){
                            ExpenseInfo imageUploadInfo = postSnapshot.getValue(ExpenseInfo.class);
                            exp_fds+=Float.parseFloat(imageUploadInfo.getExp_amount());

                        }
                        if((exp_fds*convert)!=0){
                            ExpensespieEntryList.add(new PieEntry((int)(exp_fds*convert),"Feeds"));
                        }else {

                        }


                        //ExpensespieEntryList.add(new PieEntry((int)(exp_fds*convert),"Feeds"));
                        PieDataSet ExppieDataSet = new PieDataSet(ExpensespieEntryList,"Category");
                        ExppieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        ExpensespieData = new PieData(ExppieDataSet);
                        ExpensesPiechart.setData(ExpensespieData);
                        ExpensesPiechart.invalidate();

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
                ref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot: snapshot.getChildren()){
                            ExpenseInfo imageUploadInfo = postSnapshot.getValue(ExpenseInfo.class);
                            exp_others+=Float.parseFloat(imageUploadInfo.getExp_amount());

                        }

                        if(((exp_others*convert))!=0){
                            ExpensespieEntryList.add(new PieEntry((int)(exp_others*convert),"Others"));
                        }else {

                        }

                       // ExpensespieEntryList.add(new PieEntry((int)(exp_others*convert),"Others"));
                        PieDataSet ExppieDataSet = new PieDataSet(ExpensespieEntryList,"Category");
                        ExppieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        ExpensespieData = new PieData(ExppieDataSet);
                        ExpensesPiechart.setData(ExpensespieData);
                        ExpensesPiechart.invalidate();

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
                ref3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot: snapshot.getChildren()){
                            ExpenseInfo imageUploadInfo = postSnapshot.getValue(ExpenseInfo.class);
                            exp_medication+=Float.parseFloat(imageUploadInfo.getExp_amount());

                        }

                        if(((exp_medication*convert))!=0){
                            ExpensespieEntryList.add(new PieEntry((int)(exp_medication*convert),"Medication"));
                        }else {

                        }

                      //  ExpensespieEntryList.add(new PieEntry((int)(exp_medication*convert),"Medication"));
                        PieDataSet ExppieDataSet = new PieDataSet(ExpensespieEntryList,"Category");
                        ExppieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        ExpensespieData = new PieData(ExppieDataSet);
                        ExpensesPiechart.setData(ExpensespieData);
                        ExpensesPiechart.invalidate();

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }

        });

     //bar graph
        // Data Sets
        DatabaseReference total_sales_ref=  FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("my_sales");
        DatabaseReference total_exp_ref=FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("my_expenses");
        total_exp_ref.keepSynced(true);
        total_sales_ref.keepSynced(true);
        ArrayList<BarEntry> entries = new ArrayList<>();
        total_exp_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot post:snapshot.getChildren()){
                    ExpenseInfo im=post.getValue(ExpenseInfo.class);
                    total_exp_amount+=Float.parseFloat(im.getExp_amount());
                }
                try
                {
                    if(total_exp_amount!=0){
                        entries.add(new BarEntry(4f, total_exp_amount));

                        expt.setText(String.valueOf(total_exp_amount));
                        expt.setTextColor(Color.WHITE);
                        expt.setBackgroundColor(Color.RED);
                    }else {

                    }

                BarDataSet dataset = new BarDataSet(entries, "Expenses");
                // Declare the bar chart and set it as the view for this activity

                // Set data for the bar chart
                BarData data = new BarData(dataset);
                chart.setData(data);
                // make the x-axis fit exactly all bars
                chart.setFitBars(true);
                // Set description
                Description description = new Description();
                description.setText("Sales");
                chart.setDescription(description);
                // Set themes
                dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                // Animate
                chart.animateY(5000);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        total_sales_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot post:snapshot.getChildren()){
                    Rabbit_Products im=post.getValue(Rabbit_Products.class);
                    total_sls_amount+=im.getAmount();
                }
                try
                {
                    if(total_sls_amount!=0){
                        entries.add(new BarEntry(8f, total_sls_amount));
                        t.setText(String.valueOf(total_sls_amount));
                        t.setBackgroundColor(Color.BLUE);
                        t.setTextColor(Color.WHITE);
                    }else {

                    }

                BarDataSet dataset = new BarDataSet(entries, "Expenses");
                // Declare the bar chart and set it as the view for this activity

                // Set data for the bar chart
                BarData data = new BarData(dataset);
                chart.setData(data);
                // make the x-axis fit exactly all bars
                chart.setFitBars(true);
                // Set description
                Description description = new Description();
                description.setText("Sales");
                chart.setDescription(description);
                // Set themes
                dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                // Animate
                chart.animateY(5000);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),d.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
        expt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),expt.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
        if(expt.getText().toString().trim()!=null && t.getText().toString().trim()!=null){


            d.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),d.getText().toString(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float s=Float.parseFloat(t.getText().toString())-Float.parseFloat(expt.getText().toString());
                if(s<0){
                    Toast.makeText(getApplicationContext(),String.valueOf(s)+" "+" as loss",Toast.LENGTH_SHORT).show();
                    d.setText("loss in progress");
                    d.setTextColor(Color.RED);
                }else if(s==0){
                    d.setText("Break Even");
                    d.setTextColor(Color.YELLOW);
                }else if(s>0){
                    d.setTextColor(Color.GREEN);
                    d.setText("Profit in progress");
                }
                else {
                    //Toast.makeText(getApplicationContext(),String.valueOf(s)+"profit",Toast.LENGTH_SHORT).show();
                    d.setTextColor(Color.BLUE);
                    d.setText("No data to compute");
                }
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {


                        System.out.println("Permissions --> " + "Permission Granted: " + permissions[i]);
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        System.out.println("Permissions --> " + "Permission Denied: " + permissions[i]);
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
    @Override
    public void onBackPressed() {
        //Checks if the navigation drawer is open -- If so, close it
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        // If drawer is already close -- Do not override original functionality
        else {

            androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(Dahboard.this)
//set icon
                    .setIcon(R.drawable.app_log)
//set title
                    .setTitle("Are you sure, you want to Exit")
//set message
                    .setMessage("Exiting.....")
//set positive button
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            finish();
                        }
                    })
//set negative button
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //set what should happen when negative button is clicked
                            //Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                        }
                    })
                    .show();
        }
    }

    /**
     * Checks if the navigation drawer is open - if so, close it
     */
    private void closeDrawer(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    /**
     * Iterates through all the items in the navigation menu and deselects them:
     * removes the selection color
     */
    private void deSelectCheckedState(){
        int noOfItems = navigationView.getMenu().size();
        for (int i=0; i<noOfItems;i++){
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    //hidding graps
    private void showBar(){
        chart.setVisibility(View.VISIBLE);
        SalespieChart.setVisibility(View.GONE);
        ExpensesPiechart.setVisibility(View.GONE);
    }
    private void showSalesPie(){
        SalespieChart.setVisibility(View.VISIBLE);
        chart.setVisibility(View.GONE);
        ExpensesPiechart.setVisibility(View.GONE);
    }
    private void showExpPie(){
        ExpensesPiechart.setVisibility(View.VISIBLE);
        SalespieChart.setVisibility(View.GONE);
        chart.setVisibility(View.GONE);
    }

    private void showBunnyDOBnotification(String title, String body,int bunnies,int alive,int dead,String DOB,String doe,String buck,String push_key){
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.app_log)
                        .setColor(Color.BLUE)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true);

        Intent notificationIntent = new Intent(this, BunnyNotificationUpdate.class);
        //data to send for update
        notificationIntent.putExtra("1",String.valueOf(bunnies));
        notificationIntent.putExtra("2",String.valueOf(alive));
        notificationIntent.putExtra("3",String.valueOf(dead));
        notificationIntent.putExtra("4",DOB);
        notificationIntent.putExtra("5",doe);
        notificationIntent.putExtra("6",buck);
        notificationIntent.putExtra("7",push_key);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //sound
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        manager.notify(999, builder.build());

    }
    private void showBunnyDOBPrepNotification(String title, String body){
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.app_log)
                        .setColor(Color.BLUE)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true);

       // Intent notificationIntent = new Intent(this, Bunnies.class);
        //PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
          //      PendingIntent.FLAG_UPDATE_CURRENT);
        //builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //sound
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        manager.notify(999, builder.build());
    }

}