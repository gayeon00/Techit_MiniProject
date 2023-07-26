package com.test.mini02_boardproject01

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
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
import com.test.mini02_boardproject01.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding
    lateinit var navController: NavController
    var preferences: SharedPreferences? = null

    var userId = ""
    var userPw = ""
    var userNickname = ""
    var userAge = ""
    var userJoinRoute = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        splashScreenCustomizing(splashScreen)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        navController = findNavController(R.id.nav_host_fragment)
        preferences = getSharedPreferences("data", MODE_PRIVATE)

        if(preferences!=null) {
            userId = preferences!!.getString("userId","user").toString()
            userPw = preferences!!.getString("userPw","user").toString()
            userNickname = preferences!!.getString("userNickname","user").toString()
            userAge = preferences!!.getString("userAge","user").toString()
            userJoinRoute = preferences!!.getStringSet("userJoinRoute",
                mutableSetOf<String>()) as MutableSet<String>
        }
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

    fun savePreference() {
        val editor = preferences!!.edit()
        editor.putString("userId", userId)
        editor.putString("userPw", userPw)
        editor.putString("userNickname", userNickname)
        editor.putString("userAge", userAge)
        editor.putStringSet("userJoinRoute", userJoinRoute.toSet())

        editor.apply()
    }
}