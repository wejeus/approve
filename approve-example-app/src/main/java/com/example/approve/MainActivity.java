package com.example.approve;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.isalldigital.approve.ApproveActivity;
import com.isalldigital.approve.ApproveRequestListener;
import com.isalldigital.approve.RuntimePermissions;
import com.isalldigital.approve.RuntimePermissionsRequest;


public class MainActivity extends ApproveActivity implements ApproveRequestListener {

    private RuntimePermissionsRequest startLocationRequest = new RuntimePermissionsRequest.Builder()
            .setRequestCode(200)
            .setRequestedPermission(RuntimePermissions.PERMISSION_LOCATION)
            .setShowPreRequestRationale("We need location for this awesome app!")
            .setShowPostDeniedRequestRationale("Denied!") // error must be attached to activity to get string
            .setShouldShowGotoSettingsOptionOnFail(true)
            .setRequestListener(this)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button requestPermissionButton = (Button) findViewById(R.id.button_request_permission);
        requestPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAppPermissions(startLocationRequest);
            }
        });
    }

    @Override
    public void onPermissionsGranted(int requestCode, String permission) {
        Toast.makeText(this, "Location Granted!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode, String permission) {
        Toast.makeText(this, "Location permission denied, do fallback!", Toast.LENGTH_LONG).show();
    }
}
