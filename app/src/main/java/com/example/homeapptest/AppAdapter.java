package com.example.homeapptest;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {

    private Context mContext;
    private List<AppInfoList> mAppInfoLists;
    private PackageManager packageManager;
    private View.OnClickListener mAppItemClick;
    private View.OnClickListener mAppCheckClick;

    public AppAdapter(Context context, List<AppInfoList> appInfoLists, View.OnClickListener appClick, View.OnClickListener checkClick) {
        this.mContext = context;
        mAppInfoLists = appInfoLists;
        packageManager = context.getPackageManager();
        mAppItemClick = appClick;
        mAppCheckClick = checkClick;
    }

    public void updateAppWhiteList(List<AppInfoList> appInfoLists, int index) {
        mAppInfoLists = appInfoLists;
        notifyItemChanged(index);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelativeLayout layer = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false);
        CheckBox appCheck = layer.findViewById(R.id.AppCheckBox);
        LinearLayout appItem = layer.findViewById(R.id.AppItemLayer);
        TextView label = layer.findViewById(R.id.app_label);
        ImageView icon = layer.findViewById(R.id.app_icon);
        return new ViewHolder(layer, appItem, appCheck, label, icon, mAppCheckClick, mAppItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppInfoList info = mAppInfoLists.get(position);
        String label = info.getAppInfo().loadLabel(packageManager).toString();
        String pkgName = info.getAppInfo().activityInfo.packageName;
        String className = info.getAppInfo().activityInfo.name;
        Drawable icon = info.getAppInfo().loadIcon(packageManager);
        holder.mAppCheck.setTag(position + "," + pkgName);
        holder.mAppCheck.setChecked(info.isCheck());
        holder.mAppItem.setTag(position + "," + pkgName + "," + className);
        holder.mLable.setText(label);
        holder.mIcon.setImageDrawable(icon);
    }

    @Override
    public int getItemCount() {
        return mAppInfoLists != null ? mAppInfoLists.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mItemLayer;
        private LinearLayout mAppItem;
        private CheckBox mAppCheck;
        private ImageView mIcon;
        private TextView mLable;

        public ViewHolder(RelativeLayout layer, LinearLayout appItem, CheckBox appCheck, TextView label, ImageView icon,
                          View.OnClickListener checkClick, View.OnClickListener itemClick) {
            super(layer);
            mItemLayer = layer;
            mAppCheck = appCheck;
            mAppCheck.setOnClickListener(checkClick);
            mAppItem = appItem;
            mAppItem.setOnClickListener(itemClick);
            mLable = label;
            mIcon = icon;
        }
    }
}

