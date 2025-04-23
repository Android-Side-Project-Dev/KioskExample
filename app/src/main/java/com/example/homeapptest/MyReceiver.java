package com.example.homeapptest;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class MyReceiver extends DeviceAdminReceiver {

    @Override
    public void onEnabled(@NonNull Context context, @NonNull Intent intent) {
        super.onEnabled(context, intent);
        Log.i("logan","onEnabled~~~~~~~~~~");
        Toast.makeText(context, "Device Admin Enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisabled(@NonNull Context context, @NonNull Intent intent) {
        super.onDisabled(context, intent);
        Log.i("logan","onDisabled~~~~~~~~~~");
        Toast.makeText(context, "Device Admin Disabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        Log.i("logan","onReceive~~~~~~~~~~");
    }
}