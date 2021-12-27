package com.example.rfarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rfarm.constructors.ExpenseInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Expenses extends AppCompatActivity {
   private TextInputEditText exp_Cat,exp_Date,exp_Amount,exp_Type;
    private ProgressDialog mExpProgress;
   private Button save_Exp;
    private DatabaseReference mDatabase;
  private int day,month,year;
    //drawer feilds
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SwitchCompat darkModeSwitch;
    private TextView drawer_username,drawer_email;
    private ImageView drawer_pic;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        //drawer views
        toolbar = findViewById(R.id.toolbar_id);
        toolbar.setTitle(R.string.toolbar_title);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout_id);
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
                        startActivity(new Intent(Expenses.this,Doer_buck.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_chat_id:
                        startActivity(new Intent(Expenses.this,Bunnies.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_profile_id:
                        startActivity(new Intent(Expenses.this,Sales.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_notebooks_id:
                        startActivity(new Intent(Expenses.this,Expenses.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_send_id:
                        startActivity(new Intent(Expenses.this,Herd.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_share_id:
                        startActivity(new Intent(Expenses.this,Expenses_Records.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_photos_id:
                        startActivity(new Intent(Expenses.this,BreedingRabbits.class));
                        // closeDrawer();
                        finish();
                        break;
                    case R.id.nav_trash_id:
                        startActivity(new Intent(Expenses.this,BreedingHistory.class));
                        // closeDrawer();
                        finish();
                        break;
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(),Dahboard.class));
                        //closeDrawer();
                        finish();
                        break;

                    case R.id.nav_settings_id:
                        startActivity(new Intent(Expenses.this,SalesRecords.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_signout_id:



                        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(Expenses.this)
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
                                        startActivity(new Intent(Expenses.this,Home.class));
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




        //getting views
        mExpProgress = new ProgressDialog(this);
        save_Exp=findViewById(R.id.exp_btn_save);
        exp_Cat=findViewById(R.id.exp_category);
        exp_Amount=findViewById(R.id.exp_amount);
        exp_Date=findViewById(R.id.exp_date);
        exp_Type=findViewById(R.id.exp_type);
        //getting string values from fields
        //expense object
        exp_Cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Expenses.this, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.expense_category, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.medical:
                                exp_Cat.setText(item.getTitle());
                                Drawable img =getApplicationContext().getResources().getDrawable(R.drawable.medical);
                                img.setBounds(0, 0, 30, 30);
                                exp_Cat.setCompoundDrawables(img, null, null, null);
                            break;
                            case R.id.housing:
                                exp_Cat.setText(item.getTitle());
                                Drawable img1 =getApplicationContext().getResources().getDrawable(R.drawable.housing);
                                img1.setBounds(0, 0, 30, 30);
                                exp_Cat.setCompoundDrawables(img1, null, null, null);
                                break;
                            case R.id.feeds:
                                exp_Cat.setText(item.getTitle());
                                Drawable img2 =getApplicationContext().getResources().getDrawable(R.drawable.grass);
                                img2.setBounds(0, 0, 30, 30);
                                exp_Cat.setCompoundDrawables(img2, null, null, null);
                                break;
                            case R.id.otherss:
                                exp_Cat.setText(item.getTitle());
                                Drawable img3 =getApplicationContext().getResources().getDrawable(R.drawable.others);
                                img3.setBounds(0, 0, 30, 30);
                                exp_Cat.setCompoundDrawables(img3, null, null, null);   break;
                            default:
                                return false;

                        }
                        return true;
                    }
                });
            }
        });
      exp_Date.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              showDialog(0);
          }
      });

        save_Exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date=exp_Date.getText().toString();
                String amount=exp_Amount.getText().toString();
                String expType=exp_Type.getText().toString();
                String ExpCategory=exp_Cat.getText().toString();
                //check if fields are not empty
                if(!TextUtils.isEmpty(date) && !TextUtils.isEmpty(amount) && !TextUtils.isEmpty(expType) && !TextUtils.isEmpty(ExpCategory)){


                    AlertDialog alertDialog = new AlertDialog.Builder(Expenses.this)
//set icon
                            .setIcon(R.mipmap.app_log)
//set title
                            .setTitle("Expense")
//set message
                            .setMessage("Save expense?")
//set positive button
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mExpProgress.setTitle("Adding'" +expType+"worth"+"#"+amount + "'");
                                    mExpProgress.setMessage("Please wait while we add your Expense Details!");
                                    mExpProgress.setCanceledOnTouchOutside(false);
                                    mExpProgress.show();
                                    //save expense data
                                    save_expense(ExpCategory,expType,date,amount);
                                    exp_Amount.setText(" ");
                                    exp_Cat.setText(" ");
                                    exp_Date.setText(" ");
                                    exp_Type.setText(" ");
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



                }else if(TextUtils.isEmpty(exp_Cat.getText().toString())){
                    exp_Cat.setError("Click to select expense category{feeds,medication,housing...}");
                    exp_Cat.requestFocus();
                }else if(TextUtils.isEmpty(expType)) {
                    exp_Type.setError("Enter expense type{maize mash,pellets,vitamins...}");
                    exp_Type.requestFocus();
                    //Toast.makeText(getApplicationContext(),"fields empty",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(exp_Date.getText().toString())){
                    exp_Date.setError("click to set date");
                    exp_Date.requestFocus();
                }
                else if(TextUtils.isEmpty(amount)){
                    exp_Amount.setError("Enter expense amount");
                    exp_Amount.requestFocus();
                }

            }
        });



    }




    private void save_expense(String expCategory, String expType, String date, String amount) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("my_expenses");
        mDatabase.keepSynced(true);
        //save and close progress dialog

        mDatabase.push().setValue(new ExpenseInfo(expCategory,date,amount,expType)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    mExpProgress.dismiss();
                }else {
                    Toast.makeText(getApplicationContext(),"Error in saving"+expType+" Expense!",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {

        return new DatePickerDialog(this, datePickerListener, year, month, day);
    }


    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            day = selectedDay;
            month = selectedMonth;
            year = selectedYear;
            exp_Date.setText(selectedDay + "-" + (selectedMonth + 1) + "-"
                    + selectedYear);

        }


    };
    @Override
    public void onBackPressed() {
        //Checks if the navigation drawer is open -- If so, close it
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        // If drawer is already close -- Do not override original functionality
        else {

            androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(Expenses.this)
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