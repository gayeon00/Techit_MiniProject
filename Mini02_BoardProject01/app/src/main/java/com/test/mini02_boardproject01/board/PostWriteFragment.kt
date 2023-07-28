package com.test.mini02_boardproject01.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.test.mini02_boardproject01.R
import com.test.mini02_boardproject01.databinding.FragmentPostWriteBinding

class PostWriteFragment : Fragment() {
    lateinit var fragmentPostWriteBinding: FragmentPostWriteBinding
    lateinit var boardMainActivity: BoardMainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentPostWriteBinding = FragmentPostWriteBinding.inflate(layoutInflater)
        boardMainActivity = activity as BoardMainActivity

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.hide()

        fragmentPostWriteBinding.run {
            materialToolbar4.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.item_camera -> {
                    }
                    R.id.item_gallery -> {
                    }
                    R.id.item_complete -> {
                        findNavController().navigate(R.id.action_postWriteFragment_to_postReadFragment)
                    }
                }
                true
            }

            materialToolbar4.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
        // Inflate the layout for this fragment
        return fragmentPostWriteBinding.root
    }

}