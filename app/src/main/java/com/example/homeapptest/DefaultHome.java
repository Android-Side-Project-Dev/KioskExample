package com.example.homeapptest;

public class DefaultHome{
    private String mPackageName;
    private String mActivityName;

    public DefaultHome(String packageName, String activityName) {
        mPackageName = packageName;
        mActivityName = activityName;
    }

    public String getActivityName() {
        return mActivityName;
    }

    public void setActivityName(String activityName) {
        mActivityName = activityName;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String packageName) {
        mPackageName = packageName;
    }
}