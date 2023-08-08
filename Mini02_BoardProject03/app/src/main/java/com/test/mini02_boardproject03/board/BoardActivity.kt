package com.test.mini02_boardproject03.board

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
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
    lateinit var drawerLayout: DrawerLayout

    //nav에 따라 앱바 타이틀을 바꿔주고, navigationIcon의 역할과 모양을 바꿔주는, drawerlayout도 보여주는 놈!
    lateinit var appBarConfiguration: AppBarConfiguration

    //navHost에 있는 앱 네비게이션을 관리해주는 놈
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityBoardBinding = ActivityBoardBinding.inflate(layoutInflater)
        setContentView(activityBoardBinding.root)
        setSupportActionBar(activityBoardBinding.boardToolbar)

        drawerLayout = activityBoardBinding.drawerLayout

        //navController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // 네비게이션 이벤트 발생 시 실행될 코드
            activityBoardBinding.navigationView.setCheckedItem(destination.id)
        }
        //navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.postListFragment,
                R.id.postQnaFragment,
                R.id.postHumorFragment,
                R.id.postHumorFragment,
                R.id.postFreeFragment
            ), drawerLayout
        )


        //navController Setup
        setupActionBarWithNavController(navController, appBarConfiguration)
        activityBoardBinding.navigationView.setupWithNavController(navController)
        activityBoardBinding.navigationView.setCheckedItem(R.id.postListFragment)
        //navController Setup

    }

    //뒤로가기 버튼을 누르면 호출되는 함수!
    override fun onSupportNavigateUp(): Boolean {
        //최상위가 아니라 뒤로갈 수 있으면 이 줄이 실행됨
        return navController.navigateUp(appBarConfiguration)
    }
}