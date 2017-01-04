package com.isalldigital.approve;


import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;


public class ApproveActivityDelegate {

    public static final String PREF_SUBGROUP_PRE_RATIONALE_SHOWN = "_PRE_RATIONALE_SHOWN";
    public static final String PREF_SUBGROUP_POST_RATIONALE_SHOWN = "_POST_RATIONALE_SHOWN";
    public static final String PREF_SUBGROUP_POST_RATIONALE_SETTINGS_SHOWN = "_POST_RATIONALE_SETTINGS_SHOWN";

    private AppCompatActivity activity;
    private Map<Integer, RuntimePermissionsRequest> activeRuntimePermissionRequest;

    public ApproveActivityDelegate(AppCompatActivity activity) {
        this.activity = activity;
        activeRuntimePermissionRequest = new HashMap<>();
    }

    /**
     * Possible to send in array of permissions to be requested, on callback for each permission will be fired.
     * Custom handling due to API is broken by design. Sometimes the frameworks assumes multiple permissions are requested at
     * same time but in other times the API is written to only handle one permission at a time.
     */
    public void requestAppPermissions(final RuntimePermissionsRequest request) {
        final String permission = request.getRequestedPermission();

        if (hasPermission(permission)) {
            request.getRequestListener().onPermissionsGranted(request.getRequestCode(), permission);
            return;
        }

        boolean hasShownRationaleBefore = get(permission + PREF_SUBGROUP_PRE_RATIONALE_SHOWN);

        if ( ! hasShownRationaleBefore && request.hasPreRequestRationale()) {
            set(permission + PREF_SUBGROUP_PRE_RATIONALE_SHOWN);

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(request.getPreRequestRationale())
                    .setPositiveButton(request.getPositiveButtonLabel(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestAppPermissionsReal(request);
                        }
                    });

            if (request.hasDialogTitle()) {
                builder.setTitle(request.getDialogTitle());
            }

            builder.show();
        } else {
            requestAppPermissionsReal(request);
        }
    }

    private void requestAppPermissionsReal(RuntimePermissionsRequest request) {
        activeRuntimePermissionRequest.put(request.getRequestCode(), request);
        ActivityCompat.requestPermissions(activity, new String[] {request.getRequestedPermission()}, request.getRequestCode());
    }

    public void onRequestPermissionsResult(final int requestCode, String[] permissions, int[] grantResults) {
        if (permissions.length != 1) {
            throw new RuntimeException("Can only request one runtime permission at a time!");
        }

        final String permission = permissions[0];
        final int grantResult = grantResults[0];

        if ( ! activeRuntimePermissionRequest.containsKey(requestCode)) {
            throw new RuntimeException("Missing listener for request code!");
        }

        final RuntimePermissionsRequest request = activeRuntimePermissionRequest.get(requestCode);
        activeRuntimePermissionRequest.remove(requestCode);

        if (grantResult == PackageManager.PERMISSION_GRANTED) {
            request.getRequestListener().onPermissionsGranted(requestCode, permission);
            return;
        }

        // Whether or not the check-box is ticked can be determined by using Activity.shouldShowRequestPermissionRationale(String)
        // ActivityCompat.shouldShowRequestPermissionRationale helps us by returning true if the use HAVE NOT checked the option "Do not ask me again", otherwise false.
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            maybeShowPostRequestRationaleAndCallback(request);
            return;
        }

        if (request.shouldShowGotoSettingsOptionOnFail()) {
            maybeShowManualOptionToChangePermissionsAndCallback(request);
            return;
        }

        // all else failed..
        request.getRequestListener().onPermissionsDenied(request.getRequestCode(), request.getRequestedPermission());
    }

    public static boolean isPermissionModelRuntimeBased() {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.M;
    }

    @TargetApi(23)
    public boolean hasPermission(String permission) {
        if (isPermissionModelRuntimeBased()) {
            return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
        } else {
            // if installed on older OS than M permissions is implicitly granted.
            return true;
        }
    }

    /**
     * If the user have previously denied a permission (and told the system not to ask again)
     * the only way to be granted permissions is if the user manually goes to the settings app
     * and enables them.
     *
     * It is assumed that whatever action the user did to trigger this permission request
     * will be triggered again by the user once returing from settings. Hence no callback needed.
     */
    public void startSettingsApp() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        activity.startActivity(intent);
    }

    public boolean get(String key) {
        return ApproveSharedPreferences.getInstance(activity).get(key);
    }

    public void set(String key) {
        ApproveSharedPreferences.getInstance(activity).set(key, true);
    }

    private void maybeShowPostRequestRationaleAndCallback(final RuntimePermissionsRequest request) {
        boolean hasShownRationaleBefore = get(request.getRequestedPermission() + PREF_SUBGROUP_POST_RATIONALE_SHOWN);

        if ( ! hasShownRationaleBefore) {
            set(request.getRequestedPermission() + PREF_SUBGROUP_POST_RATIONALE_SHOWN);

            if (request.hasPostDeniedRequestRationale()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage(request.getPostDeniedRequestRationale())
                        .setPositiveButton(request.getPositiveButtonLabel(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                request.getRequestListener().onPermissionsDenied(request.getRequestCode(), request.getRequestedPermission());
                            }
                        })
                        .setNegativeButton(request.getTryAgainButtonLabel(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestAppPermissions(request);
                            }
                        });
                if (request.hasDialogTitle()) {
                    builder.setTitle(request.getDialogTitle());
                }

                builder.show();
                return;
            }
        }
        request.getRequestListener().onPermissionsDenied(request.getRequestCode(), request.getRequestedPermission());
    }

    private void maybeShowManualOptionToChangePermissionsAndCallback(final RuntimePermissionsRequest request) {
        boolean hasShownRationaleBefore = get(request.getRequestedPermission() + PREF_SUBGROUP_POST_RATIONALE_SETTINGS_SHOWN);

        if ( ! hasShownRationaleBefore) {
            set(request.getRequestedPermission() + PREF_SUBGROUP_POST_RATIONALE_SETTINGS_SHOWN);

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(request.getManualOptionToChangePermissionsRationale())
                    .setPositiveButton(request.getGotoSettingsButtonLabel(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startSettingsApp();
                        }
                    })
                    .setNegativeButton(request.getNegativeButtonLabel(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            request.getRequestListener().onPermissionsDenied(request.getRequestCode(), request.getRequestedPermission());
                        }
                    });

            if (request.hasDialogTitle()) {
                builder.setTitle(request.getDialogTitle());
            }

            builder.show();
            return;
        }
        request.getRequestListener().onPermissionsDenied(request.getRequestCode(), request.getRequestedPermission());
    }
}
