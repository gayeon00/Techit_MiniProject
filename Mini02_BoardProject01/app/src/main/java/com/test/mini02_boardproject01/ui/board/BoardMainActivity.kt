package com.test.mini02_boardproject01.ui.board

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.test.mini02_boardproject01.R
import com.test.mini02_boardproject01.data.model.User
import com.test.mini02_boardproject01.databinding.ActivityBoardMainBinding
import com.test.mini02_boardproject01.databinding.NavHeaderBinding
import com.test.mini02_boardproject01.ui.MainActivity

class BoardMainActivity : AppCompatActivity() {
    lateinit var activityBoardMainBinding: ActivityBoardMainBinding

    lateinit var drawerLayout: DrawerLayout
    lateinit var navController: NavController

    lateinit var loginUser: User

    // 게시판 종류
    val postTypeList = arrayOf(
        "자유게시판", "유머게시판", "스포츠게시판", "질문게시판"
    )

    var nowPostType = 0

    // 확인할 권한 목록
    val permissionList = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_MEDIA_LOCATION,
        Manifest.permission.INTERNET
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBoardMainBinding = ActivityBoardMainBinding.inflate(layoutInflater)
        setContentView(activityBoardMainBinding.root)
        setSupportActionBar(activityBoardMainBinding.materialToolbar)

        drawerLayout = activityBoardMainBinding.drawerLayout
        navController = findNavController(R.id.nav_host_fragment_activity_board_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            loginUser = intent.getSerializableExtra("user", User::class.java)!!
        }

        requestPermissions(permissionList, 0)
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
                headerBoardMainBinding.textViewHeaderTitle.text = "${loginUser.userNickname}님"
                addHeaderView(headerBoardMainBinding.root)

                setNavigationItemSelectedListener {
                    when (it.itemId) {
                        R.id.whole_item -> {
                            materialToolbar.title = "전체게시판"
                            navController.popBackStack()
                            val args = Bundle()
                            args.putLong("postType", 0L)
                            nowPostType = 0

                            navController.navigate(R.id.postListFragment, args)

                            drawerLayout.close()
                        }

                        R.id.free_item -> {
                            materialToolbar.title = "자유게시판"
                            navController.popBackStack()
                            val args = Bundle()
                            args.putLong("postType", 1L)
                            nowPostType = 1
                            navController.navigate(R.id.postListFragment, args)

                            drawerLayout.close()
                        }

                        R.id.humor_item -> {
                            materialToolbar.title = "유머게시판"
                            navController.popBackStack()
                            val args = Bundle()
                            args.putLong("postType", 2L)
                            nowPostType = 2
                            navController.navigate(R.id.postListFragment, args)

                            drawerLayout.close()
                        }

                        R.id.sports_item -> {
                            materialToolbar.title = "스포츠게시판"
                            navController.popBackStack()
                            val args = Bundle()
                            args.putLong("postType", 3L)
                            nowPostType = 3
                            navController.navigate(R.id.postListFragment, args)

                            drawerLayout.close()
                        }

                        R.id.qna_item -> {
                            materialToolbar.title = "질문게시판"
                            navController.popBackStack()
                            val args = Bundle()
                            args.putLong("postType", 4L)
                            nowPostType = 4
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