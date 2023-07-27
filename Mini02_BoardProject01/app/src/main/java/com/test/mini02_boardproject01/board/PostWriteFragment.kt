package com.test.mini02_boardproject01.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return fragmentPostWriteBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.write, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //누른 거에 따라 작업 다르게 해주기
        return when (item.itemId) {
            R.id.item_camera -> {
                true
            }

            R.id.item_gallery -> {
                true
            }

            R.id.item_complete -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}