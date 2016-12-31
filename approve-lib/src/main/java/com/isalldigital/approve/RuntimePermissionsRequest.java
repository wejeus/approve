package com.isalldigital.approve;


public class RuntimePermissionsRequest {

    private int requestCode;
    private String permission;
    private ApproveRequestListener listener;
    private String preRequestRationale;
    private String postDeniedRequestRationale;
    private boolean shouldShowGotoSettingsOptionOnFail;

    public int getRequestCode() {
        return requestCode;
    }

    public String getRequestedPermission() {
        return permission;
    }

    public ApproveRequestListener getRequestListener() {
        return listener;
    }

    public boolean hasPreRequestRationale() {
        return preRequestRationale != null;
    }

    public String getPreRequestRationale() {
        return preRequestRationale;
    }

    public boolean hasPostDeniedRequestRationale() {
        return postDeniedRequestRationale != null;
    }

    public String getPostDeniedRequestRationale() {
        return postDeniedRequestRationale;
    }

    public boolean shouldShowGotoSettingsOptionOnFail() {
        return shouldShowGotoSettingsOptionOnFail;
    }

    public static class Builder {
        private RuntimePermissionsRequest request;

        public Builder() {
            request = new RuntimePermissionsRequest();
        }

        public Builder setRequestCode(int requestCode) {
            request.requestCode = requestCode;
            return this;
        }

        public Builder setRequestedPermission(String permission) {
            request.permission = permission;
            return this;
        }

        public Builder setRequestListener(ApproveRequestListener listener) {
            request.listener = listener;
            return this;
        }

        // @StringRes int textId
        public Builder setShowPreRequestRationale(String message) {
            request.preRequestRationale = message;
            return this;
        }

        /**
         * Shows a dialog with message if requested permission is denied.
         * Intention is to give further explanation why app needs that particular permission
         * in the hope getting permission from user next time it is requested.
         * @param message
         * @return
         */
        public Builder setShowPostDeniedRequestRationale(String message) {
            request.postDeniedRequestRationale = message;
            return this;
        }

        /**
         * Only use this for permissions with extremely high requirement.
         * This shows an option to (if requirement was previously denied)
         * the system settings app to manually turn on the permission.
         * That is only if you really really really need it.
         */
        public Builder setShouldShowGotoSettingsOptionOnFail(boolean show) {
            request.shouldShowGotoSettingsOptionOnFail = show;
            return this;
        }

        public RuntimePermissionsRequest build() {
            return request;
        }
    }
}
