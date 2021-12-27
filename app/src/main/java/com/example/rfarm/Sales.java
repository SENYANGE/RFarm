package com.example.rfarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rfarm.constructors.Rabbit;
import com.example.rfarm.constructors.Rabbit_Products;
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
import java.util.Date;
import java.util.Locale;

public class Sales extends AppCompatActivity {
    //drawer feilds
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FrameLayout frameLayout;
    private NavigationView navigationView;
    private SwitchCompat darkModeSwitch;
    private TextView drawer_username,drawer_email;
    private ImageView drawer_pic;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle toggle;

    private CheckBox sk,drp_urine,mt,cage;
    private TextInputEditText amount;
    private Button saveBtn,doe_buck,bunny;
    private DatabaseReference ref;
    private String dateSold;
    private String push_key;
    String prdct_type;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
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
                        startActivity(new Intent(Sales.this,Doer_buck.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_chat_id:
                        startActivity(new Intent(Sales.this,Bunnies.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_profile_id:
                        startActivity(new Intent(Sales.this,Sales.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_notebooks_id:
                        startActivity(new Intent(Sales.this,Expenses.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_send_id:
                        startActivity(new Intent(Sales.this,Herd.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_share_id:
                        startActivity(new Intent(Sales.this,Expenses_Records.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_photos_id:
                        startActivity(new Intent(Sales.this,BreedingRabbits.class));
                        // closeDrawer();
                        finish();
                        break;
                    case R.id.nav_trash_id:
                        startActivity(new Intent(Sales.this,BreedingHistory.class));
                        // closeDrawer();
                        finish();
                        break;
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(),Dahboard.class));
                        //closeDrawer();
                        finish();
                        break;

                    case R.id.nav_settings_id:
                        startActivity(new Intent(Sales.this,SalesRecords.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_signout_id:



                        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(Sales.this)
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
                                        startActivity(new Intent(Sales.this,Home.class));
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


        progressDialog=new ProgressDialog(this);
        amount=findViewById(R.id.other_sales_amount);
        sk=findViewById(R.id.skins);
        drp_urine=findViewById(R.id.fertilizers);
        mt=findViewById(R.id.meat);
        cage=findViewById(R.id.cages);
        saveBtn=findViewById(R.id.save_other_sales);
        doe_buck=findViewById(R.id.button2);
        bunny=findViewById(R.id.button3);
        doe_buck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Select Doe or Buck to Sell!",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),Herd.class));
            }
        });
        bunny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Select Bunny Birth group to sell From!",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),Bunnies.class));
            }
        });


        //my_sales node
        ref= FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("my_sales");
        dateSold= new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        ref.keepSynced(true);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sk.isChecked()==true){
                    prdct_type="skins";
                }else if(mt.isChecked()==true){
                    prdct_type="meat";
                }else if(cage.isChecked()==true){
                    prdct_type="cages";
                }else if(drp_urine.isChecked()==true){
                    prdct_type="droppings and urine";
                }
                if(!TextUtils.isEmpty(amount.getText().toString())&&(prdct_type!=null)&&(sk.isChecked()==true||drp_urine.isChecked()==true||mt.isChecked()==true||cage.isChecked()==true)){
                 push_key=ref.push().getKey();

                    AlertDialog alertDialog = new AlertDialog.Builder(Sales.this)
//set icon
                            .setIcon(R.mipmap.app_log)
//set title
                            .setTitle("Sales")
//set message
                            .setMessage("Are you sure?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    progressDialog.setTitle("Selling "+prdct_type+"worth: "+amount.getText().toString()+"/=");
                                    progressDialog.setMessage("Processing....");
                                    progressDialog.setCanceledOnTouchOutside(false);
                                    progressDialog.show();

                                    ref.child(push_key).setValue(new Rabbit_Products("other_sales",Integer.parseInt(amount.getText().toString()),1,dateSold,true,prdct_type,"default")).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(),"successful",Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                            amount.setText("");
                                            sk.setChecked(false);
                                            mt.setChecked(false);
                                            cage.setChecked(false);
                                            drp_urine.setChecked(false);
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


                }else if(sk.isChecked()==false&&mt.isChecked()==false&&cage.isChecked()==false&&drp_urine.isChecked()==false){
                    Toast.makeText(getApplicationContext(),"Select Sales Category",Toast.LENGTH_LONG).show();
                    drp_urine.setError("Select Sales Category");
                }
                else if(TextUtils.isEmpty(amount.getText().toString())){
                    amount.setError("Enter amount");
                    amount.requestFocus();
                }else {
                    Toast.makeText(getApplicationContext(),"Error in fields",Toast.LENGTH_LONG).show();
                }


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
            androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(Sales.this)
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

    public void checkBoxAlt(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.fertilizers:
                if (checked) cage.setChecked(false);
                if (checked) mt.setChecked(false);
                if (checked) sk.setChecked(false);

                    // Put some meat on the sandwich
                else drp_urine.setChecked(true);
                break;
            case R.id.meat:
                if (checked) drp_urine.setChecked(false);
                if (checked) cage.setChecked(false);
                if (checked) sk.setChecked(false);
                else mt.setChecked(true);
                break;
            case R.id.skins:
                if (checked) drp_urine.setChecked(false);
                if (checked) cage.setChecked(false);
                if (checked) mt.setChecked(false);
                else sk.setChecked(true);
                break;
            case R.id.cages:
                if (checked) drp_urine.setChecked(false);
                if (checked) mt.setChecked(false);
                if (checked) sk.setChecked(false);
                else cage.setChecked(true);
                break;
            // TODO: Veggie sandwich
        }
    }
}