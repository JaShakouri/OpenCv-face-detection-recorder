package com.jaShakouriLib.listener;

import java.io.File;

public interface OnRecordVideo {
    void onRecord(File file);

    void onRecordFailure();
}
