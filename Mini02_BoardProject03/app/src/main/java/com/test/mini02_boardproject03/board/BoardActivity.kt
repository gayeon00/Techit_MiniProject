package com.test.mini02_boardproject03.board

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.test.mini02_boardproject03.R
import com.test.mini02_boardproject03.databinding.ActivityBoardBinding
import com.test.mini02_boardproject03.user.MainActivity

class BoardActivity : AppCompatActivity() {
    lateinit var activityBoardBinding: ActivityBoardBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityBoardBinding = ActivityBoardBinding.inflate(layoutInflater)
        setContentView(activityBoardBinding.root)

        setSupportActionBar(activityBoardBinding.boardToolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.menu_24px)

        activityBoardBinding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.postListFragment,
                R.id.postFreeFragment,
                R.id.postHumorFragment,
                R.id.postSportsFragment,
                R.id.postQnaFragment,
                R.id.modifyUserFragment -> {

                    navController.navigate(it.itemId)
                }

                R.id.item_logout,
                R.id.item_delete -> {
                    goToMainActivity()
                }
            }

            activityBoardBinding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun goToMainActivity() {
        val newIntent = Intent(this, MainActivity::class.java)
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(newIntent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activityBoardBinding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() { //뒤로가기 했을 때
        if (activityBoardBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityBoardBinding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}