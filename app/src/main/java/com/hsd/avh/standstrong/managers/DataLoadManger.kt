package com.hsd.avh.standstrong.managers

import android.app.Application
import com.hsd.avh.standstrong.utilities.SSUtils
import javax.inject.Inject

class DataLoadManger constructor(val app: Application) {

    fun updateAll() {

        SSUtils.loadPeople()
    }
}