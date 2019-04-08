package com.hsd.avh.standstrong.managers

import android.app.Application
import com.hsd.avh.standstrong.utilities.SSUtils
import javax.inject.Inject

class DataLoadManger @Inject constructor(val app: Application) {

    fun updateAll() {

        SSUtils.checkForNewProximity()
        SSUtils.checkForNewGPS()
        SSUtils.checkForNewMessages()
        SSUtils.checkForNewActivity()
    }
}