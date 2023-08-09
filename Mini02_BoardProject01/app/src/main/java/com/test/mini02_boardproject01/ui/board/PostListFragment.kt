package com.test.mini02_boardproject01.ui.board

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.search.SearchView
import com.test.mini02_boardproject01.R
import com.test.mini02_boardproject01.databinding.FragmentPostListBinding
import com.test.mini02_boardproject01.databinding.RowPostListBinding
import com.test.mini02_boardproject01.domain.PostViewModel

class PostListFragment : Fragment() {
    lateinit var fragmentPostListBinding: FragmentPostListBinding
    lateinit var boardMainActivity: BoardMainActivity
    lateinit var postViewModel: PostViewModel

    var postType = 0L
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentPostListBinding = FragmentPostListBinding.inflate(layoutInflater)
        boardMainActivity = activity as BoardMainActivity
        // Inflate the layout for this fragment

        // show the ActionBar
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.show()

        postType = arguments?.getLong("postType")!!
        postViewModel = ViewModelProvider(requireActivity())[PostViewModel::class.java]
        postViewModel.getPostAll(postType)

        fragmentPostListBinding.run {
            recyclerViewAll.run {
                adapter = RecyclerViewAdapter()
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(
                    MaterialDividerItemDecoration(
                        context,
                        MaterialDividerItemDecoration.VERTICAL
                    )
                )
            }

            recyclerViewSearchList.run {
                adapter = RecyclerViewAdapter()
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(
                    MaterialDividerItemDecoration(
                        context,
                        MaterialDividerItemDecoration.VERTICAL
                    )
                )
            }

            searchBar.setOnMenuItemClickListener {
                //메뉴 누르면 수행할 일
                val arg = Bundle()
                arg.putBoolean("isModify", false)
                findNavController().navigate(R.id.action_postListFragment_to_postWriteFragment)
                true
            }

            searchView.run {
                addTransitionListener { searchView, previousState, newState ->
                    // 서치바를 눌러 서치뷰가 보일 때
                    if (newState == SearchView.TransitionState.SHOWING) {
                        // Snackbar.make(fragmentPostListBinding.root, "Showing", Snackbar.LENGTH_SHORT).show()
                        postViewModel.resetPostList()
                    }
                    // 서치뷰의 백버튼을 눌러 서치뷰가 사라지고 서치바가 보일 때
                    else if (newState == SearchView.TransitionState.HIDING) {
                        // Snackbar.make(fragmentPostListBinding.root, "Hiding", Snackbar.LENGTH_SHORT).show()
                        postViewModel.getPostAll(postType)
                    }
                }

                editText.setOnEditorActionListener { textView, i, keyEvent ->
//                    Snackbar.make(fragmentPostListBinding.root, text!!, Snackbar.LENGTH_SHORT).show()
                    postViewModel.getSearchPostList(
                        arguments?.getLong("postType")!!,
                        text.toString()
                    )

                    true
                }
            }

            postViewModel.run {
                postList.observe(requireActivity()) {
                    recyclerViewAll.adapter?.notifyDataSetChanged()
                    recyclerViewSearchList.adapter?.notifyDataSetChanged()
                }
            }
        }
        return fragmentPostListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postViewModel.getPostAll(postType)

        postViewModel.postList.observe(requireActivity()) {
            fragmentPostListBinding.recyclerViewAll.adapter?.notifyDataSetChanged()
        }
    }

    inner class RecyclerViewAdapter : Adapter<RecyclerViewAdapter.AllViewHolder>() {
        inner class AllViewHolder(rowPostListBinding: RowPostListBinding) :
            ViewHolder(rowPostListBinding.root) {
            val rowPostListSubject = rowPostListBinding.rowPostListSubject
            val rowPostListNickName = rowPostListBinding.rowPostListNickName

            init {
                rowPostListBinding.root.setOnClickListener {
                    //글을 볼 수 있는 postreadfragment로 이동
                    //항목 번째 객체에서 글 번호를 가져온다.
                    val postIdx = postViewModel.postList.value!![adapterPosition].postIdx
                    Log.d("postIdx", postIdx.toString())
                    val arg = Bundle()
                    arg.putLong("postIdx", postIdx)
                    findNavController().navigate(
                        R.id.action_postListFragment_to_postReadFragment,
                        arg
                    )
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllViewHolder {
            val rowPostListBinding = RowPostListBinding.inflate(layoutInflater)
            rowPostListBinding.root.layoutParams = ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            return AllViewHolder(rowPostListBinding)
        }

        override fun getItemCount(): Int {
            return postViewModel.postList.value?.size!!
        }

        override fun onBindViewHolder(holder: AllViewHolder, position: Int) {
            holder.rowPostListSubject.text = postViewModel.postList.value!![position].postTitle
//            holder.rowPostListNickName.text = postViewModel.writerNicknameList.value!![position]
//            holder.rowPostListNickName.text = "df"
        }
    }
}