package ir.jashakouri.opencvproject.view.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.kongqw.permissionslibrary.PermissionsManager;

import ir.jashakouri.opencvproject.R;
import ir.jashakouri.opencvproject.view.base.BaseActivity;

public class OverViewActivity extends BaseActivity {

    private String[] PERMISSIONS = new String[]{Manifest.permission.CAMERA};

    private int REQUEST_CODE_DETECTION = 1001;

    @Override
    protected int layout() {
        return R.layout.activity_over_view;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        checkPermissions();
    }

    private void checkPermissions() {
        mPermissionsManager = new PermissionsManager(this) {
            @Override
            public void authorized(int requestCode) {
                if (requestCode == REQUEST_CODE_DETECTION)
                    startActivity(new Intent(OverViewActivity.this, MainActivity.class));

                finish();
            }

            @Override
            public void noAuthorization(int i, String[] strings) {
                showPermissionDialog();
            }

            @Override
            public void ignore(int requestCode) {
                authorized(requestCode);
            }

        };
        mPermissionsManager.checkPermissions(REQUEST_CODE_DETECTION, PERMISSIONS);
    }

}