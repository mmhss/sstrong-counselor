package com.hsd.avh.standstrong.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.managers.DataLoadManger
import com.hsd.avh.standstrong.utilities.SSUtils
import javax.inject.Inject

class MainViewModel(val app: Application) : AndroidViewModel(app) {

    init {
        (app as StandStrong).managerComponent.inject(this)
    }

    @Inject
    lateinit var dataLoadManager: DataLoadManger

    fun updateAll() {

        dataLoadManager.updateAll()
    }
}