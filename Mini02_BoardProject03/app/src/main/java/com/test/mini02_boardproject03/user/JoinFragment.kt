package com.test.mini02_boardproject03.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.test.mini02_boardproject03.R
import com.test.mini02_boardproject03.databinding.FragmentJoinBinding

class JoinFragment : Fragment() {
    lateinit var fragmentJoinBinding: FragmentJoinBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentJoinBinding = FragmentJoinBinding.inflate(layoutInflater)

        fragmentJoinBinding.run {
            buttonGoToAddUserInfo.setOnClickListener {
                //아이디, 비밀번호 AddUserInfoFragment로 전달
                val bundle = Bundle()
                bundle.putString("userId", textInputEditTextJoinUserId.text.toString())
                bundle.putString("userPw", textInputEditTextJoinUserPw.text.toString())

                findNavController().navigate(R.id.action_joinFragment_to_addUserInfoFragment, bundle)
            }
        }
        return fragmentJoinBinding.root
    }
}