package com.isalldigital.approve;


public interface ApproveRequestListener {

    void onPermissionsGranted(int requestCode, String permission);

    void onPermissionsDenied(int requestCode, String permission);

}
