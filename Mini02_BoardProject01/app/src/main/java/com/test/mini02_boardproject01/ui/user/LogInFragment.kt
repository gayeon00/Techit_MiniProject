package com.test.mini02_boardproject01.ui.user

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.test.mini02_boardproject01.R
import com.test.mini02_boardproject01.data.model.User
import com.test.mini02_boardproject01.data.repository.UserRepository
import com.test.mini02_boardproject01.databinding.FragmentLogInBinding
import com.test.mini02_boardproject01.domain.UserViewModel
import com.test.mini02_boardproject01.ui.MainActivity

class LogInFragment : Fragment() {
    lateinit var fragmentLogInBinding: FragmentLogInBinding
    lateinit var mainActivity: MainActivity
    var logInUserId = ""
    var logInUserPw = ""

    lateinit var userViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentLogInBinding = FragmentLogInBinding.inflate(layoutInflater)

        userViewModel = ViewModelProvider(mainActivity)[UserViewModel::class.java]

        fragmentLogInBinding.run {

            //감시자 달아주기
            userViewModel.run {
                userId.observe(mainActivity) {
                    textInputEditTextLoginUserId.setText(it)
                }
                userPw.observe(mainActivity) {
                    textInputEditTextLoginUserPw.setText(it)
                }
            }
            buttonLogin.setOnClickListener {
                logInUserId = textInputEditTextLoginUserId.text.toString()
                logInUserPw = textInputEditTextLoginUserPw.text.toString()

                if (validateLoginFields()) {
                    checkUserValidateInFirebase()
                }


            }
            buttonGoToJoin.setOnClickListener {
                findNavController().navigate(R.id.action_logInFragment_to_joinFragment)
            }
        }
        // Inflate the layout for this fragment
        return fragmentLogInBinding.root
    }

    override fun onResume() {
        super.onResume()

        userViewModel.reset()
    }

    private fun checkUserValidateInFirebase() {

        UserRepository.getUserInfoByUserId(logInUserId) {
            //가져온 데이터가 있다면
            if (it.result.exists()) {

                for (c1 in it.result.children) {
                    //가져온 데이터에서 비밀번호를 가져온다.
                    val userPwFromDatabase = c1.child("userPw").value as String

                    //입력한 비밀번호와 현재 계정의 비밀번호가 다르다면
                    if (logInUserPw != userPwFromDatabase) {
                        showWarningDialog()
                    }
                    // 입력한 비밀번호와 현재 계정의 비밀번호가 같다면
                    else {
                        //로그인한 사용자 정보를 가져온다.
                        // 로그인한 사용자 정보를 가져온다.
                        val userIdx = c1.child("userIdx").value as Long
                        val userId = c1.child("userId").value as String
                        val userPw = c1.child("userPw").value as String
                        val userNickname = c1.child("userNickname").value as String
                        val userAge = c1.child("userAge").value as Long
                        val userJoinRoute1 = c1.child("userJoinRoute1").value as Boolean
                        val userJoinRoute2 = c1.child("userJoinRoute2").value as Boolean
                        val userJoinRoute3 = c1.child("userJoinRoute3").value as Boolean
                        val userJoinRoute4 = c1.child("userJoinRoute4").value as Boolean
                        val userJoinRoute5 = c1.child("userJoinRoute5").value as Boolean

                        mainActivity.loginUser = User(
                            userIdx,
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
                        Snackbar.make(fragmentLogInBinding.root, "로그인 되었습니다", Snackbar.LENGTH_SHORT)
                            .show()

                        mainActivity.goToBoardMainActivity()
                    }
                }

            }
            //가져온 데이터가 없다면
            else {
                showWariningDialog2()
            }
        }
    }

    private fun showWariningDialog2() {
        val builder = MaterialAlertDialogBuilder(mainActivity)
        builder.setTitle("로그인 오류")
        builder.setMessage("존재하지 않는 아이디 입니다")
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
            fragmentLogInBinding.textInputEditTextLoginUserId.setText("")
            fragmentLogInBinding.textInputEditTextLoginUserPw.setText("")
        }
        builder.show()
    }

    private fun showWarningDialog() {
        val builder = MaterialAlertDialogBuilder(mainActivity)
        builder.setTitle("로그인 오류")
        builder.setMessage("잘못된 비밀번호 입니다")
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
            fragmentLogInBinding.textInputEditTextLoginUserPw.setText("")
        }
        builder.show()
    }

    private fun FragmentLogInBinding.validateLoginFields(): Boolean {
        val userId = textInputEditTextLoginUserId.text.toString()
        val userPw = textInputEditTextLoginUserPw.text.toString()

        val pref = mainActivity.getSharedPreferences("data", Context.MODE_PRIVATE)
        val savedUserId = pref.getString("userId", "user")
        val savedUserPw = pref.getString("userPw", "user")

        if (userId.isEmpty()) {
            textInputLayoutLoginUserId.error = "아이디를 입력해주세요."
            return false
        }

        if (userPw.isEmpty()) {
            textInputLayoutLoginUserPw.error = "비밀번호를 입력해주세요."
            return false
        }

        return true


    }
}