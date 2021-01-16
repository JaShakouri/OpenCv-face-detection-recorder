package ir.jashakouri.opencvproject.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;

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

}
