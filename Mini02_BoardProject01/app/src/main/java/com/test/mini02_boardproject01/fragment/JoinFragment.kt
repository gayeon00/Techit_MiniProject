package com.test.mini02_boardproject01.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.test.mini02_boardproject01.MainActivity
import com.test.mini02_boardproject01.R
import com.test.mini02_boardproject01.databinding.FragmentJoinBinding

class JoinFragment : Fragment() {
    lateinit var fragmentJoinBinding: FragmentJoinBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentJoinBinding = FragmentJoinBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        // Inflate the layout for this fragment
        fragmentJoinBinding.run {
            buttonGoToAddUserInfo.setOnClickListener {
                val userId = textInputEditTextJoinUserId.text.toString()
                val userPw = textInputEditTextJoinUserPw.text.toString()
                val userPwRe = textInputEditTextJoinUserPwRe.text.toString()

                if (userPw == userPwRe) {
                    mainActivity.userId = userId
                    mainActivity.userPw = userPw

                    findNavController().navigate(R.id.addUserInfoFragment, null, navOptions {
                        anim {
                            enter = R.anim.enter_anim
                            exit = R.anim.exit_anim
                            popEnter = R.anim.pop_enter_anim
                            popExit = R.anim.pop_exit_anim
                        }
                    })
                } else {
                    textInputLayoutJoinUserPwRe.error = "비밀번호가 일치하지 않습니다."
                }


            }

            toolBarJoin.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
        return fragmentJoinBinding.root
    }
}