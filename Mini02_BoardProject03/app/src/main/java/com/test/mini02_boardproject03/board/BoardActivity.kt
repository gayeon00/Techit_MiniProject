package com.test.mini02_boardproject03.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.test.mini02_boardproject03.R
import com.test.mini02_boardproject03.databinding.ActivityBoardBinding

class BoardActivity : AppCompatActivity() {
    lateinit var activityBoardBinding: ActivityBoardBinding
    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityBoardBinding = ActivityBoardBinding.inflate(layoutInflater)
        setContentView(activityBoardBinding.root)

        setSupportActionBar(activityBoardBinding.boardToolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.postListFragment, R.id.postFreeFragment, R.id.postHumorFragment, R.id.postSportsFragment, R.id.postQnaFragment, R.id.mainActivity, R.id.mainActivity2
            ), activityBoardBinding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        activityBoardBinding.navigationView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (activityBoardBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityBoardBinding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}