package com.test.mini02_boardproject01.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.mini02_boardproject01.R
import com.test.mini02_boardproject01.databinding.FragmentPostListBinding

class PostListFragment : Fragment() {
    lateinit var fragmentPostListBinding: FragmentPostListBinding
    lateinit var boardMainActivity: BoardMainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentPostListBinding = FragmentPostListBinding.inflate(layoutInflater)
        boardMainActivity = activity as BoardMainActivity
        // Inflate the layout for this fragment
        return fragmentPostListBinding.root
    }
}