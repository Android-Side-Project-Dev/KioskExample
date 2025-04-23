package com.example.homeapptest;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class PermissionHelper {

    // 檢查使用情況存取權限
    public static boolean isUsageAccessGranted(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mode = appOps.unsafeCheckOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(),
                    context.getPackageName()
            );
        } else {
            mode = appOps.checkOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(),
                    context.getPackageName()
            );
        }

        return mode == AppOpsManager.MODE_ALLOWED;
    }

    // 跳轉到「使用情況存取權限」設定畫面
    public static void requestUsageAccess(final AppCompatActivity activity) {
        // 檢查是否已經有權限
        if (isUsageAccessGranted(activity)) {
            return;
        }

        new AlertDialog.Builder(activity)
                .setTitle("需要權限")
                .setMessage("此功能需要您開啟「使用情況存取」權限，請前往設定開啟。")
                .setPositiveButton("前往設定", (dialog, which) -> {
                    // 跳轉到設定頁面
                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    intent.setData(Uri.parse("package:" + activity.getPackageName()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                })
                .setNegativeButton("取消", null)
                .show();
    }

    // 檢查權限是否開啟，並且在回到頁面時再次確認
    public static void checkAndRequestUsageAccess(AppCompatActivity activity) {
        if (!isUsageAccessGranted(activity)) {
            requestUsageAccess(activity);
        }
    }

}

