package com.hsd.avh.standstrong


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.hsd.avh.standstrong.databinding.ActivityHomeBinding
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected


//import com.hsd.avh.standstrong

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var navControllerPeople: NavController
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




}