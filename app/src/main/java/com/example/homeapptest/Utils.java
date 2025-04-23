package com.example.homeapptest;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Utils {

    public static boolean isProcessOnTop(Context context) {
        String topPackageName = "";

        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        // We get usage stats for the last 10 seconds
        List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
        // Sort the stats by the last time used
        if (stats != null) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
            for (UsageStats usageStats : stats) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (mySortedMap != null && !mySortedMap.isEmpty())
                topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
        }
        Log.d("logan", "topPackageName:" + topPackageName);
        return topPackageName.equals(context.getPackageName());
    }

    public static boolean isProcessOnTopWithPackageName(Context context, String packageName) {
        String topPackageName = "";

        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        // We get usage stats for the last 10 seconds
        List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
        // Sort the stats by the last time used
        if (stats != null) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
            for (UsageStats usageStats : stats) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (mySortedMap != null && !mySortedMap.isEmpty())
                topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
        }
        return topPackageName.equals(context.getPackageName()) || topPackageName.equals(packageName);
    }

    public static String getTagContent(String tag, int index) {
        String[] id = tag.split(",");
        return id[index];
    }
}
