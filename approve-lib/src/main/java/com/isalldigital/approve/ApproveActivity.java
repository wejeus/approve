package com.isalldigital.approve;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public abstract class ApproveActivity extends AppCompatActivity {

    private ApproveActivityDelegate runtimePermissionsActivityDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runtimePermissionsActivityDelegate = new ApproveActivityDelegate(this);

    }

    public void requestAppPermissions(RuntimePermissionsRequest request) {
        runtimePermissionsActivityDelegate.requestAppPermissions(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        runtimePermissionsActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
