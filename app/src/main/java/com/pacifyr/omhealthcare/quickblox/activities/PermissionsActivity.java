package com.pacifyr.omhealthcare.quickblox.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.pacifyr.omhealthcare.quickblox.utils.Consts;
import com.pacifyr.omhealthcare.quickblox.utils.PermissionsChecker;
import com.quickblox.sample.core.utils.Toaster;
import com.quickblox.sample.groupchatwebrtc.R;


public class PermissionsActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 0;
    private static final String EXTRA_PERMISSIONS = "extraPermissions";
    public static final String CHECK_ONLY_AUDIO = "checkAudio";

    private enum permissionFeatures {
        CAMERA,
        MICROPHONE
    }

    private PermissionsChecker checker;
    private boolean requiresCheck;
    public static final int PERMISSION_EXTERNAL_REQUEST_CODE = 11;

    public static void startActivity(Activity activity, boolean checkOnlyAudio, String... permissions) {
        Intent intent = new Intent(activity, PermissionsActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        intent.putExtra(CHECK_ONLY_AUDIO, checkOnlyAudio);
       // ActivityCompat.startActivity(activity, intent, null);
        activity.startActivityForResult(intent, PERMISSION_EXTERNAL_REQUEST_CODE);
       // ActivityCompat. startActivityForResult(activity,intent, PERMISSION_EXTERNAL_REQUEST_CODE,null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || !getIntent().hasExtra(EXTRA_PERMISSIONS)) {
            throw new RuntimeException("This Activity needs to be launched using the static startActivityForResult() method.");
        }
        setContentView(R.layout.activity_permissions);

        checker = new PermissionsChecker(this);
        requiresCheck = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (requiresCheck) {
            checkPermissions();
        } else {
            requiresCheck = true;
        }
    }

    private void checkPermissions() {
        String[] permissions = getPermissions();
        boolean checkOnlyAudio = getCheckOnlyAudio();

        if (checkOnlyAudio) {
            checkPermissionAudio(permissions[1]);
        } else {
            checkPermissionAudioVideo(permissions);
        }
    }

    private void checkPermissionAudio(String audioPermission) {
        if (checker.lacksPermissions(audioPermission)) {
            requestPermissions(audioPermission);
        } else {
            allPermissionsGranted();
        }
    }

    private void checkPermissionAudioVideo(String[] permissions) {
        if (checker.lacksPermissions(permissions)) {
            requestPermissions(permissions);
        } else {
            allPermissionsGranted();
        }
    }

    private String[] getPermissions() {
        return getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
    }

    private boolean getCheckOnlyAudio() {
        return getIntent().getBooleanExtra(CHECK_ONLY_AUDIO, false);
    }

    private void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    private void allPermissionsGranted() {
        Intent intent=new Intent();
        intent.putExtra(CHECK_ONLY_AUDIO,getCheckOnlyAudio());
        intent.putExtra("permissions",true);
        setResult(PERMISSION_EXTERNAL_REQUEST_CODE,intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            requiresCheck = true;
            allPermissionsGranted();
        } else {
            requiresCheck = false;
            showDeniedResponse(grantResults);
           // finish();
        }
    }

 /*   @Override
    protected void onDestroy() {
        super.onDestroy();
        if (checker.lacksPermissions(permissions)) {
            requestPermissions(permissions);
        } else {
            allPermissionsGranted();
        }

    }*/

    private void showDeniedResponse(int[] grantResults) {
        if (grantResults.length > 1) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != 0) {
                    Toaster.longToast(getString(R.string.permission_unavailable, permissionFeatures.values()[i]));
                }
            }
        } else {
            Toaster.longToast(getString(R.string.permission_unavailable, permissionFeatures.MICROPHONE));
        }
        Intent intent=new Intent();
        intent.putExtra(CHECK_ONLY_AUDIO,getCheckOnlyAudio());
        intent.putExtra("permissions",false);
        setResult(PERMISSION_EXTERNAL_REQUEST_CODE,intent);

        finish();
    }

    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
}