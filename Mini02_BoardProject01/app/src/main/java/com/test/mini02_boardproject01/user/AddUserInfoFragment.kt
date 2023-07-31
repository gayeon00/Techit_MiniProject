package com.test.mini02_boardproject01.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.test.mini02_boardproject01.MainActivity
import com.test.mini02_boardproject01.board.BoardMainActivity
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

        mainActivity.userNickname = name
        mainActivity.userAge = age

        val result = mutableListOf<String>()
        for (idx in 0 until 5) {
            if (userJoinRouteCheck[idx]) result.add(userJoinRoute[idx])
        }

        mainActivity.userJoinRoute = result.toMutableSet()

        mainActivity.savePreference()

        val newIntent = Intent(mainActivity, BoardMainActivity::class.java)
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(newIntent)
        mainActivity.finish()
    }

}