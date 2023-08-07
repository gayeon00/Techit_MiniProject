package com.test.mini02_boardproject01.ui.board

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
import com.test.mini02_boardproject01.data.repository.PostRepository
import com.test.mini02_boardproject01.databinding.FragmentPostReadBinding
import com.test.mini02_boardproject01.domain.PostViewModel

class PostReadFragment : Fragment() {

    lateinit var fragmentPostReadBinding: FragmentPostReadBinding
    lateinit var postViewModel: PostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
        //viewmodel의 title, content 업데이트
        //게시글 인덱스 번호를 받는다.
        val postIdx = arguments?.getLong("postIdx")!!
        postViewModel.setPostReadData(postIdx)


        fragmentPostReadBinding.run {
            materialToolbar2.run {
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.item_edit -> {
                            val arg = Bundle()
                            arg.putBoolean("isModify", true)
                            arg.putLong("postIdx", postIdx)
                            findNavController().navigate(
                                R.id.action_postReadFragment_to_postWriteFragment, arg
                            )
                        }

                        R.id.item_delete -> {
                            //글 삭제
                            PostRepository.removePost(postIdx) {
                                //이미지가 있으면 삭제
                                if (postViewModel.fileName.value != "None") {
                                    PostRepository.removeImage(postViewModel.fileName.value!!) {
                                        findNavController().navigate(R.id.action_postReadFragment_to_postListFragment)
                                    }
                                } else {
                                    findNavController().navigate(R.id.action_postReadFragment_to_postListFragment)
                                }
                            }

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
                    imageViewPostRead.visibility = View.VISIBLE
                    imageViewPostRead.setImageBitmap(it)
                }
                date.observe(requireActivity()) {
                    textInputEditTextPostReadDate.setText(it)
                }
                writer.observe(requireActivity()) {
                    textInputEditTextPostReadWriter.setText(it)
                }
                fileName.observe(requireActivity()) {
                    if (it == "None") {
                        imageViewPostRead.visibility = View.GONE
                    }
                }
            }

        }


        return fragmentPostReadBinding.root
    }


}