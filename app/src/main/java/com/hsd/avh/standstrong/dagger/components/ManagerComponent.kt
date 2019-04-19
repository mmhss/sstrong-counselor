package com.hsd.avh.standstrong.dagger.components

import com.hsd.avh.standstrong.BaseActivity
import com.hsd.avh.standstrong.MainActivity
import com.hsd.avh.standstrong.dagger.modules.ManagerModule
import com.hsd.avh.standstrong.fragments.FilterPersonFabFragment
import com.hsd.avh.standstrong.fragments.FilterPostFabFragment
import com.hsd.avh.standstrong.fragments.baseFragments.BaseFragment
import com.hsd.avh.standstrong.viewmodels.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ManagerModule::class])
@Singleton
interface ManagerComponent {

    fun inject(main: MainViewModel)
    fun inject(baseFragment: BaseFragment)
    fun inject(filterPersonFabFragment: FilterPersonFabFragment)
    fun inject(filterPostFabFragment: FilterPostFabFragment)
    fun inject(baseActivity: BaseActivity)
}