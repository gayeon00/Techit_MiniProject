package com.test.mini02_boardproject01.ui.board

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.mini02_boardproject01.R
import com.test.mini02_boardproject01.databinding.FragmentModifyUserBinding

class ModifyUserFragment : Fragment() {
    lateinit var fragmentModifyUerBinding: FragmentModifyUserBinding
    lateinit var boardMainActivity: BoardMainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentModifyUerBinding = FragmentModifyUserBinding.inflate(layoutInflater)
        boardMainActivity = activity as BoardMainActivity

        fragmentModifyUerBinding.run {
            //수정 완료 버튼
            buttonModifyUserAccept.setOnClickListener {
                //입력한 내용을 가져온다.
                val modifyUserPw1 = textInputEditTextModifyUserPw1.text.toString()
                val modifyUserPw2 = textInputEditTextModifyUserPw2.text.toString()
                val modifyUserNickname = textInputEditTextModifyUserNickname.text.toString()
                val modifyUserAge = textInputEditTextModifyUserAge.text.toString()

                if(modifyUserPw1.isNotEmpty() || modifyUserPw2.isNotEmpty()){
                    if(modifyUserPw1 != modifyUserPw2){
                        val builder = MaterialAlertDialogBuilder(boardMainActivity)
                        builder.setTitle("비빌번호 오류")
                        builder.setMessage("비밀번호가 다릅니다.")
                        builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                            textInputEditTextModifyUserPw1.setText("")
                            textInputEditTextModifyUserPw2.setText("")

                            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.showSoftInput(textInputEditTextModifyUserPw1, InputMethodManager.SHOW_IMPLICIT)
                        }
                        builder.show()
                        return@setOnClickListener
                    }
                }

                if(modifyUserNickname.isEmpty()){
                    val builder = MaterialAlertDialogBuilder(boardMainActivity)
                    builder.setTitle("닉네임 입력 오류")
                    builder.setMessage("닉네임을 입력해주세요")
                    builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.showSoftInput(textInputEditTextModifyUserNickname, InputMethodManager.SHOW_IMPLICIT)
                    }
                    builder.show()
                    return@setOnClickListener
                }

                if(modifyUserAge.isEmpty()){
                    val builder = MaterialAlertDialogBuilder(boardMainActivity)
                    builder.setTitle("나이 입력 오류")
                    builder.setMessage("나이를 입력해주세요")
                    builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.showSoftInput(textInputEditTextModifyUserAge, InputMethodManager.SHOW_IMPLICIT)
                    }
                    builder.show()
                    return@setOnClickListener
                }


            }
        }
        return fragmentModifyUerBinding.root
    }
}