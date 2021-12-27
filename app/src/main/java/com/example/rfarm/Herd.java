package com.example.rfarm;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rfarm.adapters.HerdRecyclerViewAdapter;
import com.example.rfarm.constructors.Rabbit;
import com.example.rfarm.constructors.Rabbit_Products;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Herd extends AppCompatActivity {
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
    private FloatingActionButton rabImage;
    private TextInputEditText  rabName,rabBreed;
    private static final int TAKE_PICTURE = 0;
    private DatabaseReference mDatabase;
    //ProgressDialog
    private ProgressDialog mRegProgress;
    private String sex;
    private String pushKey;
    private Uri filePath;
    ImageView rabbit_image;

    private final int PICK_IMAGE_REQUEST = 71;
    // Creating DatabaseReference.
    DatabaseReference databaseReference;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter ;

    // Creating Progress dialog
    ProgressDialog progressDialog;

    // Creating List of ImageUploadInfo class.
    List<Rabbit> list = new ArrayList<>();
    DividerItemDecoration itemDecorator;
    String pusk_key;
    CheckBox doer,buck;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_herd);
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
                        startActivity(new Intent(Herd.this,Doer_buck.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_chat_id:
                        startActivity(new Intent(Herd.this,Bunnies.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(),Dahboard.class));
                        //closeDrawer();
                        finish();
                        break;

                    case R.id.nav_profile_id:
                        startActivity(new Intent(Herd.this,Sales.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_notebooks_id:
                        startActivity(new Intent(Herd.this,Expenses.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_send_id:
                        startActivity(new Intent(Herd.this,Herd.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_share_id:
                        startActivity(new Intent(Herd.this,Expenses_Records.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_photos_id:
                        startActivity(new Intent(Herd.this,BreedingRabbits.class));
                        // closeDrawer();
                        finish();
                        break;
                    case R.id.nav_trash_id:
                        startActivity(new Intent(Herd.this,BreedingHistory.class));
                        // closeDrawer();
                        finish();
                        break;
                    case R.id.nav_settings_id:
                        startActivity(new Intent(Herd.this,SalesRecords.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_signout_id:



                        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(Herd.this)
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
                                        startActivity(new Intent(Herd.this,Home.class));
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
        recyclerView = (RecyclerView) findViewById(R.id.herd_recyclerview);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(Herd.this));

        // Assign activity this to progress dialog.
        progressDialog = new ProgressDialog(Herd.this);

        // Setting up message in Progress dialog.
        progressDialog.setMessage("Loading....");

        // Showing progress dialog.
        progressDialog.show();

        // Setting up Firebase image upload folder path in databaseReference.
        // The path is already defined in MainActivity.
        databaseReference = FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("my_rabbits");
        databaseReference.keepSynced(true);

        // Adding Add Value Event Listener to databaseReference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    Rabbit imageUploadInfo = postSnapshot.getValue(Rabbit.class);

                    list.add(imageUploadInfo);
                }

                adapter = new HerdRecyclerViewAdapter(Herd.this, list, new HerdRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Rabbit item) {

                        LayoutInflater inflater=LayoutInflater.from(Herd.this);
                        final View DialogView=inflater.inflate(R.layout.her_op_dialog,null);
                        final AlertDialog dialog=new AlertDialog.Builder(Herd.this).create();
                        dialog.setView(DialogView);
                        TextView t=DialogView.findViewById(R.id.textView3);
                        t.setText(item.getRabbit_tag()+" Options");
                        DialogView.findViewById(R.id.herd_op_delete).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(),"Deleting...",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                AlertDialog alertDialog = new AlertDialog.Builder(Herd.this)
//set icon
                                        .setIcon(R.mipmap.app_log)
//set title
                                        .setTitle("Deleting..."+item.getRabbit_tag())
//set message
                                        .setMessage("Are you sure?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                progressDialog.setTitle("Removing "+item.getRabbit_tag()+" from Herd");
                                                progressDialog.setMessage("Deleting....");
                                                progressDialog.setCanceledOnTouchOutside(false);
                                                progressDialog.show();

                                                 //remove  rabbit from herd on delete
                                                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("my_rabbits");
                                                ref.keepSynced(true);
                                                Query query=ref.orderByChild("rabbit_tag").equalTo(item.getRabbit_tag());
                                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                                                            Rabbit imageUploadInfo = postSnapshot.getValue(Rabbit.class);
                                                            ref.child(postSnapshot.getKey()).removeValue();
                                                            progressDialog.dismiss();
                                                            startActivity(new Intent(getApplicationContext(),Herd.class));

                                                        }


                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                                    Toast.makeText(getApplicationContext(),"Error in deleting "+item.getRabbit_tag(),Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                    }
                                                });
                                                progressDialog.dismiss();



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
                        });
                        DialogView.findViewById(R.id.herd_op_sale).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(getApplicationContext(),"Selling...",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                //openning another alertdialog for amount

                                LayoutInflater inflater=LayoutInflater.from(Herd.this);
                                final View SaleDialogView=inflater.inflate(R.layout.amount_entry_sale,null);
                                final AlertDialog sale_dialog=new AlertDialog.Builder(Herd.this).create();
                                sale_dialog.setView(SaleDialogView);

                                TextInputEditText amount=SaleDialogView.findViewById(R.id.sale_amouint);
                                String date_now = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                                SaleDialogView.findViewById(R.id.procced_sale).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if((!TextUtils.isEmpty(amount.getText().toString()))){

                                            //are you sure dialog
                                            //dismiss sale alert
                                            sale_dialog.dismiss();
                                            //open confirm alert
                                            AlertDialog alertDialog = new AlertDialog.Builder(Herd.this)
//set icon
                                                    .setIcon(R.mipmap.app_log)
//set title
                                                    .setTitle("Sales")
//set message
                                                    .setMessage("Are you sure?")
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            progressDialog.setTitle("Selling "+item.getRabbit_tag()+"worth "+amount);
                                                            progressDialog.setMessage("Processing....");
                                                            progressDialog.setCanceledOnTouchOutside(false);
                                                            progressDialog.show();
                                                            //save expense data
                                                            DatabaseReference data_ref = FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("my_sales");
                                                            // Toast.makeText(getApplicationContext(),amount.getText(),Toast.LENGTH_SHORT).show();
                                                            //get push key
                                                            pusk_key=data_ref.push().getKey();
                                                            data_ref.keepSynced(true);
                                                            data_ref.child(pusk_key).setValue(new Rabbit_Products(item.getRabbit_tag(),Integer.parseInt(amount.getText().toString()),1,date_now,true,"mature_rabbit",item.getImage())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                                                                    //remove sold rabbit from herd if sold
                                                                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("my_rabbits");
                                                                    ref.keepSynced(true);
                                                                    Query query=ref.orderByChild("rabbit_tag").equalTo(item.getRabbit_tag());
                                                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                                                                                Rabbit imageUploadInfo = postSnapshot.getValue(Rabbit.class);
                                                                                ref.child(postSnapshot.getKey()).removeValue();


                                                                            }


                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                        }
                                                                    });
                                                                    progressDialog.dismiss();
                                                                }
                                                            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                   Toast.makeText(getApplicationContext(),"Deactivating "+item.getRabbit_tag()+"from Herd",Toast.LENGTH_SHORT).show();
                                                                        startActivity(new Intent(Herd.this,Herd.class));
                                                                        finish();

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


                                            // Toast.makeText(getApplicationContext(),amount.getText().toString(),Toast.LENGTH_SHORT).show();
                                            //sale_dialog.dismiss();

                                        }else if(TextUtils.isEmpty(amount.getText().toString())){
                                            amount.setError("Enter Amount");
                                            amount.requestFocus();
                                        }else{
                                            Toast.makeText(getApplicationContext(),"Select Product Type",Toast.LENGTH_SHORT).show();
                                        }



                                    }
                                });

                                  SaleDialogView.findViewById(R.id.cancle_sale).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        sale_dialog.dismiss();
                                    }
                                });
                                sale_dialog.show();


                            }
                        });
                        DialogView.findViewById(R.id.herd_op_history).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(),"Checking Birth history...",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                        DialogView.findViewById(R.id.herd_op_update).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(),"Updating...",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                                LayoutInflater inflater=LayoutInflater.from(Herd.this);
                                final View UpdateDialogView=inflater.inflate(R.layout.rabbit_update,null);
                                final AlertDialog Updatedialog=new AlertDialog.Builder(Herd.this).create();
                                Updatedialog.setView(UpdateDialogView);
                                rabImage=UpdateDialogView.findViewById(R.id.update_rabbit_image);
                                rabName=UpdateDialogView.findViewById(R.id.update_rab_tag);
                                rabBreed=UpdateDialogView.findViewById(R.id.update_rab_breed);
                                 buck=UpdateDialogView.findViewById(R.id.update_sex_male);
                                doer=UpdateDialogView.findViewById(R.id.update_sex_female);
                                rabbit_image=UpdateDialogView.findViewById(R.id.update_doer_pic);

                                UpdateDialogView.findViewById(R.id.update_cancle).setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       Updatedialog.dismiss();
                                   }
                               });
                                rabImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        chooseImage();
                                    }
                                });
                                //remove  rabbit from herd on delete
                                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("my_rabbits");
                                ref.keepSynced(true);
                                Query query=ref.orderByChild("rabbit_tag").equalTo(item.getRabbit_tag());
                                UpdateDialogView.findViewById(R.id.update_save_rabbit).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String breed=rabBreed.getText().toString();
                                        String tag=rabName.getText().toString();
                                        if(buck.isChecked()){
                                            doer.setChecked(false);
                                            sex="buck";
                                        }else {
                                            buck.setChecked(false);
                                            sex="doer";
                                        }
                                        if(!TextUtils.isEmpty(breed) && !TextUtils.isEmpty(tag) && (buck.isChecked()==true || doer.isChecked()==true)&& !(rabbit_image.getDrawable().equals(R.drawable.bunny1))){


                                            androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(Herd.this)
//set icon
                                                    .setIcon(R.mipmap.app_log)
//set title
                                                    .setTitle("Update Rabbit Details")
//set message
                                                    .setMessage("Are you sure?")
//set positive button
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            progressDialog.setTitle("Updating..." +item.getRabbit_tag());
                                                            progressDialog.setMessage("Please wait while we Update your Rabbit!");
                                                            progressDialog.setCanceledOnTouchOutside(false);
                                                            progressDialog.show();

                                                            UpdateRabbitDetails(tag,sex,breed);
                                                            progressDialog.dismiss();

                                                        }
                                                    })
//set negative button
                                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {

                                                            progressDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else if(rabbit_image.getDrawable().equals(R.drawable.bunny1)) {
                                            Toast.makeText(getApplicationContext(),"Take picture or Select from gallery",Toast.LENGTH_SHORT).show();
                                        }
                                        else if(TextUtils.isEmpty(tag)) {
                                            rabName.setError("Field empty");
                                            rabName.requestFocus();
                                        }
                                        else if(TextUtils.isEmpty(breed)){
                                            rabBreed.setError("Field empty");
                                            rabBreed.requestFocus();
                                        }
                                        else if(buck.isChecked()==false||doer.isChecked()==false) {
                                            Toast.makeText(getApplicationContext(),"Select gender",Toast.LENGTH_SHORT).show();
                                        }

                                        else {
                                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                                            Rabbit imageUploadInfo = postSnapshot.getValue(Rabbit.class);
                                            if(!imageUploadInfo.getImage().equals("default")) {

                                                //Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.default_avatar).into(mDisplayImage);

                                                Picasso.get().load(imageUploadInfo.getImage()).networkPolicy(NetworkPolicy.OFFLINE)
                                                        .placeholder(R.drawable.bunnies2).into(rabbit_image, new Callback() {
                                                    @Override
                                                    public void onSuccess() {

                                                    }

                                                    @Override
                                                    public void onError(Exception e) {
                                                        Picasso.get().load(imageUploadInfo.getImage()).placeholder(R.drawable.bunnies2).into(rabbit_image);

                                                    }


                                                });

                                            }
                                            rabName.setText(imageUploadInfo.getRabbit_tag());
                                            rabBreed.setText(imageUploadInfo.getBreed());
                                            if(imageUploadInfo.getSex().equals("doe")){
                                                doer.setChecked(true);
                                                buck.setChecked(false);
                                            }else if(imageUploadInfo.getSex().equals("buck")) {
                                                buck.setChecked(true);
                                                doer.setChecked(false);
                                            }

                                           // progressDialog.dismiss();
                                            //startActivity(new Intent(getApplicationContext(),Herd.class));

                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                        Toast.makeText(getApplicationContext(),"Error in deleting "+item.getRabbit_tag(),Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });

                                Updatedialog.show();
                            }
                        });
                        DialogView.findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
               adapter.notifyDataSetChanged();
                itemDecorator = new DividerItemDecoration(Herd.this, DividerItemDecoration.VERTICAL);
                itemDecorator.setDrawable(ContextCompat.getDrawable(Herd.this, R.drawable.divider));

                recyclerView.addItemDecoration(itemDecorator);
                recyclerView.setAdapter(adapter);

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
    public void UpdateRabbitDetails(String name,String sex,String breed){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("my_rabbits");
        //check if name of rabbit exists in my_rabbits
        mDatabase.keepSynced(true);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    Rabbit imageUploadInfo = postSnapshot.getValue(Rabbit.class);
                    pushKey=postSnapshot.getKey();
                    //if rabbit name is new
                    //getting details from view
                    HashMap<String, String> RabbitDetailsMap = new HashMap<>();
                    RabbitDetailsMap.put("image", "default");
                    RabbitDetailsMap.put("rabbit_tag", name);
                    RabbitDetailsMap.put("sold", "false");
                    RabbitDetailsMap.put("alive","true");
                    RabbitDetailsMap.put("breed",breed);
                    RabbitDetailsMap.put("sex",sex);
                    uploadImage();
                    mDatabase.child(pushKey).setValue(RabbitDetailsMap).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //save image ur
                                rabbit_image.setImageResource(R.drawable.bunny1);
                                rabBreed.setText(" ");
                                rabName.setText(" ");
                                buck.setChecked(false);
                                doer.setChecked(false);
                                progressDialog.dismiss();
                            }else {
                                Toast.makeText(getApplicationContext(),"Error in saving" +name +  "Try agian Later!",Toast.LENGTH_SHORT).show();
                            }}

                    });

                }



            }


            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
    public void onSexCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.update_sex_male:
                if (checked) doer.setChecked(false);
                    // Put some meat on the sandwich
                else doer.setChecked(true);
                break;
            case R.id.update_sex_female:
                if (checked) buck.setChecked(false);
                else buck.setChecked(true);
                break;
            // TODO: Veggie sandwich
        }

    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                rabbit_image.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private void uploadImage() {


        if(filePath != null)
        {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference storageRef = storage.getReference();

            final StorageReference ref = storageRef.child("rfarm").child("my_rabbits/"+random()+".jpg");


            // StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            final UploadTask uploadTask = ref.putFile(filePath);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.i("whatTheFuck:",exception.toString());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.i("problem", task.getException().toString());
                            }

                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {

                                Uri downloadUri = task.getResult();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("my_rabbits");
                                ref.keepSynced(true);
                                Log.i("seeThisUri", downloadUri.toString());// This is the one you should store

                                ref.child(pushKey).child("image").setValue(downloadUri.toString());


                            } else {
                                Log.i("wentWrong","downloadUri failure");
                            }
                        }
                    });
                }
            });
        }
    }
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(20);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
    @Override
    public void onBackPressed() {
        //Checks if the navigation drawer is open -- If so, close it
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        // If drawer is already close -- Do not override original functionality
        else {
            androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(Herd.this)
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