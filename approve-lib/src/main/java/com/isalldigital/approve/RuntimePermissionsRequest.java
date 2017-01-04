package com.isalldigital.approve;


public class RuntimePermissionsRequest {

    private int requestCode;
    private String permission;
    private ApproveRequestListener listener;
    private String preRequestRationale;
    private String postDeniedRequestRationale;
    private boolean shouldShowGotoSettingsOptionOnFail;
    private String positiveButtonLabel = "OK";
    private String negativeButtonLabel = "Cancel";
    private String dialogTitle;
    private String tryAgainButtonLabel = "Try Again";
    private String gotoSettingsButtonLabel = "Settings";
    private String manualOptionToChangePermissionsRationale = "";

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

    public String getPositiveButtonLabel() {
        return positiveButtonLabel;
    }

    public String getNegativeButtonLabel() {
        return negativeButtonLabel;
    }

    public boolean hasDialogTitle() {
        return dialogTitle != null;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public String getTryAgainButtonLabel() {
        return tryAgainButtonLabel;
    }

    public String getGotoSettingsButtonLabel() {
        return gotoSettingsButtonLabel;
    }

    public String getManualOptionToChangePermissionsRationale() {
        return manualOptionToChangePermissionsRationale;
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

        public Builder setPositiveButtonLabel(String label) {
            request.positiveButtonLabel = label;
            return this;
        }

        public Builder setNegativeButtonLabel(String label) {
            request.negativeButtonLabel = label;
            return this;
        }

        /**
         * Adds a dialog title if set otherwise none will be used
         */
        public Builder setShowDialogTitle(String title) {
            request.dialogTitle = title;
            return this;
        }

        public Builder setTryAgainLabel(String label) {
            request.tryAgainButtonLabel = label;
            return this;
        }

        public Builder setGotoSettingsButtonLabel(String label) {
            request.gotoSettingsButtonLabel = label;
            return this;
        }

        public Builder setManualOptionToChangePermissionsRationale(String message) {
            request.manualOptionToChangePermissionsRationale = message;
            return this;
        }

        public RuntimePermissionsRequest build() {
            return request;
        }
    }
}
