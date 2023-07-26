package com.test.mini02_boardproject01.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.test.mini02_boardproject01.MainActivity
import com.test.mini02_boardproject01.R
import com.test.mini02_boardproject01.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {
    lateinit var fragmentLogInBinding: FragmentLogInBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentLogInBinding = FragmentLogInBinding.inflate(layoutInflater)


        fragmentLogInBinding.run {
            buttonLogin.setOnClickListener {
                val userId = textInputEditTextLoginUserId.text.toString()
                val userPw = textInputEditTextLoginUserPw.text.toString()

                val pref = mainActivity.getSharedPreferences("data", Context.MODE_PRIVATE)
                val savedUserId = pref.getString("userId", "user")
                val savedUserPw = pref.getString("userPw", "user")

                if (userId == savedUserId && userPw == savedUserPw) {
                    textInputLayoutLoginUserId.error = null
                    textInputLayoutLoginUserPw.error = null

                    val navController = findNavController()
                    navController.popBackStack()
                    navController.navigate(R.id.boardMainFragment, null, navOptions {
                        anim {
                            enter = R.anim.enter_anim
                            exit = R.anim.exit_anim
                            popEnter = R.anim.pop_enter_anim
                            popExit = R.anim.pop_exit_anim
                        }
                    })
                }
                if (userId != savedUserId) {
                    textInputLayoutLoginUserId.error = "회원정보가 존재하지 않습니다."
                    textInputLayoutLoginUserPw.error = null
                }
                if (userId == savedUserId && userPw != savedUserPw) {
                    textInputLayoutLoginUserId.error = null
                    textInputLayoutLoginUserPw.error = "비밀번호가 일치하지 않습니다."
                }

            }
            buttonGoToJoin.setOnClickListener {
                findNavController().navigate(R.id.joinFragment, null, navOptions {
                    anim {
                        enter = R.anim.enter_anim
                        exit = R.anim.exit_anim
                        popEnter = R.anim.pop_enter_anim
                        popExit = R.anim.pop_exit_anim
                    }
                })
            }
        }
        // Inflate the layout for this fragment
        return fragmentLogInBinding.root
    }
}