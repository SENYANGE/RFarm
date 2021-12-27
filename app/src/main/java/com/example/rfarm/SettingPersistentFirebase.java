package com.example.rfarm;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class SettingPersistentFirebase  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
