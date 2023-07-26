package com.test.mini02_boardproject01.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.test.mini02_boardproject01.R
import com.test.mini02_boardproject01.databinding.FragmentJoinBinding

class JoinFragment : Fragment() {
    lateinit var fragmentJoinBinding: FragmentJoinBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentJoinBinding = FragmentJoinBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        fragmentJoinBinding.run {
            buttonGoToAddUserInfo.setOnClickListener {
                findNavController().navigate(R.id.addUserInfoFragment, null, navOptions {
                    anim {
                        enter = R.anim.enter_anim
                        exit = R.anim.exit_anim
                        popEnter = R.anim.pop_enter_anim
                        popExit = R.anim.pop_exit_anim
                    }
                })
            }

            toolBarJoin.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
        return fragmentJoinBinding.root
    }
}