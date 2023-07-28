package com.test.mini02_boardproject01.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.test.mini02_boardproject01.R
import com.test.mini02_boardproject01.databinding.FragmentPostReadBinding

class PostReadFragment : Fragment() {
    lateinit var fragmentPostReadBinding: FragmentPostReadBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentPostReadBinding = FragmentPostReadBinding.inflate(layoutInflater)
        // Hide the ActionBar
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.hide()


        fragmentPostReadBinding.run {
            materialToolbar2.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.item_edit -> {
                        findNavController().navigate(R.id.action_postReadFragment_to_postModifyFragment)
                    }
                    R.id.item_delete -> {
                        findNavController().navigate(R.id.action_postReadFragment_to_postListFragment)
                    }
                }
                true
            }

            materialToolbar2.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
        return fragmentPostReadBinding.root
    }

}