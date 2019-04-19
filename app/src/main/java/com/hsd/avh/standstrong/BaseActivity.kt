package com.hsd.avh.standstrong

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hsd.avh.standstrong.managers.AnalyticsManager
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var analyticsManager: AnalyticsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as StandStrong).managerComponent.inject(this)
    }
}