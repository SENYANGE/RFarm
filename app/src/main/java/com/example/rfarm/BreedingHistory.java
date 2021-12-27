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

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rfarm.adapters.BreedingHistoryAdapter;
import com.example.rfarm.adapters.SalesRecordAdapter;
import com.example.rfarm.constructors.Rabbit_Products;
import com.example.rfarm.constructors.mating_records;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BreedingHistory extends AppCompatActivity {
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

    RecyclerView history_recycler;
    RecyclerView.Adapter adapter ;
    ProgressDialog progressDialog;
    // Creating List of ImageUploadInfo class.
    List<mating_records> list = new ArrayList<>();
    DividerItemDecoration itemDecorator;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breeding_history);
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
                        startActivity(new Intent(BreedingHistory.this,Doer_buck.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_chat_id:
                        startActivity(new Intent(BreedingHistory.this,Bunnies.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_profile_id:
                        startActivity(new Intent(BreedingHistory.this,Sales.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_notebooks_id:
                        startActivity(new Intent(BreedingHistory.this,Expenses.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_send_id:
                        startActivity(new Intent(BreedingHistory.this,Herd.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_share_id:
                        startActivity(new Intent(BreedingHistory.this,Expenses_Records.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_photos_id:
                        startActivity(new Intent(BreedingHistory.this,BreedingRabbits.class));
                        // closeDrawer();
                        finish();
                        break;
                    case R.id.nav_trash_id:
                        startActivity(new Intent(BreedingHistory.this,BreedingHistory.class));
                        // closeDrawer();
                        finish();
                        break;
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(),Dahboard.class));
                        //closeDrawer();
                        finish();
                        break;

                    case R.id.nav_settings_id:
                        startActivity(new Intent(BreedingHistory.this,SalesRecords.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_signout_id:



                        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(BreedingHistory.this)
//set icon
                                .setIcon(R.drawable.app_log)
//set title
                                .setTitle("Are you sure to Exit")
//set message
                                .setMessage("Exiting!")
//set positive button
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FirebaseAuth.getInstance().signOut();
                                        startActivity(new Intent(BreedingHistory.this,Home.class));
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



        history_recycler=findViewById(R.id.brdH_recycler);
        progressDialog=new ProgressDialog(this);
        // Setting RecyclerView size true.
        history_recycler.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        history_recycler.setLayoutManager(new LinearLayoutManager(BreedingHistory.this));

        // Assign activity this to progress dialog.
        progressDialog = new ProgressDialog(BreedingHistory.this);

        // Setting up message in Progress dialog.
        progressDialog.setMessage("Loading....");

        // Showing progress dialog.
        progressDialog.show();

        ref = FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mating_records");
        ref.keepSynced(true);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot:snapshot.getChildren()){
                    mating_records mating=postSnapshot.getValue(mating_records.class);
                    list.add(mating);



                }
                adapter=new BreedingHistoryAdapter(BreedingHistory.this, list, new BreedingHistoryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(mating_records item) {
                        Toast.makeText(getApplicationContext(),"is it Birth time?: "+item.isTime_birth(),Toast.LENGTH_SHORT).show();
                    }
                });
                adapter.notifyDataSetChanged();
                itemDecorator = new DividerItemDecoration(BreedingHistory.this, DividerItemDecoration.VERTICAL);
                itemDecorator.setDrawable(ContextCompat.getDrawable(BreedingHistory.this, R.drawable.divider));

                history_recycler.addItemDecoration(itemDecorator);

                history_recycler.setAdapter(adapter);


                // Hiding the progress dialog.
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error occurred",Toast.LENGTH_SHORT).show();
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
            androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(BreedingHistory.this)
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