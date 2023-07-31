package com.test.mini02_boardproject03.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.test.mini02_boardproject03.R
import com.test.mini02_boardproject03.databinding.FragmentJoinBinding
class JoinFragment : Fragment() {
    lateinit var fragmentJoinBinding: FragmentJoinBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentJoinBinding = FragmentJoinBinding.inflate(layoutInflater)

        fragmentJoinBinding.run {
            buttonGoToAddUserInfo.setOnClickListener {
                (activity as MainActivity).hideSoftInput()
                //유효성 검사 통과한 경우에만
                if (validateJoinFields()) {
                    //아이디, 비밀번호 AddUserInfoFragment로 전달
                    val bundle = Bundle()
                    bundle.putString("userId", textInputEditTextJoinUserId.text.toString())
                    bundle.putString("userPw", textInputEditTextJoinUserPw.text.toString())

                    findNavController().navigate(R.id.action_joinFragment_to_addUserInfoFragment, bundle)
                }

            }
        }
        return fragmentJoinBinding.root
    }

    private fun FragmentJoinBinding.validateJoinFields(): Boolean {
        val userId = textInputEditTextJoinUserId.text.toString().trim()
        val userPw = textInputEditTextJoinUserPw.text.toString().trim()
        val userPwRe = textInputEditTextJoinUserPwRe.text.toString().trim()

        if (userId.isEmpty()) {
            textInputLayoutJoinUserId.error = "아이디를 입력해주세요."
            textInputLayoutJoinUserPw.error = null
            textInputLayoutJoinUserPwRe.error = null
            return false
        }

        if (userPw.isEmpty()) {
            textInputLayoutJoinUserId.error = null
            textInputLayoutJoinUserPw.error = "비밀번호를 입력해주세요."
            textInputLayoutJoinUserPwRe.error = null
            return false
        }

        if (userPwRe.isEmpty()) {
            textInputLayoutJoinUserId.error = null
            textInputLayoutJoinUserPw.error = null
            textInputLayoutJoinUserPwRe.error = "비밀번호를 확인해주세요."
            return false
        }

        if (userPw != userPwRe) {
            textInputLayoutJoinUserId.error = null
            textInputLayoutJoinUserPw.error = "비밀번호가 일치하지 않습니다."
            textInputLayoutJoinUserPwRe.error = "비밀번호가 일치하지 않습니다."
            return false
        }

        // 로그인 유효성 검사 통과
        return true
    }
}