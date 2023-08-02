package com.test.mini02_boardproject01.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.test.mini02_boardproject01.R
import com.test.mini02_boardproject01.databinding.FragmentPostReadBinding
import com.test.mini02_boardproject01.domain.PostViewModel

class PostReadFragment : Fragment() {

    lateinit var fragmentPostReadBinding: FragmentPostReadBinding
    lateinit var postViewModel: PostViewModel

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

        postViewModel = ViewModelProvider(requireActivity())[PostViewModel::class.java]
//        //viewmodel의 title, content 업데이트
//        val arg = arguments?.getLong("postIdx")
//        postViewModel.getOne(arg!!)


        fragmentPostReadBinding.run {
            materialToolbar2.run{
                setOnMenuItemClickListener {
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

                setNavigationOnClickListener {
                    findNavController().navigate(R.id.action_postReadFragment_to_postListFragment)
                }
            }

            //감시자 달아줌
            postViewModel.run {
                title.observe(requireActivity()) {
                    textInputEditTextPostReadTitle.setText(it)
                }
                content.observe(requireActivity()) {
                    textInputEditTextPostReadContent.setText(it)
                }
                image.observe(requireActivity()) {
                    imageViewPostRead.setImageBitmap(it)
                }
            }

        }


        return fragmentPostReadBinding.root
    }



}