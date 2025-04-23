package com.example.homeapptest;

import android.content.pm.ResolveInfo;

public class AppInfoList {
    private ResolveInfo mAppInfo;
    private boolean mIsCheck;

    public AppInfoList(ResolveInfo appInfo, boolean isCheck) {
        mAppInfo = appInfo;
        mIsCheck = isCheck;
    }

    public ResolveInfo getAppInfo() {
        return mAppInfo;
    }

    public void setAppInfo(ResolveInfo appInfo) {
        mAppInfo = appInfo;
    }

    public boolean isCheck() {
        return mIsCheck;
    }

    public void setCheck(boolean check) {
        mIsCheck = check;
    }
}
