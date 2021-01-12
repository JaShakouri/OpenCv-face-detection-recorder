package ir.jashakouri.opencvproject.view.base

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.kongqw.permissionslibrary.PermissionsManager

abstract class BaseActivity : AppCompatActivity() {

    protected var mPermissionsManager: PermissionsManager? = null

    @LayoutRes
    abstract fun layout(): Int

    abstract fun init(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout())
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        init(savedInstanceState)
    }

    protected fun hideStatusBar(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }

    }

    /**
     * Review authority
     *
     * @param requestCode  requestCode
     * @param permissions  permissions
     * @param grantResults grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        // After the user makes a choice, review the permissions to determine whether the permission application is passed
        mPermissionsManager!!.recheckPermissions(requestCode, permissions, grantResults)
    }

    /**
     * Show missing permission dialog
     */
    protected open fun showPermissionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Request permission")
        builder.setMessage("Android 6.0+ dynamically request camera permissions")
        builder.setPositiveButton(
            "Go to set permissions"
        ) { dialog, _ ->
            dialog.dismiss()
            PermissionsManager.startAppSettings(applicationContext)
        }
        builder.setNegativeButton(
            "cancel"
        ) { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }

    /**
     * Show a dialog box without OpenCV Manager installed
     */
    protected open fun showInstallDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle("You have not installed OpenCV Manager")
        builder.setMessage("Whether to download and install?")
        builder.setPositiveButton(
            "Download"
        ) { dialog, which ->
            Toast.makeText(applicationContext, "Downloading", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://github.com/kongqw/FaceDetectLibrary/tree/opencv3.2.0/OpenCVManager")))
        }
        builder.setNegativeButton(
            "Exit"
        ) { dialog, which ->
            dialog.dismiss()
            finish()
        }
        builder.create().show()
    }

}