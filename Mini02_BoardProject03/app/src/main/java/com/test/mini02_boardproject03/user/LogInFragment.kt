package com.test.mini02_boardproject03.user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.test.mini02_boardproject03.R
import com.test.mini02_boardproject03.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {
    lateinit var fragmentLogInBinding: FragmentLogInBinding

    private var answerId = ""
    private var answerPw = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loadUserIdPwFromPreferences()

        fragmentLogInBinding = FragmentLogInBinding.inflate(layoutInflater)
        fragmentLogInBinding.run {
            buttonGoToJoin.setOnClickListener {
                findNavController().navigate(R.id.action_logInFragment_to_joinFragment)
            }

            buttonLogin.setOnClickListener {
                (activity as MainActivity).hideSoftInput()
                //유효성 검사
                if (validateLoginFields()) {
                    //boardActivity로 이동
                    (activity as MainActivity).goToBoardActivity()
                }
            }
        }
        return fragmentLogInBinding.root
    }


    private fun loadUserIdPwFromPreferences() {
        val preferences = requireActivity().getSharedPreferences("data", Context.MODE_PRIVATE)

        if (preferences != null) {
            answerId = preferences.getString("userId", "user").toString()
            answerPw = preferences.getString("userPw", "user").toString()
        }
    }


    private fun FragmentLogInBinding.validateLoginFields(): Boolean {
        val userId = textInputEditTextLoginUserId.text.toString().trim()
        val userPw = textInputEditTextLoginUserPw.text.toString().trim()

        if (userId.isEmpty()) {
            textInputLayoutLoginUserId.error = "아이디를 입력해주세요."
            textInputLayoutLoginUserPw.error = null
            return false
        }

        if (userPw.isEmpty()) {
            textInputLayoutLoginUserId.error = null
            textInputLayoutLoginUserPw.error = "비밀번호를 입력해주세요."
            return false
        }

        if (userId != answerId || userPw != answerPw) {
            textInputLayoutLoginUserId.error = null
            textInputLayoutLoginUserPw.error = null
            Snackbar.make(fragmentLogInBinding.root, "아이디나 비밀번호가 일치하지 않습니다.", Snackbar.LENGTH_SHORT).show()
            return false
        }

        // 로그인 유효성 검사 통과
        return true
    }
}