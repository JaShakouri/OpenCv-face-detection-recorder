package ir.jashakouri.opencvproject.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import ir.jashakouri.opencvproject.R;

public class StatusBarUtils {

    public static void setLightStatusBar(Activity activity) {
        prepareTaskDescription(activity, false);
    }

    public static void setDarkStatusBar(Activity activity) {
        prepareTaskDescription(activity, true);
    }

    private static void prepareTaskDescription(Activity activity, boolean isDark) {

        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (isDark)
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            else
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.text_shadow));
        }

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(activity, true);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(activity, false);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }

    private static void setWindowFlag(Activity activity, boolean state) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();

        if (state) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                winParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                winParams.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            }
        }
    }

}