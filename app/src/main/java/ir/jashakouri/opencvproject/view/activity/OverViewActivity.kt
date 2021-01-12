package ir.jashakouri.opencvproject.view.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.kongqw.permissionslibrary.PermissionsManager
import ir.jashakouri.opencvproject.R
import ir.jashakouri.opencvproject.view.base.BaseActivity

class OverViewActivity : BaseActivity() {

    // Permissions to verify
    private val PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    // Identification request code
    private val REQUEST_CODE_DETECTION = 1001

    override fun layout(): Int {
        return R.layout.activity_over_view
    }

    override fun init(savedInstanceState: Bundle?) {
        checkPermissions()
    }

    private fun checkPermissions() {

        // Dynamic permission verification
        mPermissionsManager = object : PermissionsManager(this) {
            override fun authorized(requestCode: Int) {
                // Permission passed
                when (requestCode) {
                    REQUEST_CODE_DETECTION -> {
                        startActivity(
                            Intent(
                                this@OverViewActivity,
                                MainActivity::class.java
                            )
                        )
                        finish()
                    }
                }
            }

            override fun noAuthorization(requestCode: Int, lacksPermissions: Array<String>) {
                // Lack of necessary permissions
                showPermissionDialog()
            }

            override fun ignore(requestCode: Int) {
                // The system does not verify under Android 6.0
                authorized(requestCode)
            }
        }

        mPermissionsManager!!.checkPermissions(REQUEST_CODE_DETECTION, *PERMISSIONS)

    }

}