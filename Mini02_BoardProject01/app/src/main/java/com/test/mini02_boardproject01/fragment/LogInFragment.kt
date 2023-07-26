package com.test.mini02_boardproject01.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.test.mini02_boardproject01.R
import com.test.mini02_boardproject01.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {
    lateinit var fragmentLogInBinding: FragmentLogInBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentLogInBinding = FragmentLogInBinding.inflate(layoutInflater)


        fragmentLogInBinding.run {
            buttonGoToJoin.setOnClickListener {
                findNavController().navigate(R.id.joinFragment, null, navOptions {
                    anim {
                        enter = R.anim.enter_anim
                        exit = R.anim.exit_anim
                        popEnter = R.anim.pop_enter_anim
                        popExit = R.anim.pop_exit_anim
                    }
                })
            }
        }
        // Inflate the layout for this fragment
        return fragmentLogInBinding.root
    }
}