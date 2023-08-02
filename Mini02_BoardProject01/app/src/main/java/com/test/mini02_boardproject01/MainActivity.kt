package com.test.mini02_boardproject01

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.test.mini02_boardproject01.board.BoardMainActivity
import com.test.mini02_boardproject01.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding
    lateinit var navController: NavController
    var preferences: SharedPreferences? = null

    var userId = ""
    var userPw = ""
    var userNickname = ""
    var userAge = 0L
    var userJoinRoute1 = false
    var userJoinRoute2 = false
    var userJoinRoute3 = false
    var userJoinRoute4 = false
    var userJoinRoute5 = false

    lateinit var loginUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        splashScreenCustomizing(splashScreen)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        navController = findNavController(R.id.nav_host_fragment)
//        preferences = getSharedPreferences("data", MODE_PRIVATE)
//
//        if (preferences != null) {
//            userId = preferences!!.getString("userId", "user").toString()
//            userPw = preferences!!.getString("userPw", "user").toString()
//            userNickname = preferences!!.getString("userNickname", "user").toString()
//            userAge = preferences!!.getString("userAge", "user").toString().toLong()
//            userJoinRoute = preferences!!.getStringSet(
//                "userJoinRoute",
//                mutableSetOf<String>()
//            ) as MutableSet<String>
//        }
    }

    //splash screen 커스터마이징
    private fun splashScreenCustomizing(splashScreen: SplashScreen) {
        // splash screen이 사라질 때 동작하는 리스너를 설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener {
                //가로 비율 애니메이션
                //1배, 2배, 1배, 0배로 순차적으로 변경하자!
                val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 2f, 1f, 0f)

                //세로 비율 애니메이션
                //1배, 2배, 1배, 0배로 순차적으로 변경하자!
                val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 2f, 1f, 0f)

                //투명도
                //1배, 1배, 0.5배, 0배로 순차적으로 투명도를 변경하자!
                val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 1f, 0.5f, 0f)

                //animation 관리 객체 생성
                //첫 번째 : 애니메이션을 적용할 뷰
                //나머지 : 적용할 애니메이션 종류
                val objectAnimator =
                    ObjectAnimator.ofPropertyValuesHolder(it.iconView, scaleX, scaleY, alpha)
                //애니메이션 적용을 위한 에이징 (느리다가 점점 빨라질건지, 빠르다가 점점 느려질 건지 등 속도를 조정할 때 사용)
                objectAnimator.interpolator = AnticipateInterpolator()
                //애니메이션 동작 시간
                objectAnimator.duration = 1000
                // 애니메이션이 끝났을 때 동작할 리스너
                objectAnimator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)

                        // SplashScreen을 제거한다.
                        it.remove()
                    }
                })

                objectAnimator.start()

            }
        }

    }

    fun saveUserInfo() {
        val database = Firebase.database
        val userIdxRef = database.reference.child("userIdx")

        var userIdx = 0L

        userIdxRef.get().addOnCompleteListener { task ->
            //현재 사용자 순서 값을 가져온다.
            userIdx = task.result.value as Long

            val usersRef = database.reference.child("users")
            val user = User(
                ++userIdx,
                userId,
                userPw,
                userNickname,
                userAge,
                userJoinRoute1,
                userJoinRoute2,
                userJoinRoute3,
                userJoinRoute4,
                userJoinRoute5
            )
            usersRef.child(userId).setValue(user).addOnCompleteListener {
                userIdxRef.get().addOnCompleteListener {
                    it.result.ref.setValue(userIdx)
                }
            }

        }


    }

    fun goToBoardMainActivity() {
        val newIntent = Intent(this, BoardMainActivity::class.java)
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        newIntent.putExtra("user", loginUser)
        startActivity(newIntent)
        finish()
    }

//    fun loadUserInfo(userId: String) {
//        val database = Firebase.database
//        val myRef = database.reference
//
//        myRef.child("users").child(userId).get().addOnSuccessListener {
//            Log.i("firebase", "Got value ${it.value}")
//            this.userId = userId
//            userPw = it.child("userPw").value as String
//            userNickname = it.child("userNickname").value as String
//            userAge = it.child("userAge").value as Long
//            userJoinRoute1 = it.child("userJoinRoute").value as MutableSet<String>
//        }.addOnFailureListener {
//            Log.e("firebase", "Error getting data", it)
//        }
//    }
}