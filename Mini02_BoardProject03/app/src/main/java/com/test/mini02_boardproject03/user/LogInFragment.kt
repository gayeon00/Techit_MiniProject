package com.test.mini02_boardproject03.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.test.mini02_boardproject03.R
import com.test.mini02_boardproject03.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {
    lateinit var fragmentLogInBinding: FragmentLogInBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentLogInBinding = FragmentLogInBinding.inflate(layoutInflater)
        fragmentLogInBinding.run {
            buttonGoToJoin.setOnClickListener {
                findNavController().navigate(R.id.action_logInFragment_to_joinFragment)
            }

            buttonLogin.setOnClickListener {

            }
        }
        return fragmentLogInBinding.root
    }
}