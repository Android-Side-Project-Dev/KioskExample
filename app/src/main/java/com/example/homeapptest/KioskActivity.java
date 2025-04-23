package com.example.homeapptest;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.homeapptest.databinding.ActivityKioskBinding;

public class KioskActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("logan", "KioskActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiosk);
        checkStartActivity();
    }

    private void checkStartActivity() {
        if(SharedPrefHelper.isNeedSetHome(this)) {
            try {
                startActivity(new Intent(this, KioskActivity2.class));

            } catch (ActivityNotFoundException e) {
                Log.d("logan", "ActivityNotFoundException");
                Intent intent2 = new Intent("android.intent.action.MAIN");
                intent2.addCategory("android.intent.category.HOME");
                intent2.addCategory("android.intent.category.DEFAULT");
                String str = getPackageManager().queryIntentActivities(intent2, 0).get(0).activityInfo.packageName;
                intent2.setPackage(str);
                startActivity(intent2);
            } catch (Exception e2) {
            }
            finish();
        } else {
            Intent intent2 = new Intent("android.intent.action.MAIN");
            intent2.addCategory("android.intent.category.HOME");
            intent2.addCategory("android.intent.category.DEFAULT");
            String str = getPackageManager().queryIntentActivities(intent2, 0).get(0).activityInfo.packageName;
            intent2.setPackage(str);
            startActivity(intent2);
            finish();
        }
    }


    public DefaultHome getDefaultHomePackage(Context context) {
        Log.d("logan", "[getDefaultHomePackage]");
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        ResolveInfo resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo != null && resolveInfo.activityInfo != null) {
            Log.d("logan", "packageName: " + resolveInfo.activityInfo.packageName);
            Log.d("logan", "name: " + resolveInfo.activityInfo.name);
            return new DefaultHome(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
        }
        return null;
    }
}
