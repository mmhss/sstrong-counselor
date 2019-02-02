package com.hsd.avh.standstrong.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.hsd.avh.standstrong.StandStrong;

import java.util.Calendar;

public class AlarmRebootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Start the scheduler again
        StandStrong.Companion.startCollection();
    }
}
