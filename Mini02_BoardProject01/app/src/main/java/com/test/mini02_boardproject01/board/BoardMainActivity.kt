package com.test.mini02_boardproject01.board

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.test.mini02_boardproject01.MainActivity
import com.test.mini02_boardproject01.R
import com.test.mini02_boardproject01.databinding.ActivityBoardMainBinding
import com.test.mini02_boardproject01.databinding.NavHeaderBinding

class BoardMainActivity : AppCompatActivity() {
    lateinit var activityBoardMainBinding: ActivityBoardMainBinding

    lateinit var drawerLayout: DrawerLayout
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBoardMainBinding = ActivityBoardMainBinding.inflate(layoutInflater)
        setContentView(activityBoardMainBinding.root)
        setSupportActionBar(activityBoardMainBinding.materialToolbar)

        drawerLayout = activityBoardMainBinding.drawerLayout
        navController = findNavController(R.id.nav_host_fragment_activity_board_main)

//        // 액션바 설정
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.whole_item, R.id.free_item, R.id.humor_item, R.id.sports_item, R.id.qna_item
//            ), drawerLayout
//        )
//
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        activityBoardMainBinding.navigationView.setupWithNavController(navController)

        activityBoardMainBinding.run {
            // toolbar
            materialToolbar.run {
                title = "게시판메인"

                setNavigationIcon(R.drawable.menu_24px)
                setNavigationOnClickListener {
                    // 네비게이션 뷰를 보여준다.
                    drawerLayout.open()
                }

            }
            navigationView.run {
                //전체 게시판 선택돼있도록
                setCheckedItem(R.id.whole_item)

                // 헤더설정
                val headerBoardMainBinding = NavHeaderBinding.inflate(layoutInflater)
                headerBoardMainBinding.textViewHeaderTitle.text = "홍길동님"
                addHeaderView(headerBoardMainBinding.root)

                setNavigationItemSelectedListener {


                    when (it.itemId) {
                        R.id.whole_item, R.id.free_item, R.id.humor_item, R.id.sports_item, R.id.qna_item-> {
                            navController.popBackStack()
                            val args = Bundle()
                            args.putString("boardType", it.itemId.toString())

                            navController.navigate(R.id.postListFragment, args)

                            drawerLayout.close()
                        }
                        //사용자 정보 수정
                        R.id.item_user_info -> {
                            navController.popBackStack()
                            navController.navigate(R.id.item_board_main_user_info)
                            drawerLayout.close()
                        }
                        //로그아웃
                        R.id.item_board_main_logout -> {
                            val newIntent = Intent(this@BoardMainActivity, MainActivity::class.java)
                            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(newIntent)
                            finish()
                        }
                        //회원탈퇴
                        R.id.item_board_main_sign_out -> {
                            val newIntent = Intent(this@BoardMainActivity, MainActivity::class.java)
                            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(newIntent)
                            finish()
                        }
                    }
                    true
                }
            }
        }
    }
}