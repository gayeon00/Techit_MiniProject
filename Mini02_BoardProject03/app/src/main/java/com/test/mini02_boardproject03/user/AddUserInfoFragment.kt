package com.test.mini02_boardproject03.user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.test.mini02_boardproject03.databinding.FragmentAddUserInfoBinding
import com.test.mini02_boardproject03.databinding.FragmentLogInBinding

class AddUserInfoFragment : Fragment() {
    lateinit var fragmentAddUserInfoBinding: FragmentAddUserInfoBinding

    val userJoinRouteCheck = Array<Boolean>(5) { false }
    val userJoinRouteString = arrayOf(
        "구글 플레이스토어",
        "SNS (인스타그램, 페이스북 등)",
        "유튜브",
        "지역카페 (맘카페 등)",
        "기타"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentAddUserInfoBinding = FragmentAddUserInfoBinding.inflate(layoutInflater)

        fragmentAddUserInfoBinding.run {
            checkBox.setOnCheckedChangeListener { compoundButton, b ->
                userJoinRouteCheck[0] = b
            }
            checkBox2.setOnCheckedChangeListener { compoundButton, b ->
                userJoinRouteCheck[1] = b
            }
            checkBox3.setOnCheckedChangeListener { compoundButton, b ->
                userJoinRouteCheck[2] = b
            }
            checkBox4.setOnCheckedChangeListener { compoundButton, b ->
                userJoinRouteCheck[3] = b
            }
            checkBox5.setOnCheckedChangeListener { compoundButton, b ->
                userJoinRouteCheck[4] = b
            }

            buttonCompleteJoin.setOnClickListener {
                (activity as MainActivity).hideSoftInput()

                // 가입하기
                val userId = requireArguments().getString("userId")
                val userPw = requireArguments().getString("userPw")

                val userNickname = textInputEditTextAddInfoName.text.toString()
                val userAge = textInputEditTextAddInfoAge.text.toString()

                val userJoinRoute = getUserJoinRoute()

                if (validateAddUerInfoFields(userNickname, userAge)) {
                    saveUserInfoToFirebase(userId!!, userPw!!, userNickname, userAge.toLong(), userJoinRoute)
                    //기기의 아이디를 식별하기 위해 preference에도 저장
                    saveUserIdPwToPreferences()

                    //boardActivity로 이동
                    (activity as MainActivity).goToBoardActivity()
                }
            }
        }
        // Inflate the layout for this fragment
        return fragmentAddUserInfoBinding.root
    }

    private fun saveUserIdPwToPreferences() {
        val preferences = requireActivity().getSharedPreferences("data", Context.MODE_PRIVATE)

        val editor = preferences.edit()
        editor.putString("userId", requireArguments().getString("userId"))
        editor.putString("userPw", requireArguments().getString("userPw"))
        editor.commit()
    }

    private fun saveUserInfoToFirebase(
        userId: String,
        userPw: String,
        userNickname: String,
        userAge: Long,
        userJoinRoute: List<String>
    ) {
        val user = User(userId, userPw, userNickname, userAge, userJoinRoute)

        val database = Firebase.database.reference
        //firebasedatabase에 저장
        database.child("users").child(userId).setValue(user)
    }

    private fun getUserJoinRoute(): List<String> {
        //체크된 항목의 boolean을 바탕으로 체크된 목록을 return함
        val tmp = mutableListOf<String>()
        for (idx in 0..4) {
            if (userJoinRouteCheck[idx]) {
                tmp.add(userJoinRouteString[idx])
            }
        }
        return tmp.toList()
    }

    private fun FragmentAddUserInfoBinding.validateAddUerInfoFields(userNickname: String, userAge: String): Boolean {
        if (userNickname.isEmpty()) {
            textInputLayoutAddInfoName.error = "닉네임을 입력해주세요."
            return false
        }

        if (userAge.isEmpty()) {
            textInputLayoutAddInfoName.error = null
            textInputLayoutAddInfoAge.error = "나이를 입력해주세요."
            return false
        }

        return true
    }
}