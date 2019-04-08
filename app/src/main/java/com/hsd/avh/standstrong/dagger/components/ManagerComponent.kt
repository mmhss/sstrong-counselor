package com.hsd.avh.standstrong.dagger.components

import com.hsd.avh.standstrong.MainActivity
import com.hsd.avh.standstrong.dagger.modules.ManagerModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ManagerModule::class])
@Singleton
interface ManagerComponent {

    fun inject(main: MainActivity)
}