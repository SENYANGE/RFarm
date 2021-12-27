package com.example.rfarm;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetSequence;

public class Doer_buck extends Dahboard {
    private Uri picUri;
    //views
    FloatingActionButton rabImage;
    ImageView rabbit_image;
    TextInputEditText rabName,rabBreed;
    CheckBox buck,doer;
    //image
    private static final int GALLERY_PICK = 1;
    private static final int TAKE_PICTURE = 0;
    private DatabaseReference mDatabase;
    //ProgressDialog
    private ProgressDialog mRegProgress;
    private String sex;
    private String pushKey;
    private Uri filePath;
    //keep track of cropping intent
    final int PIC_CROP = 2;

    private final int PICK_IMAGE_REQUEST = 71;
    private   View view1;
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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doer_buck);

        mRegProgress = new ProgressDialog(this);
        rabImage=findViewById(R.id.rabbit_image);
        rabBreed=findViewById(R.id.rab_breed);
        rabbit_image=findViewById(R.id.doer_pic);
        rabName=findViewById(R.id.rab_tag);
        buck=findViewById(R.id.sex_male);
        doer=findViewById(R.id.sex_female);

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
                        startActivity(new Intent(Doer_buck.this,Doer_buck.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_chat_id:
                        startActivity(new Intent(Doer_buck.this,Bunnies.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_profile_id:
                        startActivity(new Intent(Doer_buck.this,Sales.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_notebooks_id:
                        startActivity(new Intent(Doer_buck.this,Expenses.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_send_id:
                        startActivity(new Intent(Doer_buck.this,Herd.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_share_id:
                        startActivity(new Intent(Doer_buck.this,Expenses_Records.class));
                        //closeDrawer();
                        finish();
                        break;
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(),Dahboard.class));
                        //closeDrawer();
                        finish();
                        break;

                    case R.id.nav_photos_id:
                        startActivity(new Intent(Doer_buck.this,BreedingRabbits.class));
                        // closeDrawer();
                        finish();
                        break;
                    case R.id.nav_trash_id:
                        startActivity(new Intent(Doer_buck.this,BreedingHistory.class));
                        // closeDrawer();
                        finish();
                        break;
                    case R.id.nav_settings_id:
                        startActivity(new Intent(Doer_buck.this,SalesRecords.class));
                        finish();
                        break;
                    case R.id.nav_signout_id:



                        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(Doer_buck.this)
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
                                        startActivity(new Intent(Doer_buck.this,Home.class));
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




        BitmapDrawable drawable=(BitmapDrawable)rabbit_image.getDrawable();

        findViewById(R.id.save_rabbit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String breed=rabBreed.getText().toString();
                String tag=rabName.getText().toString();
                if(buck.isChecked()){
                    sex="buck";
                }else {
                    sex="doe";
                }
                if(!TextUtils.isEmpty(breed) && !TextUtils.isEmpty(tag) && (buck.isChecked()==true || doer.isChecked()==true)&& !(rabbit_image.getDrawable().equals(R.drawable.bunny1))){


                    AlertDialog alertDialog = new AlertDialog.Builder(Doer_buck.this)
//set icon
                            .setIcon(R.mipmap.app_log)
//set title
                            .setTitle("Save Rabbit Details")
//set message
                            .setMessage("Are you sure?")
//set positive button
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mRegProgress.setTitle("Adding'" +tag + "'");
                                    mRegProgress.setMessage("Please wait while we add your Rabbit!");
                                    mRegProgress.setCanceledOnTouchOutside(false);
                                    mRegProgress.show();

                                    SaveRabbitDetails(tag,sex,breed);

                                }
                            })
//set negative button
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    mRegProgress.dismiss();
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
        rabImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               view1=getLayoutInflater().inflate(R.layout.bottom_sheet,null);
                BottomSheetDialog dialog=new BottomSheetDialog(Doer_buck.this);
                dialog.setContentView(view1);
                //getting views

                view1.findViewById(R.id.camera_select).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            //use standard intent to capture an image

                            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            //we will handle the returned data in onActivityResult
                            startActivityForResult(captureIntent, 0);
                        }catch(ActivityNotFoundException anfe){
                            //display an error message
                            String errorMessage = "Whoops - your device doesn't support capturing images!";
                            Toast toast = Toast.makeText(Doer_buck.this, errorMessage, Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    }
                });

                view1.findViewById(R.id.gallery_selection).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseImage(); }
                });
                dialog.show();
            }
        });


    }
    public void RFarmDemo(){
        new MaterialTapTargetSequence()
                .addPrompt(new MaterialTapTargetPrompt.Builder(Doer_buck.this)
                        .setTarget(findViewById(R.id.rabbit_image))
                        .setPrimaryText("Picture for Rabbit ")
                        .setSecondaryText("Click to select either Camera or Gallery Picture")
                        .setFocalPadding(Integer.parseInt("40dp"))
                        .setIcon(R.drawable.arrow_pointer)
                        .create(), 4000)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 0:
                    // Log.i("CameraCode",""+CAMERA_CODE);
                    Bundle bundle = data.getExtras();
                    Bitmap bmp = (Bitmap) bundle.get("data");
                    Bitmap resized = Bitmap.createScaledBitmap(bmp, 200, 200, true);
                    rabbit_image.setImageBitmap(resized);
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                   filePath =getImageUri(getApplicationContext(), resized);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                   // File finalFile = new File(getRealPathFromURI(filePath));
                    view1.setVisibility(View.GONE);
                    break;

                case 1:
                    filePath = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                        Bitmap resized1 = Bitmap.createScaledBitmap(bitmap, 200, 200, true);

                        rabbit_image.setImageBitmap(resized1);
                        view1.setVisibility(View.GONE);
                        filePath=getImageUri(getApplicationContext(),resized1);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                  //chooseImage();
                    view1.setVisibility(View.GONE);
                    break;
            }
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }
    private void performCrop(){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.theartofdev.edmodo.cropper.CropImageActivity");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);

        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
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

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.sex_male:
                if (checked) doer.setChecked(false);
                // Put some meat on the sandwich
                else doer.setChecked(true);
                break;
            case R.id.sex_female:
                if (checked) buck.setChecked(false);
                else buck.setChecked(true);
                break;
            // TODO: Veggie sandwich
        }

    }
    public void SaveRabbitDetails(String name,String sex,String breed){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("my_rabbits");
        //check if name of rabbit exists in my_rabbits
        pushKey=mDatabase.push().getKey();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

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
                                mRegProgress.dismiss();
                            }else {
                                Toast.makeText(getApplicationContext(),"Error in saving" +name +  "Try agian Later!",Toast.LENGTH_SHORT).show();
                            }}

                    });


                }


            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void uploadFile(Bitmap bitmap) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();

        final StorageReference ImagesRef = storageRef.child("rfarm").child("my_rabbits/"+random()+".jpg");


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        final UploadTask uploadTask = ImagesRef.putBytes(data);



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

                        return ImagesRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("my_rabbits");

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
            androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(Doer_buck.this)
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