package com.test.mini02_boardproject01.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.test.mini02_boardproject01.MainActivity
import com.test.mini02_boardproject01.R
import com.test.mini02_boardproject01.board.BoardMainActivity
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
                validateLoginFields()

            }
            buttonGoToJoin.setOnClickListener {
                findNavController().navigate(R.id.action_logInFragment_to_joinFragment)
            }
        }
        // Inflate the layout for this fragment
        return fragmentLogInBinding.root
    }

    private fun FragmentLogInBinding.validateLoginFields() {
        val userId = textInputEditTextLoginUserId.text.toString()
        val userPw = textInputEditTextLoginUserPw.text.toString()

        val pref = mainActivity.getSharedPreferences("data", Context.MODE_PRIVATE)
        val savedUserId = pref.getString("userId", "user")
        val savedUserPw = pref.getString("userPw", "user")

        if (userId.isEmpty()) {
            textInputLayoutLoginUserId.error = "아이디를 입력해주세요."
            return
        }

        if (userPw.isEmpty()) {
            textInputLayoutLoginUserPw.error = "비밀번호를 입력해주세요."
            return
        }

        if (userId != savedUserId || userPw != savedUserPw) {
            Toast.makeText(requireContext(), "아이디나 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val newIntent = Intent(mainActivity, BoardMainActivity::class.java)
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(newIntent)
        mainActivity.finish()
    }
}