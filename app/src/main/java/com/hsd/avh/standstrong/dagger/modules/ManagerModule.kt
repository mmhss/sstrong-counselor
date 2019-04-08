package com.hsd.avh.standstrong.dagger.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ManagerModule(val app: Application) {

    @Provides
    @Singleton
    fun provideApp() : Application = app
}