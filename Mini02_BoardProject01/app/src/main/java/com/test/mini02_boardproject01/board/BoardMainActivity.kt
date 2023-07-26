package com.test.mini02_boardproject01.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.test.mini02_boardproject01.R
import com.test.mini02_boardproject01.databinding.ActivityBoardMainBinding

class BoardMainActivity : AppCompatActivity() {
    lateinit var activityBoardMainBinding: ActivityBoardMainBinding

    lateinit var drawerLayout: DrawerLayout
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBoardMainBinding = ActivityBoardMainBinding.inflate(layoutInflater)
        setContentView(activityBoardMainBinding.root)
        setSupportActionBar(activityBoardMainBinding.materialToolbar)

        drawerLayout = activityBoardMainBinding.drawerLayout
        navController = findNavController(R.id.nav_host_fragment_activity_board_main)

        // 액션바 설정
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.whole_item, R.id.free_item, R.id.humor_item, R.id.sports_item, R.id.qna_item
            )
            , drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        activityBoardMainBinding.navigationView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}