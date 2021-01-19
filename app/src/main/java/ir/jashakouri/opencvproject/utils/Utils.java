package ir.jashakouri.opencvproject.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;

import java.io.File;

import ir.jashakouri.opencvproject.BuildConfig;
import ir.jashakouri.opencvproject.utils.appEnum.FileDirectory;

public class Utils {

    private static final String TAG = "Utils";

    public static String[] path_create = new String[]{"/Face Detection"/* parent folder */, "/video"};

    public static void checkFolder() {

        String TAG = "CreateFolderTAG";

        File file;

        for (int i = 0; i < path_create.length; i++) {
            if (i == 0) {
                file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), path_create[i]);
            } else {
                file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), path_create[0] + path_create[i]);
            }

            if (!file.exists()) {

                if (file.mkdirs())
                    Log.i(TAG, "checkFolder: " + path_create[i] + " created");
                else
                    Log.i(TAG, "checkFolder: Can not create " + path_create[i]);
            } else {
                Log.i(TAG, "checkFolder: " + path_create[i] + " is exists");
            }

        }

        file = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Face Detection");
        if (!file.exists()) {

            if (file.mkdir())
                Log.i(TAG, "checkFolder: " + Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Face Detection" + " created");
            else
                Log.i(TAG, "checkFolder: Can not create " + Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Face Detection");

        } else {
            Log.i(TAG, "checkFolder: " + Environment.DIRECTORY_PICTURES + "/Face Detection" + " is exists");
        }

    }

    public static File getFileResource(FileDirectory fileDirectory) {

        if (fileDirectory == FileDirectory.Public) {
            return new File(Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_PICTURES) + "/Face Detection");
        } else
            return new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                    fileDirectory == FileDirectory.Main ? path_create[0] :
                            fileDirectory == FileDirectory.Video ? path_create[0] + path_create[1] : "");
    }

    public static File defaultVideoPath() {
        checkFolder();
        return new File(getFileResource(FileDirectory.Video).getAbsolutePath(),
                System.currentTimeMillis() + ".mp4");
    }

    public static void shareFile(Activity activity, File file) {
        try {

            Uri contentUri = FileProvider.getUriForFile(activity.getApplicationContext(),
                    BuildConfig.APPLICATION_ID + ".contentprovider", file);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
            intent.setType(getMimeType(file.getAbsolutePath()));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivity(intent);

        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
        }
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

}
