package com.example.homeapptest;

import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_CODE_SYSTEM_ALERT_WINDOW = 1131;
    private final int REQUEST_CODE_ENABLE_ADMIN = 11223;
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mDeviceAdminComponent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setDeviceAdmin();
        registerBroadcast();

        String defaultHome = getDefaultHomePackage(this);
        Log.d("logan", "Current default Home package: " + defaultHome);
        List<ResolveInfo> homeActivitiess = getHomeActivities(this);
        for (ResolveInfo resolveInfo : homeActivitiess) {
            String packageName = resolveInfo.activityInfo.packageName;
            String activityName = resolveInfo.activityInfo.name;
            Log.d("logan", "Package: " + packageName + ", Activity: " + activityName);
        }

    }

    private void setDeviceAdmin() {
        mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDeviceAdminComponent = new ComponentName(this, MyReceiver.class);

        // 註冊 Device Admin
        Button enableAdminButton = findViewById(R.id.enableAdminButton);
        enableAdminButton.setOnClickListener(view -> {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminComponent);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "This app needs admin rights to enable Lock Task Mode.");
            startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
        });

        Button startLockTaskButton = findViewById(R.id.EnableKioskMode);
        startLockTaskButton.setOnClickListener(view -> {
            SharedPrefHelper.setHome(this, true);

//            if (mDevicePolicyManager.isAdminActive(mDeviceAdminComponent)) {
//
//                Log.i("logan", "Device Admin 已啟用");
////                IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");
////                intentFilter.addCategory("android.intent.category.DEFAULT");
////                intentFilter.addCategory("android.intent.category.HOME");
////                mDevicePolicyManager.addPersistentPreferredActivity(mDeviceAdminComponent, intentFilter, new ComponentName(this, MainActivity.class));
////                mDevicePolicyManager.setLockTaskPackages(mDeviceAdminComponent, new String[]{getPackageName()});
//            } else {
//                Log.e("logan", "Device Admin 未啟用");
//            }
//
//            AuoLog.i("startLockTaskButton");
//            IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");
//            intentFilter.addCategory("android.intent.category.DEFAULT");
//            intentFilter.addCategory("android.intent.category.HOME");
//            mDevicePolicyManager.addPersistentPreferredActivity(mDeviceAdminComponent, intentFilter, new ComponentName(this, MainActivity.class));
//            if (mDevicePolicyManager.isAdminActive(mDeviceAdminComponent)) {
//
////                startLockTask();
//            } else {
//                AuoLog.i("mDevicePolicyManager is not Admin");
//            }
        });

        Button stopLockTaskButton = findViewById(R.id.DisableKioskMode);
        stopLockTaskButton.setOnClickListener(view -> {
            SharedPrefHelper.setHome(this, false);
            startActivity(new Intent(this, KioskActivity.class));
//            setRoleHolderAsUser(RoleManager.ROLE_HOME, "com.auodplus.adm.android.agent.driver", 0, Process.myUserHandle(), this);

            // 這裡可以移除admin
//            mDevicePolicyManager.removeActiveAdmin(mDeviceAdminComponent);

//            mDevicePolicyManager.clearPackagePersistentPreferredActivities(mDeviceAdminComponent, getPackageName());
//            finish();

        });

        Button callHomeSettings = findViewById(R.id.callHomeSettings);
        callHomeSettings.setOnClickListener(view -> {
            Toast.makeText(this, "callHomeSettings", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent("android.settings.HOME_SETTINGS");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 如果是在 Service 或 BroadcastReceiver 裡，記得加這個
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("logan", "onResume");
        // 檢查並請求權限
        PermissionHelper.checkAndRequestUsageAccess(this);
    }


    public String getDefaultHomePackage(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        ResolveInfo resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo != null && resolveInfo.activityInfo != null) {
            Log.d("logan", "######packageName: " + resolveInfo.activityInfo.packageName);
            Log.d("logan", "#######name: " + resolveInfo.activityInfo.name);
            return resolveInfo.activityInfo.packageName;
        }
        return "Unknown";
    }

    public List<ResolveInfo> getHomeActivities(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        return packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
    }

    private void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("logan", "android.intent.action.CLOSE_SYSTEM_DIALOGS");
        }
    };

    private void unRegister() {
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("logan", "onDestroy");
        unRegister();
    }
}