package com.test.mini02_boardproject01.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.test.mini02_boardproject01.R
import com.test.mini02_boardproject01.databinding.FragmentAddUserInfoBinding

class AddUserInfoFragment : Fragment() {
    lateinit var fragmentAddUserInfoBinding: FragmentAddUserInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentAddUserInfoBinding = FragmentAddUserInfoBinding.inflate(layoutInflater)
        fragmentAddUserInfoBinding.run {
            toolBarAddUserInfo.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
        // Inflate the layout for this fragment
        return fragmentAddUserInfoBinding.root
    }

}