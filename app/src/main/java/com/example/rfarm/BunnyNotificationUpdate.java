package com.example.rfarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class BunnyNotificationUpdate extends AppCompatActivity {
TextInputEditText alive,dead,total,dateOB,doe,buck;
DatabaseReference databaseReference;
    private ProgressDialog mExpProgress;
    Bundle extras ;
    int day,month,year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bunny_notification_update);
        //persistent

        mExpProgress = new ProgressDialog(this);
        databaseReference=FirebaseDatabase.getInstance().getReference().child("rfarm").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mating_records");

        extras= getIntent().getExtras();
        //views
        alive=findViewById(R.id.bunny_alive);
        dead=findViewById(R.id.bunny_dead);
        total=findViewById(R.id.bunny_total);
        dateOB=findViewById(R.id.DOB);
        doe=findViewById(R.id.mother_doe);
        buck=findViewById(R.id.father_buck);
        doe.setEnabled(false);
        buck.setEnabled(false);
        dead.setEnabled(false);
        //setting results
        doe.setText(extras.getString("5"));
        buck.setText(extras.getString("6"));
        dateOB.setText(extras.getString("4"));
        total.setText(extras.getString("1"));
        dead.setText(extras.getString("3"));
        alive.setText(extras.getString("2"));
        if(((Integer.parseInt(dead.getText().toString())+Integer.parseInt(alive.getText().toString()))==Integer.parseInt(total.getText().toString()))&&(Integer.parseInt(alive.getText().toString())<Integer.parseInt(total.getText().toString()))){
            dead.setText(String.valueOf((Integer.parseInt(total.getText().toString())-Integer.parseInt(alive.getText().toString()))));
        }



    }

    public void dateDialogPop(View view) {
        showDialog(0);
    }

    public void update_birth_details(View view) {
        //if fields not empty
        if(!(TextUtils.isEmpty(doe.getText().toString())&&TextUtils.isEmpty(buck.getText().toString())&&TextUtils.isEmpty(alive.getText().toString())&&TextUtils.isEmpty(dead.getText().toString())&&TextUtils.isEmpty(total.getText().toString()))){

            AlertDialog alertDialog = new AlertDialog.Builder(BunnyNotificationUpdate.this)
//set icon
                    .setIcon(R.mipmap.app_log)
//set title
                    .setTitle("Update Birth Details")
//set message
                    .setMessage("Are you sure?")
//set positive button
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mExpProgress.setTitle("Birth data");
                            mExpProgress.setMessage("Updating...");
                            mExpProgress.setCanceledOnTouchOutside(false);
                            mExpProgress.show();
                          //update data
                            if(Integer.parseInt(extras.getString("3"))>Integer.parseInt(extras.getString("2"))){
                                Toast.makeText(getApplicationContext(),"Dead Bunnies can't exceed Alive",Toast.LENGTH_SHORT).show();
                            }else if(Integer.parseInt(dead.getText().toString())>Integer.parseInt(alive.getText().toString())){
                                Toast.makeText(getApplicationContext(),"Dead Bunnies can't exceed Alive",Toast.LENGTH_SHORT).show();

                            }else if((Integer.parseInt(dead.getText().toString())+Integer.parseInt(alive.getText().toString()))>Integer.parseInt(total.getText().toString())){
                                Toast.makeText(getApplicationContext(),"Sum of Alive and Dead should not Exceed: "+total.getText().toString(),Toast.LENGTH_SHORT).show();

                            }else if((Integer.parseInt(dead.getText().toString())+Integer.parseInt(alive.getText().toString()))<Integer.parseInt(total.getText().toString())){
                                Toast.makeText(getApplicationContext(),"Sum of Alive and Dead should Equal to: "+total.getText().toString(),Toast.LENGTH_SHORT).show();

                            }else {
                                //update birth data
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        //update above details
                                        databaseReference.child(extras.getString("7")).child("number_of_bunnies").setValue(Integer.parseInt(total.getText().toString()));
                                        databaseReference.child(extras.getString("7")).child("n_bunnies_alive").setValue(Integer.parseInt(alive.getText().toString()));
                                        databaseReference.child(extras.getString("7")).child("n_bunnies_died").setValue(Integer.parseInt(dead.getText().toString()));
                                        databaseReference.child(extras.getString("7")).child("birth_date").setValue(dateOB.getText().toString());
                                        databaseReference.child(extras.getString("7")).child("buck_nm").setValue(buck.getText().toString());
                                        databaseReference.child(extras.getString("7")).child("doe_nm").setValue(doe.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                //dismiss dialog
                                                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();

                                                mExpProgress.dismiss();
                                                startActivity(new Intent(getApplicationContext(),Bunnies.class));
                                                finish();

                                            }
                                        });



                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                        //dismiss dialog
                                        Toast.makeText(getApplicationContext(),"Try again later",Toast.LENGTH_LONG).show();

                                        mExpProgress.dismiss();
                                        // startActivity(new Intent(getApplicationContext(),Bunnies.class));
                                        //finish();
                                    }
                                });

                            }

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
            dateOB.setText(selectedDay + "-" + (selectedMonth + 1) + "-"
                    + selectedYear);

        }


    };
}