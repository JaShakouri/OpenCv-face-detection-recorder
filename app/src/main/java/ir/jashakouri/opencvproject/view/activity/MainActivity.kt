package ir.jashakouri.opencvproject.view.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.view.doOnDetach
import com.jaShakouriLib.ObjectDetector
import com.jaShakouriLib.listener.OnObjectTrackingListener
import com.jaShakouriLib.listener.OnOpenCVLoadListener
import ir.jashakouri.opencvproject.R
import ir.jashakouri.opencvproject.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.opencv.core.Point
import org.opencv.core.Scalar

class MainActivity : BaseActivity() {

    private var mFaceDetector: ObjectDetector? = null

    override fun layout(): Int {
        return R.layout.activity_main
    }

    override fun init(savedInstanceState: Bundle?) {

        photograph_view.setOnOpenCVLoadListener(object :
            OnOpenCVLoadListener {
            override fun onOpenCVLoadSuccess() {

                mFaceDetector = ObjectDetector(
                    applicationContext,
                    R.raw.lbpcascade_frontalface,
                    6,
                    0.2f,
                    0.2f,
                    Scalar(29.0, 151.0, 86.0, 255.0)
                )

                photograph_view.addDetector(mFaceDetector)
                cardToolbar.visibility = View.VISIBLE

            }

            override fun onOpenCVLoadFail() {
                Toast.makeText(
                    applicationContext,
                    "OpenCV Failed to load",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNotInstallOpenCVManager() {
                showInstallDialog()
            }

        })

        photograph_view.setOnObjectTrackingListener(object : OnObjectTrackingListener {
            override fun onObjectLocation(center: Point?) {
                Log.i("FaceDetection", "onObjectLocation")
            }

            override fun onObjectLost() {
                Log.i("FaceDetection", "onObjectLost")
            }

        })
        photograph_view.loadOpenCV(applicationContext)

    }

    fun swapCamera(view: View?) {
        photograph_view.swapCamera()
    }

}
