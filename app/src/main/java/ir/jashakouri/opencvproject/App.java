package ir.jashakouri.opencvproject;

import android.app.Application;

import ir.jashakouri.opencvproject.utils.Utils;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.checkFolder();
    }
}
