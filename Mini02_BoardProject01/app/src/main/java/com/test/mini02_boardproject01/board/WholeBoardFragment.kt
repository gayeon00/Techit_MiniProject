package com.test.mini02_boardproject01.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.test.mini02_boardproject01.MainActivity
import com.test.mini02_boardproject01.databinding.FragmentWholeBoardBinding

class WholeBoardFragment : Fragment() {
    lateinit var fragmentBoardMainBinding: FragmentWholeBoardBinding
    lateinit var boardMainActivity: BoardMainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        fragmentBoardMainBinding = FragmentWholeBoardBinding.inflate(layoutInflater)
        boardMainActivity = activity as BoardMainActivity

        fragmentBoardMainBinding.run {

        }
        return fragmentBoardMainBinding.root


    }
}