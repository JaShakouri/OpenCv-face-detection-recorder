package ir.jashakouri.opencvproject.view.activity;

import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.jaShakouriLib.ObjectDetectingView;
import com.jaShakouriLib.ObjectDetector;
import com.jaShakouriLib.listener.OnObjectTrackingListener;
import com.jaShakouriLib.listener.OnOpenCVLoadListener;
import com.jaShakouriLib.listener.OnRecordVideo;
import com.jaShakouriLib.recorder.CameraHelper;

import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import java.io.File;
import java.io.IOException;

import ir.jashakouri.opencvproject.R;
import ir.jashakouri.opencvproject.view.base.BaseActivity;

public class FaceDetectionActivity extends BaseActivity implements OnRecordVideo {

    private static final String TAG = "FaceDetection";

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
     * ProgressBar is display status recording
     * ImageButton is play video recording
     *
     * @return null
     */
    private ObjectDetectingView detectingView;
    private CardView cardToolbar;
    private TextView tvStatus;
    private ProgressBar progress;
    private ImageButton ibPlay;

    private MediaRecorder mMediaRecorder;
    private File mOutputFile;
    private boolean isRecording;

    @Override
    protected int layout() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        detectingView = findViewById(R.id.camera_view);
        cardToolbar = findViewById(R.id.cardToolbar);
        tvStatus = findViewById(R.id.tvStatus);
        progress = findViewById(R.id.progress);
        ibPlay = findViewById(R.id.ibPlay);

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

                if (OpenCVLoader.initDebug()) {
                    detectingView.onManagerConnected(LoaderCallbackInterface.SUCCESS);
                } else {
                    showInstallDialog();
                }

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

    @Override
    public void onResume() {
        super.onResume();
        initOpenCV();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (detectingView != null) {
            detectingView.disableView();
            detectingView.setOnObjectTrackingListener(null);
        }
    }

    private void startRecord() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (countDownTimer == null) {

                    countDownTimer = new CountDownTimer(7000, 1000) {
                        @Override
                        public void onTick(final long millisUntilFinished) {

                            if (millisUntilFinished <= 5000) {

                                tvStatus.setText(getString(R.string.str_recording_time,
                                        String.valueOf((int) (millisUntilFinished / 1000))));
                                progress.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onFinish() {

                            tvStatus.setText(getString(R.string.str_record_finish));
                            progress.setVisibility(View.GONE);
                            ibPlay.setVisibility(View.VISIBLE);
                            processCapture();

                        }

                    };

                    cardToolbar.setVisibility(View.VISIBLE);
                    countDownTimer.start();

                    processCapture();

                }
            }
        });

    }

    private void stopRecord() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                    tvStatus.setText("Recording Stop\nPlease don't move");
                    stopCapture();
                    progress.setVisibility(View.GONE);
                    ibPlay.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Please Don't Move", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void processCapture() {

        if (isRecording) {

            Log.d(TAG, "processCapture Stop recorder");

            if (mMediaRecorder != null) {
                mMediaRecorder.stop();
                mMediaRecorder.release();
                mMediaRecorder = null;
                detectingView.setRecorder(null);

                onRecord(mOutputFile);
            }

            isRecording = false;

        } else {

            Log.d(TAG, "Starting recorder");
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.reset();
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            CamcorderProfile cphigh = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
            mMediaRecorder.setProfile(cphigh);

            mOutputFile = CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_VIDEO);
            if (mOutputFile == null) {
                return;
            }
            mMediaRecorder.setOutputFile(mOutputFile.getPath());

            Log.d(TAG, String.valueOf(cphigh.videoBitRate));

            try {
                mMediaRecorder.prepare();
            } catch (IOException e) {
                Log.e("debug mediarecorder", "not prepare IOException");
            }

            detectingView.setRecorder(mMediaRecorder);
            mMediaRecorder.start();
            isRecording = true;
        }

    }

    private void stopCapture() {

        Log.d(TAG, "Stop recorder");

        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
            detectingView.setRecorder(null);
            onRecordFailure();

            if (mOutputFile != null && mOutputFile.exists() && mOutputFile.delete()) {
                Toast.makeText(this, R.string.str_file_deleted, Toast.LENGTH_SHORT).show();
            }

        }

        isRecording = false;

    }

    @Override
    public void onRecord(File file) {

        Log.i(TAG, "onRecord: file path : " + file.getAbsolutePath());
        Log.i(TAG, "onRecord: file size : " + file.length());

        PlayerActivity.start(this, file.getAbsolutePath());

    }

    @Override
    public void onRecordFailure() {

        Log.i(TAG, "onRecordFailure");

    }
}