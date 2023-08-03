package com.test.mini02_boardproject01.ui.user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.test.mini02_boardproject01.data.model.User
import com.test.mini02_boardproject01.data.repository.UserRepository
import com.test.mini02_boardproject01.ui.MainActivity
import com.test.mini02_boardproject01.databinding.FragmentAddUserInfoBinding

class AddUserInfoFragment : Fragment() {
    lateinit var fragmentAddUserInfoBinding: FragmentAddUserInfoBinding
    lateinit var mainActivity: MainActivity

    val userJoinRouteCheck = Array<Boolean>(5) { false }
    val userJoinRoute = arrayOf(
        "구글 플레이스토어",
        "SNS (인스타그램, 페이스북 등)",
        "유튜브",
        "지역카페 (맘카페 등)",
        "기타"
    )

    var userId = ""
    var userPw = ""
    var userNickname = ""
    var userAge = 0L
    var userJoinRoute1 = false
    var userJoinRoute2 = false
    var userJoinRoute3 = false
    var userJoinRoute4 = false
    var userJoinRoute5 = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentAddUserInfoBinding = FragmentAddUserInfoBinding.inflate(layoutInflater)
        fragmentAddUserInfoBinding.run {
            toolBarAddUserInfo.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            buttonCompleteJoin.setOnClickListener {
                validateFieldsAndSave()
            }

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

        }
        // Inflate the layout for this fragment
        return fragmentAddUserInfoBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentAddUserInfoBinding.run {
            textInputEditTextAddInfoName.run {
                requestFocus()

                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)

            }
        }
    }

    private fun FragmentAddUserInfoBinding.validateFieldsAndSave() {
        val name = textInputEditTextAddInfoName.text.toString()
        val age = textInputEditTextAddInfoAge.text.toString()

        if (name.isEmpty()) {
            textInputLayoutAddInfoName.error = "닉네임을 입력해주세요."
            return
        }

        if (age.isEmpty()) {
            textInputLayoutAddInfoAge.error = "나이를 입력해주세요."
            return
        }

        userNickname = name
        userAge = age.toLong()


        userJoinRoute1 = userJoinRouteCheck[0]
        userJoinRoute2 = userJoinRouteCheck[1]
        userJoinRoute3 = userJoinRouteCheck[2]
        userJoinRoute4 = userJoinRouteCheck[3]
        userJoinRoute5 = userJoinRouteCheck[4]



        saveUserInfo() {
            Snackbar.make(fragmentAddUserInfoBinding.root, "가입이 완료됐습니다.", Snackbar.LENGTH_SHORT).show()
            mainActivity.goToBoardMainActivity()
        }


    }

    private fun saveUserInfo(function: () -> Unit) {
        var userIdx = 0L

        UserRepository.getUserIdx { task ->
            //현재 사용자 순서 값을 가져온다.
            userIdx = task.result.value as Long

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

            //저장한다.
            UserRepository.addUserInfo(user) {
                UserRepository.setUserIdx(userIdx) {
                    function()
                }
            }
        }


    }

}