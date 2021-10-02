package com.tomtruyen.budgettracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView : BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(findViewById(R.id.nav_host_fragment_activity_main))

        val appbarConfig = AppBarConfiguration(
            setOf(R.id.navigation_statistics, R.id.navigation_overview, R.id.navigation_settings)
        )

        setupActionBarWithNavController(navController, appbarConfig)
        navView.setupWithNavController(navController)
    }

}
