package com.test.mini02_boardproject01.ui.board

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.mini02_boardproject01.R
import com.test.mini02_boardproject01.data.repository.UserRepository
import com.test.mini02_boardproject01.databinding.FragmentModifyUserBinding
import com.test.mini02_boardproject01.domain.UserViewModel

class ModifyUserFragment : Fragment() {
    lateinit var fragmentModifyUerBinding: FragmentModifyUserBinding
    lateinit var boardMainActivity: BoardMainActivity
    lateinit var userViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentModifyUerBinding = FragmentModifyUserBinding.inflate(layoutInflater)
        boardMainActivity = activity as BoardMainActivity

        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        fragmentModifyUerBinding.run {
            userViewModel.run {
                userNickname.observe(viewLifecycleOwner) {
                    textInputEditTextModifyUserNickname.setText(it)
                }
                userAge.observe(viewLifecycleOwner) {
                    textInputEditTextModifyUserAge.setText(it.toString())
                }
                hobby1.observe(viewLifecycleOwner) {
                    checkBox10.isChecked = it
                }
                hobby2.observe(viewLifecycleOwner) {
                    checkBox12.isChecked = it
                }
                hobby3.observe(viewLifecycleOwner) {
                    checkBox13.isChecked = it
                }
                hobby4.observe(viewLifecycleOwner) {
                    checkBox14.isChecked = it
                }
                hobby5.observe(viewLifecycleOwner) {
                    checkBox15.isChecked = it
                }

                //로그인한 사용자의 정보를 담아준다.
                userNickname.value = boardMainActivity.loginUser.userNickname
                userAge.value = boardMainActivity.loginUser.userAge
                hobby1.value = boardMainActivity.loginUser.userJoinRoute1
                hobby2.value = boardMainActivity.loginUser.userJoinRoute2
                hobby3.value = boardMainActivity.loginUser.userJoinRoute3
                hobby4.value = boardMainActivity.loginUser.userJoinRoute4
                hobby5.value = boardMainActivity.loginUser.userJoinRoute5

            }
            //수정 완료 버튼
            buttonModifyUserAccept.setOnClickListener {
                //입력한 내용을 가져온다.
                val modifyUserPw1 = textInputEditTextModifyUserPw1.text.toString()
                val modifyUserPw2 = textInputEditTextModifyUserPw2.text.toString()
                val modifyUserNickname = textInputEditTextModifyUserNickname.text.toString()
                val modifyUserAge = textInputEditTextModifyUserAge.text.toString()
                val modifyJoinRoute1 = checkBox10.isChecked
                val modifyJoinRoute2 = checkBox12.isChecked
                val modifyJoinRoute3 = checkBox13.isChecked
                val modifyJoinRoute4 = checkBox14.isChecked
                val modifyJoinRoute5 = checkBox15.isChecked

                if (modifyUserPw1.isNotEmpty() || modifyUserPw2.isNotEmpty()) {
                    if (modifyUserPw1 != modifyUserPw2) {
                        val builder = MaterialAlertDialogBuilder(boardMainActivity)
                        builder.setTitle("비빌번호 오류")
                        builder.setMessage("비밀번호가 다릅니다.")
                        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                            textInputEditTextModifyUserPw1.setText("")
                            textInputEditTextModifyUserPw2.setText("")

                            val imm =
                                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.showSoftInput(
                                textInputEditTextModifyUserPw1,
                                InputMethodManager.SHOW_IMPLICIT
                            )
                        }
                        builder.show()
                        return@setOnClickListener
                    }
                }

                if (modifyUserNickname.isEmpty()) {
                    val builder = MaterialAlertDialogBuilder(boardMainActivity)
                    builder.setTitle("닉네임 입력 오류")
                    builder.setMessage("닉네임을 입력해주세요")
                    builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                        val imm =
                            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.showSoftInput(
                            textInputEditTextModifyUserNickname,
                            InputMethodManager.SHOW_IMPLICIT
                        )
                    }
                    builder.show()
                    return@setOnClickListener
                }

                if (modifyUserAge.isEmpty()) {
                    val builder = MaterialAlertDialogBuilder(boardMainActivity)
                    builder.setTitle("나이 입력 오류")
                    builder.setMessage("나이를 입력해주세요")
                    builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                        val imm =
                            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.showSoftInput(
                            textInputEditTextModifyUserAge,
                            InputMethodManager.SHOW_IMPLICIT
                        )
                    }
                    builder.show()
                    return@setOnClickListener
                }

                //입력한 내용들을 담아줌
                if (modifyUserPw1.isNotEmpty() && modifyUserPw2.isNotEmpty()) {
                    if (modifyUserPw1 == modifyUserPw2) {
                        boardMainActivity.loginUser.userPw = modifyUserPw1
                    }
                }

                boardMainActivity.loginUser.userNickname = modifyUserNickname
                boardMainActivity.loginUser.userAge = modifyUserAge.toLong()
                boardMainActivity.loginUser.userJoinRoute1 = modifyJoinRoute1
                boardMainActivity.loginUser.userJoinRoute2 = modifyJoinRoute2
                boardMainActivity.loginUser.userJoinRoute3 = modifyJoinRoute3
                boardMainActivity.loginUser.userJoinRoute4 = modifyJoinRoute4
                boardMainActivity.loginUser.userJoinRoute5 = modifyJoinRoute5

                //저장하기
                UserRepository.modifyUserInfo(boardMainActivity.loginUser)
                findNavController().navigate(R.id.action_item_board_main_user_info_to_postListFragment)


            }
        }
        return fragmentModifyUerBinding.root
    }
}