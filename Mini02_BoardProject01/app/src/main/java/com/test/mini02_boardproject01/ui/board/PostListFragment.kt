package com.test.mini02_boardproject01.ui.board

import android.os.Bundle
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
import com.test.mini02_boardproject01.R
import com.test.mini02_boardproject01.databinding.FragmentPostListBinding
import com.test.mini02_boardproject01.databinding.RowPostListBinding
import com.test.mini02_boardproject01.domain.PostViewModel

class PostListFragment : Fragment() {
    lateinit var fragmentPostListBinding: FragmentPostListBinding
    lateinit var boardMainActivity: BoardMainActivity
    lateinit var postViewModel: PostViewModel
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

        //TODO : recyclerView의 리스트 ViewModel로 연결해주기
        postViewModel = ViewModelProvider(requireActivity())[PostViewModel::class.java]

        fragmentPostListBinding.run {
            recyclerViewAll.run {
                adapter = AllRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(
                    MaterialDividerItemDecoration(
                        context,
                        MaterialDividerItemDecoration.VERTICAL
                    )
                )
            }

            recyclerViewSearchList.run {
                adapter = ResultRecyclerViewAdapter()
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
        }
        return fragmentPostListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val boardType = arguments?.getString("boardType")
    }

    inner class AllRecyclerViewAdapter : Adapter<AllRecyclerViewAdapter.AllViewHolder>() {
        inner class AllViewHolder(rowPostListBinding: RowPostListBinding) :
            ViewHolder(rowPostListBinding.root) {
            val rowPostListSubject = rowPostListBinding.rowPostListSubject
            val rowPostListNickName = rowPostListBinding.rowPostListNickName

            init {
                rowPostListBinding.root.setOnClickListener {
                    //글을 볼 수 있는 postreadfragment로 이동
                    findNavController().navigate(R.id.action_postListFragment_to_postReadFragment)
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
            return 100
        }

        override fun onBindViewHolder(holder: AllViewHolder, position: Int) {
            holder.rowPostListSubject.text = "제목입니다: $position"
            holder.rowPostListNickName.text = "작성자 : $position"
        }
    }

    inner class ResultRecyclerViewAdapter : Adapter<ResultRecyclerViewAdapter.AllViewHolder>() {
        inner class AllViewHolder(rowPostListBinding: RowPostListBinding) :
            ViewHolder(rowPostListBinding.root) {
            val rowPostListSubject = rowPostListBinding.rowPostListSubject
            val rowPostListNickName = rowPostListBinding.rowPostListNickName

            init {
                rowPostListBinding.root.setOnClickListener {
                    //글을 볼 수 있는 postreadfragment로 이동
                    findNavController().navigate(R.id.action_postListFragment_to_postReadFragment)
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
            return 100
        }

        override fun onBindViewHolder(holder: AllViewHolder, position: Int) {
            holder.rowPostListSubject.text = "제목입니다: $position"
            holder.rowPostListNickName.text = "작성자 : $position"
        }
    }
}