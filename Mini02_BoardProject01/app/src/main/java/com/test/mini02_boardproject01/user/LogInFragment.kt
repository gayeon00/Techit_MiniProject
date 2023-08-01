package com.test.mini02_boardproject01.user

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.test.mini02_boardproject01.MainActivity
import com.test.mini02_boardproject01.R
import com.test.mini02_boardproject01.User
import com.test.mini02_boardproject01.board.BoardMainActivity
import com.test.mini02_boardproject01.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {
    lateinit var fragmentLogInBinding: FragmentLogInBinding
    lateinit var mainActivity: MainActivity
    var logInUserId = ""
    var logInUserPw = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentLogInBinding = FragmentLogInBinding.inflate(layoutInflater)


        fragmentLogInBinding.run {

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

    private fun checkUserValidateInFirebase() {
        val database = Firebase.database
        val userDataRef = database.getReference("users")

        //userId가 사용자가 입력한 아이디와 같은 데이터를 가져온다.
        userDataRef.orderByChild("userId").equalTo(logInUserId).get().addOnCompleteListener {
            //가져온 데이터가 있다면
            if (it.result.exists()) {

                for (c1 in it.result.children) {
                    //가져온 데이터에서 비밀번호를 가져온다.
                    val userPwFromDatabase = c1.child("userPw").value as String

                    //입력한 비밀번호와 현재 계정의 비밀번호가 다르다면
                    if (logInUserPw != userPwFromDatabase) {
                        val builder = MaterialAlertDialogBuilder(mainActivity)
                        builder.setTitle("로그인 오류")
                        builder.setMessage("잘못된 비밀번호 입니다")
                        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                            fragmentLogInBinding.textInputEditTextLoginUserPw.setText("")
                        }
                        builder.show()
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

                        mainActivity.loginUser = User(userIdx, userId, userPw, userNickname, userAge, userJoinRoute1, userJoinRoute2, userJoinRoute3, userJoinRoute4, userJoinRoute5)
                        Snackbar.make(fragmentLogInBinding.root, "로그인 되었습니다", Snackbar.LENGTH_SHORT).show()

                        goToBoardMainActivity()
                    }
                }

            }
            //가져온 데이터가 없다면
            else {
                val builder = MaterialAlertDialogBuilder(mainActivity)
                builder.setTitle("로그인 오류")
                builder.setMessage("존재하지 않는 아이디 입니다")
                builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                    fragmentLogInBinding.textInputEditTextLoginUserId.setText("")
                    fragmentLogInBinding.textInputEditTextLoginUserPw.setText("")
                }
                builder.show()
            }
        }
    }

    private fun goToBoardMainActivity() {
        val newIntent = Intent(mainActivity, BoardMainActivity::class.java)
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(newIntent)
        mainActivity.finish()
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
//
//        if (userId != savedUserId || userPw != savedUserPw) {
//            Toast.makeText(requireContext(), "아이디나 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
//            return false
//        }

        return true


    }
}