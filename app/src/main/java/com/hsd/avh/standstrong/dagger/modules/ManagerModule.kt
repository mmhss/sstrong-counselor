package com.hsd.avh.standstrong.dagger.modules

import android.app.Application
import com.hsd.avh.standstrong.managers.AnalyticsManager
import com.hsd.avh.standstrong.managers.DataLoadManger
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ManagerModule(val app: Application) {

    @Provides
    @Singleton
    fun provideApp() : Application = app

    @Provides
    @Singleton
    fun provideDataLoadManager() : DataLoadManger = DataLoadManger(app)

    @Provides
    @Singleton
    fun provideAnalyticsManager() = AnalyticsManager(app)
}