package com.test.mini02_boardproject01.ui.user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.test.mini02_boardproject01.R
import com.test.mini02_boardproject01.databinding.FragmentJoinBinding
import com.test.mini02_boardproject01.domain.UserViewModel
import com.test.mini02_boardproject01.ui.MainActivity

class JoinFragment : Fragment() {
    lateinit var fragmentJoinBinding: FragmentJoinBinding
    lateinit var mainActivity: MainActivity
    lateinit var userViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentJoinBinding = FragmentJoinBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        //viewModel 객체 가져오기
        userViewModel = ViewModelProvider(mainActivity)[UserViewModel::class.java]

        // Inflate the layout for this fragment
        fragmentJoinBinding.run {

            //userViewModel 감시자 달아주기
            userViewModel.run {
                userId.observe(mainActivity) {
                    textInputEditTextJoinUserId.setText(it)
                }
                userPw.observe(mainActivity) {
                    textInputEditTextJoinUserPw.setText(it)
                }
                userPw2.observe(mainActivity) {
                    textInputEditTextJoinUserPwRe.setText(it)
                }
            }

            buttonGoToAddUserInfo.setOnClickListener {
                validateLoginFields()
            }

            toolBarJoin.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
        return fragmentJoinBinding.root
    }

    override fun onResume() {
        super.onResume()

        userViewModel.reset()
    }

    private fun FragmentJoinBinding.validateLoginFields() {
        val userId = textInputEditTextJoinUserId.text.toString()
        val userPw = textInputEditTextJoinUserPw.text.toString()
        val userPwRe = textInputEditTextJoinUserPwRe.text.toString()

        if (userId.isEmpty()) {
            textInputLayoutJoinUserId.error = "아이디를 입력해주세요."
            return
        }

        if (userPw.isEmpty()) {
            textInputLayoutJoinUserPw.error = "비밀번호를 입력해주세요."
            return
        }

        if (userPwRe.isEmpty()) {
            textInputLayoutJoinUserPwRe.error = "비밀번호를 확인해주세요."
            return
        }

        if (userPw != userPwRe) {
            textInputLayoutJoinUserPw.error = "비밀번호가 일치하지 않습니다."
            textInputLayoutJoinUserPwRe.error = "비밀번호가 일치하지 않습니다."
            return
        }

        if (userId != "" && userPw != "" && userPw == userPwRe) {
            mainActivity.userId = userId
            mainActivity.userPw = userPw

            findNavController().navigate(R.id.addUserInfoFragment)
        } else {
            textInputLayoutJoinUserPwRe.error = "비밀번호가 일치하지 않습니다."
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //id입력 창에 focus주기
        fragmentJoinBinding.run {
            textInputEditTextJoinUserId.run {
                requestFocus()
                //focus준 곳에 키보드 올리기

                val imm =
                    mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)

                setOnEditorActionListener { textView, i, keyEvent ->
                    if (textView.text.toString() == "") {
                        textInputLayoutJoinUserId.error = "아이디를 입력해주세요."
                    } else {
                        textInputLayoutJoinUserId.error = null
                        textInputEditTextJoinUserPw.requestFocus()
                    }
                    true
                }
            }

            textInputEditTextJoinUserPw.setOnEditorActionListener { textView, i, keyEvent ->
                if (textView.text.toString() == "") {
                    textInputLayoutJoinUserPw.error = "비밀번호를 입력해주세요"
                } else {
                    textInputLayoutJoinUserPw.error = null
                    textInputEditTextJoinUserPwRe.requestFocus()
                }
                true
            }

            textInputEditTextJoinUserPwRe.setOnEditorActionListener { textView, i, keyEvent ->
                validateLoginFields()
                true
            }
        }
    }

}