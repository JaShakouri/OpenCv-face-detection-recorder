package ir.jashakouri.opencvproject.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import ir.jashakouri.opencvproject.R;
import ir.jashakouri.opencvproject.view.base.BaseActivity;

public class PlayerActivity extends BaseActivity {

    protected static String path;

    TextView tvPath;

    @Override
    protected int layout() {
        return R.layout.activity_player;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        tvPath = findViewById(R.id.tvPath);
        tvPath.setText(path);
    }

    public static void start(Context context, String path) {
        PlayerActivity.path = path;
        context.startActivity(new Intent(context, PlayerActivity.class));
    }

}