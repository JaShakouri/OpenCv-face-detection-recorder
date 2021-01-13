package ir.jashakouri.opencvproject.view.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.jaShakouriLib.ObjectDetectingView;
import com.jaShakouriLib.ObjectDetector;
import com.jaShakouriLib.listener.OnObjectTrackingListener;
import com.jaShakouriLib.listener.OnOpenCVLoadListener;

import org.opencv.core.Point;
import org.opencv.core.Scalar;

import ir.jashakouri.opencvproject.R;
import ir.jashakouri.opencvproject.view.base.BaseActivity;

public class MainActivity extends BaseActivity {

    /**
     * ObjectDetector : Object Detection is a algorithm face detection
     * CountDownTimer : Counter to check movie recording time
     */
    private ObjectDetector mFaceDetector;
    private CountDownTimer countDownTimer;

    /**
     * Views is layout
     * ObjectDetectingView is camera viewer and process video
     * CardView is toolbar ViewGroup for display time recording
     * TextView is display status recording
     *
     * @return null
     */
    private ObjectDetectingView detectingView;
    private CardView cardToolbar;
    private TextView tvStatus;

    @Override
    protected int layout() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        detectingView = findViewById(R.id.camera_view);
        cardToolbar = findViewById(R.id.cardToolbar);
        tvStatus = findViewById(R.id.tvStatus);
        initOpenCV();
    }

    private void initOpenCV() {

        detectingView.setOnOpenCVLoadListener(new OnOpenCVLoadListener() {
            @Override
            public void onOpenCVLoadSuccess() {

                mFaceDetector = new ObjectDetector(
                        getApplicationContext(),
                        R.raw.lbpcascade_frontalface,
                        6,
                        0.2f,
                        0.2f,
                        new Scalar(29.0, 151.0, 86.0, 255.0)
                );

                detectingView.addDetector(mFaceDetector);

            }

            @Override
            public void onOpenCVLoadFail() {
                Toast.makeText(
                        getApplicationContext(),
                        "OpenCV Failed to load",
                        Toast.LENGTH_SHORT
                ).show();
            }

            @Override
            public void onNotInstallOpenCVManager() {
                showInstallDialog();
            }
        });

        detectingView.setOnObjectTrackingListener(new OnObjectTrackingListener() {
            @Override
            public void onObjectLocation(Point center) {
                Log.i("FaceDetection", "onObjectLocation");
                startRecord();
            }

            @Override
            public void onObjectLost() {
                Log.i("FaceDetection", "onObjectLost");
                stopRecord();
            }
        });

        detectingView.loadOpenCV(getApplicationContext());

    }

    private synchronized void startRecord() {

        Log.i(getClass().getSimpleName(), "startRecord: ");

        if (countDownTimer == null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    countDownTimer = new CountDownTimer(5000 /* 5 total sec */,
                            1000/* 1 sec interval */) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            tvStatus.setText("recording 00:0" + millisUntilFinished / 1000);
                        }

                        @Override
                        public void onFinish() {
                            tvStatus.setText("recording 00:05 finished");
                        }
                    };

                    cardToolbar.setVisibility(View.VISIBLE);
                    countDownTimer.start();

                }
            });
        }

    }

    private synchronized void stopRecord() {

        Log.i(getClass().getSimpleName(), "stopRecord: ");

        if (countDownTimer != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    countDownTimer.cancel();
                    countDownTimer = null;
                    tvStatus.setText("Recording Stop\nPlease don't move");
                    Toast.makeText(getApplicationContext(), "Please Don't Move", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

}