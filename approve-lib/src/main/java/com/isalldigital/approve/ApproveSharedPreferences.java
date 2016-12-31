package com.isalldigital.approve;


import android.content.Context;

public class ApproveSharedPreferences {

    public static final String PACKAGE_NAME = "com.isalldigital.approve";

    private static ApproveSharedPreferences instance;

    private Context context;

    private ApproveSharedPreferences(Context context) {
        this.context = context;
    }

    public static synchronized ApproveSharedPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new ApproveSharedPreferences(context.getApplicationContext());
        }
        return instance;
    }

    public boolean get(String key) {
        return context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE)
                .getBoolean(key, false);
    }

    public void set(String key, boolean value) {
        context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key, value)
                .commit();
    }
}
