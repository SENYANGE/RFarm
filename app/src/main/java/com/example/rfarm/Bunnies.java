package com.example.rfarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rfarm.adapters.HerdRecyclerViewAdapter;
import com.example.rfarm.adapters.bunniesAdapter;
import com.example.rfarm.adapters.doer_selectAdapter;
import com.example.rfarm.constructors.Rabbit;
import com.example.rfarm.constructors.Rabbit_Products;
import com.example.rfarm.constructors.mating_records;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
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

public class Bunnies extends AppCompatActivity {
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


    // Creating DatabaseReference.
    DatabaseReference databaseReference,sale_bunny_ref;

    // Creating RecyclerView.
    RecyclerView B_recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter_bunnies ;

    // Creating Progress dialog
    ProgressDialog progressDialog;
    int number_of_bunnies;
    String push_key;
    TextView avail_bunnies;

    // Creating List of ImageUploadInfo class.
    List<mating_records> list = new ArrayList<>();
    DividerItemDecoration itemDecorator;
  //  mating_records Mmating_records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bunnies);
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
                        startActivity(new Intent(Bunnies.this,Doer_buck.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_chat_id:
                        startActivity(new Intent(Bunnies.this,Bunnies.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(),Dahboard.class));
                        //closeDrawer();
                        finish();
                        break;

                    case R.id.nav_profile_id:
                        startActivity(new Intent(Bunnies.this,Sales.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_notebooks_id:
                        startActivity(new Intent(Bunnies.this,Expenses.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_send_id:
                        startActivity(new Intent(Bunnies.this,Herd.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_share_id:
                        startActivity(new Intent(Bunnies.this,Expenses_Records.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_photos_id:
                        startActivity(new Intent(Bunnies.this,BreedingRabbits.class));
                        // closeDrawer();
                        finish();
                        break;
                    case R.id.nav_trash_id:
                        startActivity(new Intent(Bunnies.this,BreedingHistory.class));
                        // closeDrawer();
                        finish();
                        break;
                    case R.id.nav_settings_id:
                        startActivity(new Intent(Bunnies.this,SalesRecords.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_signout_id:



                        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(Bunnies.this)
//set icon
                                .setIcon(R.drawable.app_log)
//set title
                                .setTitle("Are you sure to Exit")
//set message
                                .setMessage("Exiting will call finish() method")
//set positive button
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FirebaseAuth.getInstance().signOut();
                                        startActivity(new Intent(Bunnies.this,Home.class));
                                        finish();
                                    }
                                })
//set negative button
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //set what should happen when negative button is clicked
                                        Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
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




        // Assign id to RecyclerView.
        B_recyclerView = (RecyclerView) findViewById(R.id.bunny_recyclerView);

        // Setting RecyclerView size true.
        B_recyclerView.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        B_recyclerView.setLayoutManager(new LinearLayoutManager(Bunnies.this));

        // Assign activity this to progress dialog.
        progressDialog = new ProgressDialog(Bunnies.this);

        // Setting up message in Progress dialog.
        progressDialog.setMessage("Loading....");

        // Showing progress dialog.
        progressDialog.show();

        // Setting up Firebase image upload folder path in databaseReference.
        // The path is already defined in MainActivity.
        databaseReference = FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mating_records");
        sale_bunny_ref=FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("my_sales");

        //setting time_birth==true
        String dateNow= new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    mating_records imageUploadInfo = postSnapshot.getValue(mating_records.class);
                    if(databaseReference.child(postSnapshot.getKey()).child("birth_date").equals(dateNow)){
                        databaseReference.child(postSnapshot.getKey()).child("time_birth").setValue(true);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



        // Adding Add Value Event Listener to databaseReference if birth time is now
        Query ref=databaseReference.orderByChild("time_birth").equalTo(true);
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {


                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    mating_records imageUploadInfo = postSnapshot.getValue(mating_records.class);

                    list.add(imageUploadInfo);


                }

                adapter_bunnies = new bunniesAdapter(Bunnies.this, list, new bunniesAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(mating_records item) {
                        //dialog for selling
                        //Toast.makeText(getApplicationContext(),item.getDoer_nm(),Toast.LENGTH_SHORT).show();
                        LayoutInflater inflater=LayoutInflater.from(Bunnies.this);
                        final View DialogView=inflater.inflate(R.layout.bunny_sale,null);
                        final AlertDialog dialog=new AlertDialog.Builder(Bunnies.this).create();
                        dialog.setView(DialogView);
                        avail_bunnies=DialogView.findViewById(R.id.textView7);
                        avail_bunnies.setText(String.valueOf(item.getN_bunnies_alive()));
                        TextInputEditText no_bunnies=DialogView.findViewById(R.id.num_bunnies);
                        TextInputEditText amount=DialogView.findViewById(R.id.amount_bunny);
                        Button save=DialogView.findViewById(R.id.sale_bunny);
                        Button cancle=DialogView.findViewById(R.id.button4);
                        String dateSold= new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!TextUtils.isEmpty(no_bunnies.getText().toString())&& !TextUtils.isEmpty(amount.getText().toString())&&(Integer.parseInt(no_bunnies.getText().toString())<=Integer.parseInt(avail_bunnies.getText().toString()))){
                                    //save sales details
                                    push_key=sale_bunny_ref.push().getKey();
                                    if(Integer.parseInt(avail_bunnies.getText().toString())==0){
                                        Toast.makeText(getApplicationContext(),"No Bunnies available",Toast.LENGTH_LONG).show();
                                    }else  if(Integer.parseInt(no_bunnies.getText().toString())>Integer.parseInt(avail_bunnies.getText().toString())){
                                        Toast.makeText(getApplicationContext(),"Not Enough Bunnies available!",Toast.LENGTH_LONG).show();
                                    }else if(Integer.parseInt(amount.getText().toString())<1){amount.setError("Enter value above 1");
                                        amount.requestFocus();}
                                    else if(Integer.parseInt(no_bunnies.getText().toString())<1){
                                        no_bunnies.setError("Enter 1 and above");
                                        no_bunnies.requestFocus();
                                    }
                                    else {
                                        //are you sure alert
                                        AlertDialog alertDialog = new AlertDialog.Builder(Bunnies.this)
//set icon
                                                .setIcon(R.mipmap.app_log)
//set title
                                                .setTitle("Sales")
//set message
                                                .setMessage("Are you sure?")
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        progressDialog.setTitle("Sale "+no_bunnies.getText().toString()+"Bunnies"+"worth: "+amount.getText().toString()+"/= each");
                                                        progressDialog.setMessage("Processing....");
                                                        progressDialog.setCanceledOnTouchOutside(false);
                                                        progressDialog.show();

                                                        sale_bunny_ref.child(push_key).setValue(new Rabbit_Products("Bunny",(Integer.parseInt(amount.getText().toString())*(Integer.parseInt(no_bunnies.getText().toString()))),Integer.parseInt(no_bunnies.getText().toString()),dateSold,true,"bunny","default")).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                //update available bunnies
                                                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                                                                            mating_records imageUploadInfo = postSnapshot.getValue(mating_records.class);
                                                                            databaseReference.child(postSnapshot.getKey()).child("n_bunnies_alive").setValue(item.getN_bunnies_alive()-Integer.parseInt(no_bunnies.getText().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                    //clear fields
                                                                                    Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
                                                                                    dialog.dismiss();
                                                                                    startActivity(new Intent(getApplicationContext(),Bunnies.class));
                                                                                    finish();
                                                                                }
                                                                            });

                                                                            //  list.add(imageUploadInfo);


                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                                                        Toast.makeText(getApplicationContext(),"Operation failed",Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }
                                                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                //update breeding records
                                                              DatabaseReference  bunny_ref=FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mating_records");
                                                              bunny_ref.addValueEventListener(new ValueEventListener() {
                                                                  @Override
                                                                  public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                                      for(DataSnapshot postSnapShot: snapshot.getChildren()){
                                                                          push_key=postSnapShot.getKey();
                                                                          mating_records uplaod=postSnapShot.getValue(mating_records.class);
                                                                          bunny_ref.child(push_key).child("n_bunnies_sold").setValue(Integer.parseInt(no_bunnies.getText().toString()));
                                                                      }
                                                                  }

                                                                  @Override
                                                                  public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                  }
                                                              }) ;


                                                            }
                                                        });



                                                    }
                                                })
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        //set what should happen when negative button is clicked
                                                        Toast.makeText(getApplicationContext(),"Operation Canceled",Toast.LENGTH_LONG).show();
                                                    }
                                                })
                                                .show();

                                    }




                                }else if(TextUtils.isEmpty(no_bunnies.getText().toString())) {
                                no_bunnies.setError("Enter number of bunnies to sale");
                                no_bunnies.requestFocus();
                                }
                                else if(TextUtils.isEmpty(amount.getText().toString())){
                                    amount.setError("Enter amount per Bunny");
                                    amount.requestFocus();
                                }


                            }
                        });
                        cancle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();




                        list.clear();
                    }
                });
                itemDecorator = new DividerItemDecoration(Bunnies.this, DividerItemDecoration.VERTICAL);
                itemDecorator.setDrawable(ContextCompat.getDrawable(Bunnies.this, R.drawable.divider));

                B_recyclerView.addItemDecoration(itemDecorator);

                B_recyclerView.setAdapter(adapter_bunnies);


                // Hiding the progress dialog.
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // Hiding the progress dialog.
                progressDialog.dismiss();

            }
        });




    }
    @Override
    public void onBackPressed() {
        //Checks if the navigation drawer is open -- If so, close it
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        // If drawer is already close -- Do not override original functionality
        else {
            androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(Bunnies.this)
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
                            Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
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
}