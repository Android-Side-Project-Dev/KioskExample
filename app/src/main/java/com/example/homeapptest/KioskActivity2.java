package com.example.homeapptest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KioskActivity2 extends Activity {
    private RecyclerView mAppRecyclerView;
    private List<AppInfoList> mAppWhiteList;
    private AppAdapter mAppAdapter;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("logan", "KioskActivity2 onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiosk_2);
        init();
    }

    private void init() {
        mHandler = new Handler();
        mHandler.post(mTopProcessCheck);

        mAppRecyclerView = findViewById(R.id.AppRecyclerView);
        mAppRecyclerView.setLayoutManager(new GridLayoutManager(this, 1)); // 3 欄
        mAppRecyclerView.setHasFixedSize(true);

        mAppWhiteList = loadLaunchableApps();

        mAppAdapter = new AppAdapter(this, mAppWhiteList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = (String) v.getTag();
                String pkgName = Utils.getTagContent(tag, 1);
                String className = Utils.getTagContent(tag, 2);

                boolean allowStart = false;
                String labelName = "";
                for (AppInfoList appInfoList : mAppWhiteList) {
                    if (appInfoList.getAppInfo().activityInfo.packageName.equals(pkgName)) {
                        labelName = appInfoList.getAppInfo().activityInfo.loadLabel(getPackageManager()).toString();

                        if (appInfoList.isCheck()) {
                            Intent launchIntent = new Intent(Intent.ACTION_MAIN);
                            launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                            launchIntent.setClassName(pkgName, className);
                            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(launchIntent);
                            allowStart = true;
                        }
                    }
                }

                if (!allowStart)
                    showNotWhitelistedDialog(labelName);

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = (String) v.getTag();
                CheckBox cb = (CheckBox) v;
                String index = Utils.getTagContent(tag, 0);
                String pkgName = Utils.getTagContent(tag, 1);
                if (mAppWhiteList != null) {
                    SharedPrefHelper.setAppChecked(getApplicationContext(), pkgName, cb.isChecked());
                    updateView(Integer.parseInt(index));
                }
            }
        });
        mAppRecyclerView.setAdapter(mAppAdapter);
    }

    private void updateView(int index) {
        mAppWhiteList = loadLaunchableApps();
        mAppAdapter.updateAppWhiteList(mAppWhiteList, index);
    }

//    private List<AppInfoList> loadLaunchableApps() {
//        PackageManager pm = getPackageManager();
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        List<ResolveInfo> launchableApps = pm.queryIntentActivities(intent, 0);
//
//        // 轉成 HashSet 提高查找效率
//        Set<String> whiteListApps = new HashSet<>(SharedPrefHelper.getAppCheckedList(getApplicationContext()));
//
//        List<AppInfoList> appInfoList = new ArrayList<>();
//        for (ResolveInfo info : launchableApps) {
//            String pkgName = info.activityInfo.packageName;
//            boolean isChecked = whiteListApps.contains(pkgName);
//            Log.d("logan", "pkgName:" + pkgName + ",isChecked:" + isChecked);
//            appInfoList.add(new AppInfoList(info, isChecked));
//        }
//
//        return appInfoList;
//    }


    private List<AppInfoList> loadLaunchableApps() {
        PackageManager pm = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> launchableApps = pm.queryIntentActivities(intent, 0);

        Set<String> whiteListApps = SharedPrefHelper.getAppCheckedList(getApplicationContext());
        if (whiteListApps == null) {
            whiteListApps = new HashSet<>();
        }

        List<AppInfoList> appInfoList = new ArrayList<>();
        for (ResolveInfo info : launchableApps) {
            String pkgName = info.activityInfo.packageName;
            boolean isChecked = whiteListApps.contains(pkgName);
            Log.d("logan", "pkgName:" + pkgName + ",isChecked:" + isChecked);
            appInfoList.add(new AppInfoList(info, isChecked));
        }

        return appInfoList;
    }



    private void showNotWhitelistedDialog(String appName) {
        new AlertDialog.Builder(this)
                .setTitle("無法啟動")
                .setMessage(appName + " 未在白名單中，無法開啟。")
                .setPositiveButton("確定", null)
                .show();
    }


    @Override
    public void onBackPressed() {
        Log.d("logan", "KioskActivity2 onBackPressed");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null)
            mHandler.removeCallbacks(mTopProcessCheck);
    }

    private final Runnable mTopProcessCheck = new Runnable() {
        @Override
        public void run() {
            boolean status = Utils.isProcessOnTop(getApplicationContext());
            mHandler.postDelayed(this, 5000);
        }
    };
}