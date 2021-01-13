package ir.jashakouri.opencvproject.view.base;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.kongqw.permissionslibrary.PermissionsManager;

public abstract class BaseActivity extends AppCompatActivity {

    protected PermissionsManager mPermissionsManager;

    @LayoutRes
    protected abstract int layout();

    protected abstract void init(Bundle savedInstanceState);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(layout());
        init(savedInstanceState);
    }

    /**
     * Review authority
     *
     * @param requestCode  requestCode
     * @param permissions  permissions
     * @param grantResults grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionsManager.recheckPermissions(requestCode, permissions, grantResults);
    }

    protected void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Request permission");
        builder.setMessage("Android 6.0+ dynamically request camera permissions");
        builder.setPositiveButton("Go to set permissions", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PermissionsManager.startAppSettings(getApplicationContext());
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    protected void showInstallDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("You have not installed OpenCV Manager");
        builder.setMessage("Whether to download and install?");

        builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Downloading", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://github.com/kongqw/FaceDetectLibrary/tree/opencv3.2.0/OpenCVManager")));
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }

}