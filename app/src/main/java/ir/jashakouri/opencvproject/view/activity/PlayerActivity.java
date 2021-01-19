package ir.jashakouri.opencvproject.view.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

import java.io.File;

import ir.jashakouri.opencvproject.BuildConfig;
import ir.jashakouri.opencvproject.R;
import ir.jashakouri.opencvproject.utils.Utils;
import ir.jashakouri.opencvproject.view.base.BaseActivity;

public class PlayerActivity extends BaseActivity implements View.OnClickListener {

    static String path;

    private VideoView videoView;
    private ConstraintLayout toolbox;

    @Override
    protected int layout() {
        return R.layout.activity_player;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        videoView = findViewById(R.id.videoView);

        toolbox = findViewById(R.id.toolbox);

    }

    @Override
    protected void onPause() {
        super.onPause();
        showToolbox();
    }

    private void hideToolbox() {
        toolbox.setVisibility(View.GONE);
    }

    private void showToolbox() {
        toolbox.setVisibility(View.VISIBLE);
        if (videoView.isPlaying())
            videoView.stopPlayback();
    }

    private void initVideo() {
        Uri uri = Uri.parse(path);
        videoView.setVideoURI(uri);
        hideToolbox();
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                showToolbox();
            }
        });
    }

    public static void start(Context context, String path) {
        PlayerActivity.path = path;
        context.startActivity(new Intent(context, PlayerActivity.class));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ibPlay:

                initVideo();

                break;

            case R.id.ibDelete:

                new AlertDialog.Builder(this)
                        .setTitle("Delete Video")
                        .setMessage(R.string.str_delete_description)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                File file = new File(path);
                                if (file.exists() && file.delete()) {
                                    Toast.makeText(PlayerActivity.this,
                                            getString(R.string.str_deleted), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    finish();
                                } else {
                                    Toast.makeText(PlayerActivity.this,
                                            getString(R.string.str_delete_failure), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

                break;

            case R.id.ibShare:

                File videoFile = new File(path);
                Utils.shareFile(this, videoFile);

                break;

            case R.id.toolbox:
            case R.id.videoView:

                if (toolbox.getVisibility() == View.VISIBLE) {
                    if (videoView.isPlaying())
                        toolbox.setVisibility(View.GONE);
                } else {
                    toolbox.setVisibility(View.VISIBLE);
                }

                break;

        }

    }

    @Override
    public void onBackPressed() {

        if (videoView != null && videoView.isPlaying()) {
            showToolbox();
            return;
        }

        super.onBackPressed();
    }
}