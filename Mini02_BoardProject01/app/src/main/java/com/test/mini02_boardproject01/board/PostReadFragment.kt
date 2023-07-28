package com.test.mini02_boardproject01.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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

// 뒤로가기 버튼 이벤트 처리
        requireActivity().onBackPressedDispatcher.addCallback(this) {

            // 뒤로가기 버튼을 눌렀을 때 항상 'a' 프래그먼트로 이동
            findNavController().navigate(R.id.action_postReadFragment_to_postListFragment)

        }


        fragmentPostReadBinding.run {
            materialToolbar2.setOnMenuItemClickListener {
                when (it.itemId) {
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
                findNavController().navigate(R.id.action_postReadFragment_to_postListFragment)
            }
        }
        return fragmentPostReadBinding.root
    }


}