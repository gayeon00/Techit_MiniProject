package com.test.mini02_boardproject01.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.test.mini02_boardproject01.R
import com.test.mini02_boardproject01.databinding.FragmentPostModifyBinding

class PostModifyFragment : Fragment() {
    lateinit var fragmentPostModifyBinding: FragmentPostModifyBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentPostModifyBinding = FragmentPostModifyBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment


        fragmentPostModifyBinding.run {
            materialToolbar3.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            materialToolbar3.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.item_modify_camera -> {}
                    R.id.item_modify_gallery -> {}
                    R.id.item_modify_complete -> {
                        findNavController().navigate(R.id.action_postModifyFragment_to_postReadFragment)
                    }
                }
                true
            }
        }
        return fragmentPostModifyBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Hide the ActionBar
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.hide()
    }


}