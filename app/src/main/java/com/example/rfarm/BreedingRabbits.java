package com.example.rfarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rfarm.adapters.HerdRecyclerViewAdapter;
import com.example.rfarm.adapters.doer_selectAdapter;
import com.example.rfarm.constructors.Rabbit;
import com.example.rfarm.constructors.mating_records;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BreedingRabbits extends AppCompatActivity {
    //drawer feilds
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FrameLayout frameLayout;
    private NavigationView navigationView;
    private SwitchCompat darkModeSwitch;
    private  TextView drawer_username,drawer_email;
    private ImageView drawer_pic;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle toggle;
    private Button doer_select,buck_select,save_m_d,tree_fam;
    private ImageView doer_img,buck_img;
    private RecyclerView  recyclerView_buck,recyclerView_doer;
    private ProgressDialog progressDialog;
    private String pushKey;
    private ProgressDialog mRegProgress;
    private String doe_img_url;

    // Creating DatabaseReference.
    private DatabaseReference databaseReference;
    private TextView doer_name,buck_name,doer_breed,buck_breed,doer_rating,buck_rating;

    // Creating RecyclerView.Adapter.
     RecyclerView.Adapter adapter_doer,adapter_buck;
    // Creating List of ImageUploadInfo class.
    private List<Rabbit> list_doer = new ArrayList<>();
    private List<Rabbit> list_buck = new ArrayList<>();
    private DividerItemDecoration itemDecorator;
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breeding_rabbits);
     //drawer views
     toolbar = findViewById(R.id.toolbar_id);
     toolbar.setTitle(R.string.toolbar_title);
     setSupportActionBar(toolbar);
     drawerLayout = findViewById(R.id.drawer_layout_id);
     frameLayout = findViewById(R.id.framelayout_id);
     navigationView = findViewById(R.id.navigationview_id);
     toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
     drawerLayout.addDrawerListener(toggle);
     toggle.syncState();
     //nav header elements
     View header = navigationView.getHeaderView(0);
     drawer_email=header.findViewById(R.id.nav_header_emailaddress_id);
     drawer_username=header.findViewById(R.id.nav_header_name_id);
     drawer_pic=header.findViewById(R.id.nav_header_circleimageview_id);
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
                     startActivity(new Intent(BreedingRabbits.this,Doer_buck.class));
                     //closeDrawer();
                     finish();
                     break;
                 case R.id.nav_chat_id:
                     startActivity(new Intent(BreedingRabbits.this,Bunnies.class));
                     //closeDrawer();
                     finish();
                     break;
                 case R.id.nav_profile_id:
                     startActivity(new Intent(BreedingRabbits.this,Sales.class));
                     //closeDrawer();
                     finish();
                     break;
                 case R.id.nav_notebooks_id:
                     startActivity(new Intent(BreedingRabbits.this,Expenses.class));
                     //closeDrawer();
                     finish();
                     break;
                 case R.id.nav_send_id:
                     startActivity(new Intent(BreedingRabbits.this,Herd.class));
                     //closeDrawer();
                     finish();
                     break;
                 case R.id.nav_home:
                     startActivity(new Intent(getApplicationContext(),Dahboard.class));
                     //closeDrawer();
                     finish();
                     break;

                 case R.id.nav_share_id:
                     startActivity(new Intent(BreedingRabbits.this,Expenses_Records.class));
                     //closeDrawer();
                     finish();
                     break;
                 case R.id.nav_photos_id:
                     startActivity(new Intent(BreedingRabbits.this,BreedingRabbits.class));
                     // closeDrawer();
                     finish();
                     break;
                 case R.id.nav_trash_id:
                     startActivity(new Intent(BreedingRabbits.this,BreedingHistory.class));
                     // closeDrawer();
                     finish();
                     break;
                 case R.id.nav_settings_id:
                     startActivity(new Intent(BreedingRabbits.this,SalesRecords.class));
                     //closeDrawer();
                     finish();
                     break;
                 case R.id.nav_signout_id:



                     androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(BreedingRabbits.this)
//set icon
                             .setIcon(R.drawable.app_log)
//set title
                             .setTitle("Are you sure to Exit?")
//set message
                             .setMessage("Exiting!")
//set positive button
                             .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialogInterface, int i) {
                                     FirebaseAuth.getInstance().signOut();
                                     startActivity(new Intent(BreedingRabbits.this,Home.class));
                                     finish();
                                 }
                             })
//set negative button
                             .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialogInterface, int i) {
                                     //set what should happen when negative button is clicked
                                    // Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
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
//drawer email and user name
     FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getUid()).child("profile").addValueEventListener(new ValueEventListener() {
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




     mRegProgress = new ProgressDialog(this);
        doer_img=findViewById(R.id.doerImage);
        buck_img=findViewById(R.id.buckImage);
        doer_select=findViewById(R.id.select_doer);
        buck_select=findViewById(R.id.select_buck);
        save_m_d=findViewById(R.id.save_mating);
        tree_fam=findViewById(R.id.family_both_breeds);
        doer_name=findViewById(R.id.doer_rabbit_name);
        doer_breed=findViewById(R.id.doer_breed_name);
        buck_breed=findViewById(R.id.buck_breed_name);
        buck_name=findViewById(R.id.buck_name);
        //selecting doer breed from database
        doer_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assign id to RecyclerView.
                View popupView = LayoutInflater.from(BreedingRabbits.this).inflate(R.layout.doer_dialog_select, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                recyclerView_doer =popupView.findViewById(R.id.doer_recyclerview);

                // Setting RecyclerView size true.
                recyclerView_doer.setHasFixedSize(true);

                // Setting RecyclerView layout as LinearLayout.
                recyclerView_doer.setLayoutManager(new LinearLayoutManager(BreedingRabbits.this));

                // Assign activity this to progress dialog.
                progressDialog = new ProgressDialog(BreedingRabbits.this);

                // Setting up message in Progress dialog.
                progressDialog.setMessage("Loading...");

                // Showing progress dialog.
                progressDialog.show();

                // Setting up Firebase image upload folder path in databaseReference.
                // The path is already defined in MainActivity.
                databaseReference = FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("my_rabbits");

                // Adding Add Value Event Listener to databaseReference.

                Query ref=databaseReference.orderByChild("sex").equalTo("doe");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                            Rabbit imageUploadInfo = postSnapshot.getValue(Rabbit.class);
                            if(imageUploadInfo.getSex().equalsIgnoreCase("doe"))
                            {
                                list_doer.add(imageUploadInfo);
                            }

                        }

                        adapter_doer = new doer_selectAdapter(BreedingRabbits.this, list_doer, new doer_selectAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Rabbit item) {
                                Toast.makeText(getApplicationContext(),item.getBreed(),Toast.LENGTH_SHORT).show();
                                Picasso.get().load(item.getImage()).placeholder(R.drawable.bunnies2).into(doer_img);
                                doer_name.setText(item.getRabbit_tag());
                                doer_breed.setText(item.getBreed());
                                doe_img_url=item.getImage();

                                adapter_doer.onDetachedFromRecyclerView(recyclerView_doer);
                                popupWindow.dismiss();
                                list_doer.clear();

                            }
                        });
                        itemDecorator = new DividerItemDecoration(BreedingRabbits.this, DividerItemDecoration.VERTICAL);
                        itemDecorator.setDrawable(ContextCompat.getDrawable(BreedingRabbits.this, R.drawable.divider));

                        recyclerView_doer.addItemDecoration(itemDecorator);

                        recyclerView_doer.setAdapter(adapter_doer);


                        // Hiding the progress dialog.
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        // Hiding the progress dialog.
                        progressDialog.dismiss();

                    }
                });

                popupWindow.showAsDropDown(popupView, 0, 0);

            }
        });
        doer_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assign id to RecyclerView.
                View popupView = LayoutInflater.from(BreedingRabbits.this).inflate(R.layout.doer_dialog_select, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                recyclerView_doer =popupView.findViewById(R.id.doer_recyclerview);

                // Setting RecyclerView size true.
                recyclerView_doer.setHasFixedSize(true);

                // Setting RecyclerView layout as LinearLayout.
                recyclerView_doer.setLayoutManager(new LinearLayoutManager(BreedingRabbits.this));

                // Assign activity this to progress dialog.
                progressDialog = new ProgressDialog(BreedingRabbits.this);

                // Setting up message in Progress dialog.
                progressDialog.setMessage("Loading...");

                // Showing progress dialog.
                progressDialog.show();

                // Setting up Firebase image upload folder path in databaseReference.
                // The path is already defined in MainActivity.
                databaseReference = FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("my_rabbits");

                // Adding Add Value Event Listener to databaseReference.

                Query ref=databaseReference.orderByChild("sex").equalTo("doe");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                            Rabbit imageUploadInfo = postSnapshot.getValue(Rabbit.class);
                            if(imageUploadInfo.getSex().equalsIgnoreCase("doe"))
                            {
                                list_doer.add(imageUploadInfo);
                            }

                        }

                        adapter_doer = new doer_selectAdapter(BreedingRabbits.this, list_doer, new doer_selectAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Rabbit item) {
                                Toast.makeText(getApplicationContext(),item.getBreed(),Toast.LENGTH_SHORT).show();
                                Picasso.get().load(item.getImage()).placeholder(R.drawable.bunnies2).into(doer_img);
                                doer_name.setText(item.getRabbit_tag());
                                doer_breed.setText(item.getBreed());
                                doe_img_url=item.getImage();

                                adapter_doer.onDetachedFromRecyclerView(recyclerView_doer);
                                popupWindow.dismiss();
                                list_doer.clear();

                            }
                        });
                        itemDecorator = new DividerItemDecoration(BreedingRabbits.this, DividerItemDecoration.VERTICAL);
                        itemDecorator.setDrawable(ContextCompat.getDrawable(BreedingRabbits.this, R.drawable.divider));

                        recyclerView_doer.addItemDecoration(itemDecorator);

                        recyclerView_doer.setAdapter(adapter_doer);


                        // Hiding the progress dialog.
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        // Hiding the progress dialog.
                        progressDialog.dismiss();

                    }
                });

                popupWindow.showAsDropDown(popupView, 0, 0);
            }
        });
        //selecting buck from database
        buck_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = LayoutInflater.from(BreedingRabbits.this).inflate(R.layout.buck_select_dialog, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                recyclerView_buck =popupView.findViewById(R.id.buck_recyclerview);

                // Setting RecyclerView size true.
                recyclerView_buck.setHasFixedSize(true);

                // Setting RecyclerView layout as LinearLayout.
                recyclerView_buck.setLayoutManager(new LinearLayoutManager(BreedingRabbits.this));

                // Assign activity this to progress dialog.
                progressDialog = new ProgressDialog(BreedingRabbits.this);

                // Setting up message in Progress dialog.
                progressDialog.setMessage("Loading...");

                // Showing progress dialog.
                progressDialog.show();

                // Setting up Firebase image upload folder path in databaseReference.
                // The path is already defined in MainActivity.
                databaseReference = FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("my_rabbits");


                // Adding Add Value Event Listener to databaseReference.
                Query ref=databaseReference.orderByChild("sex").equalTo("buck");
               ref.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {


                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                            Rabbit imageUploadInfo = postSnapshot.getValue(Rabbit.class);
                            if(imageUploadInfo.getSex().equalsIgnoreCase("buck"))
                            {
                                list_buck.add(imageUploadInfo);
                            }

                        }

                        adapter_buck = new doer_selectAdapter(BreedingRabbits.this, list_buck, new doer_selectAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Rabbit item) {
                                Toast.makeText(getApplicationContext(),item.getBreed(),Toast.LENGTH_SHORT).show();
                                Picasso.get().load(item.getImage()).placeholder(R.drawable.bunnies2).into(buck_img);
                                buck_name.setText(item.getRabbit_tag());
                                buck_breed.setText(item.getBreed());


                                popupWindow.dismiss();
                                list_buck.clear();
                            }
                        });
                        itemDecorator = new DividerItemDecoration(BreedingRabbits.this, DividerItemDecoration.VERTICAL);
                        itemDecorator.setDrawable(ContextCompat.getDrawable(BreedingRabbits.this, R.drawable.divider));

                        recyclerView_buck.addItemDecoration(itemDecorator);

                        recyclerView_buck.setAdapter(adapter_buck);


                        // Hiding the progress dialog.
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        // Hiding the progress dialog.
                        progressDialog.dismiss();

                    }
                });


                popupWindow.showAsDropDown(popupView, 0, 0);    }
        });
        buck_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = LayoutInflater.from(BreedingRabbits.this).inflate(R.layout.buck_select_dialog, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                recyclerView_buck =popupView.findViewById(R.id.buck_recyclerview);

                // Setting RecyclerView size true.
                recyclerView_buck.setHasFixedSize(true);

                // Setting RecyclerView layout as LinearLayout.
                recyclerView_buck.setLayoutManager(new LinearLayoutManager(BreedingRabbits.this));

                // Assign activity this to progress dialog.
                progressDialog = new ProgressDialog(BreedingRabbits.this);

                // Setting up message in Progress dialog.
                progressDialog.setMessage("Loading...");

                // Showing progress dialog.
                progressDialog.show();

                // Setting up Firebase image upload folder path in databaseReference.
                // The path is already defined in MainActivity.
                databaseReference = FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("my_rabbits");


                // Adding Add Value Event Listener to databaseReference.
                Query ref=databaseReference.orderByChild("sex").equalTo("buck");
                ref.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {


                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                            Rabbit imageUploadInfo = postSnapshot.getValue(Rabbit.class);
                            if(imageUploadInfo.getSex().equalsIgnoreCase("buck"))
                            {
                                list_buck.add(imageUploadInfo);
                            }

                        }

                        adapter_buck = new doer_selectAdapter(BreedingRabbits.this, list_buck, new doer_selectAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Rabbit item) {
                                Toast.makeText(getApplicationContext(),item.getBreed(),Toast.LENGTH_SHORT).show();
                                Picasso.get().load(item.getImage()).placeholder(R.drawable.bunnies2).into(buck_img);
                                buck_name.setText(item.getRabbit_tag());
                                buck_breed.setText(item.getBreed());


                                popupWindow.dismiss();
                                list_buck.clear();
                            }
                        });
                        itemDecorator = new DividerItemDecoration(BreedingRabbits.this, DividerItemDecoration.VERTICAL);
                        itemDecorator.setDrawable(ContextCompat.getDrawable(BreedingRabbits.this, R.drawable.divider));

                        recyclerView_buck.addItemDecoration(itemDecorator);

                        recyclerView_buck.setAdapter(adapter_buck);


                        // Hiding the progress dialog.
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        // Hiding the progress dialog.
                        progressDialog.dismiss();

                    }
                });


                popupWindow.showAsDropDown(popupView, 0, 0);
            }
        });
        //saving mating information
        save_m_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //Toast.makeText(getApplicationContext(), doer_name.getText(), Toast.LENGTH_SHORT).show();

                    String doer_nm = doer_name.getText().toString();
                    String buck_nm = buck_name.getText().toString();
                    String date_now = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    int number_of_bunnies = 0;
                    int n_bunnies_died = 0;
                    int n_bunnies_alive = 0;
                    String mating_tag = doer_nm + buck_nm, BirthPrepDate;
                    boolean time_birth = false;

                    if(!TextUtils.isEmpty(buck_nm) || !TextUtils.isEmpty(doer_nm)){


                        //alertdialog
                        AlertDialog alertDialog = new AlertDialog.Builder(BreedingRabbits.this)
//set icon
                                .setIcon(R.mipmap.app_log)
//set title
                                .setTitle("Saving Breeding Records")
//set message
                                .setMessage("Are you sure?")
//set positive button
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        mRegProgress.setTitle("Adding mating record");
                                        mRegProgress.setMessage("Please wait while we add your Record!");
                                        mRegProgress.setCanceledOnTouchOutside(false);
                                        mRegProgress.show();

                                        databaseReference = FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mating_records");
                                        //check if name of rabbit exists in my_rabbits
                                        pushKey = databaseReference.push().getKey();

                                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                     mating_records mating=new mating_records(0,doer_nm, buck_nm, date_now, number_of_bunnies, n_bunnies_died, n_bunnies_alive, time_birth, expectedDateBirth(25),expectedDateBirth(30),doe_img_url);

                                                databaseReference.child(pushKey).setValue(mating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            mRegProgress.dismiss();
                                                        }else {
                                                            Toast.makeText(getApplicationContext(),"Error in Saving Mating Record",Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });
                                                doer_img.setImageResource(R.drawable.bunny1);
                                                buck_img.setImageResource(R.drawable.bunny1);
                                                doer_breed.setText("");
                                                doer_name.setText("");
                                                buck_breed.setText("");
                                                buck_name.setText("");

                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                            }
                                        });

                                    }
                                })
//set negative button
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        progressDialog.dismiss();

                                           }
                                })
                                .show();

                    }else if(TextUtils.isEmpty(doer_nm)){
                        doer_name.setError("Field empty");
                    }
                    else  if(TextUtils.isEmpty(buck_nm)){
                        buck_name.setError("Field empty");
                    }
                    else if(doer_img.getDrawable().equals(R.drawable.bunny1)){
                        Toast t=Toast.makeText(getApplicationContext(),"Select Doe to continue",Toast.LENGTH_SHORT);
                        t.setGravity(50,50,50);

                       t.show();
                    }
                    else if(buck_img.getDrawable().equals(R.drawable.bunny1)){
                    Toast t=Toast.makeText(getApplicationContext(),"Select Buck to continue",Toast.LENGTH_SHORT);
                    t.setGravity(50,50,50);

                    t.show();
                }else{
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    }

                }
        });
        //getting previous mating and breeding information
        tree_fam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Previous mating information",Toast.LENGTH_SHORT).show();
            }
        });
    }
    //returns birth place preparation date
public String expectedDateBirth(int d){
    String date_now = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
/**
    Date dt = new Date();
    Calendar c = Calendar.getInstance();
    c.setTime(dt);
    c.add(Calendar.DATE, 25);
    dt = c.getTime();
    return  dt.toString();**/
    //can take any date in current format
    SimpleDateFormat dateFormat = new SimpleDateFormat( "dd-MM-yyyy" );
    Calendar cal = Calendar.getInstance();
    try {
        cal.setTime( dateFormat.parse(date_now));
    } catch (ParseException e) {
        e.printStackTrace();
    }
    cal.add( Calendar.DATE, d);
    return dateFormat.format(cal.getTime());
}
    @Override
    public void onBackPressed() {
        //Checks if the navigation drawer is open -- If so, close it
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        // If drawer is already close -- Do not override original functionality
        else {
            androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(BreedingRabbits.this)
//set icon
                    .setIcon(R.drawable.app_log)
//set title
                    .setTitle("Are you sure, you want to Exit?")
//set message
                    .setMessage("Exiting!")
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
                           // Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
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
    private boolean IsConnected(){
        boolean connected=false;
        ConnectivityManager manager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()== NetworkInfo.State.CONNECTED||
        manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()== NetworkInfo.State.CONNECTED||
                manager.getNetworkInfo(ConnectivityManager.TYPE_VPN).getState()== NetworkInfo.State.CONNECTED){
            connected=true;
        }else {
            Toast.makeText(getApplicationContext(),"Check your Internet Connection and Try again!",Toast.LENGTH_SHORT).show();
            connected=false;
        }
        return connected;
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

}
