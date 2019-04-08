package com.hsd.avh.standstrong


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import com.hsd.avh.standstrong.databinding.ActivityHomeBinding
import com.hsd.avh.standstrong.viewmodels.MainViewModel


//import com.hsd.avh.standstrong

class MainActivity : AppCompatActivity(){


    private lateinit var navController: NavController
    private lateinit var navControllerPeople: NavController
    private val mainViewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
        //WorkManager.getInstance().enqueue(request)

        val binding: ActivityHomeBinding= DataBindingUtil.setContentView(this,
                R.layout.activity_home)

        //Main Navigation
        navController = Navigation.findNavController(this, R.id.main_nav_fragment)
        // Set up navigation menu
        NavigationUI.setupWithNavController(binding.basicBottomNavigation,
                Navigation.findNavController(this, R.id.main_nav_fragment))
        binding.basicBottomNavigation.setOnNavigationItemSelectedListener {item ->
            onNavDestinationSelected(item, Navigation.findNavController(this, R.id.main_nav_fragment))
        }

    }

    override fun onStart() {
        super.onStart()

        mainViewModel.updateAll()
    }
}