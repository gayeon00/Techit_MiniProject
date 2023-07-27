package com.test.mini02_boardproject01.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.test.mini02_boardproject01.databinding.FragmentPostListBinding
import com.test.mini02_boardproject01.databinding.RowPostListBinding

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

        fragmentPostListBinding.run {
            recyclerViewAll.run {
                adapter = AllRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(context)
            }
        }
        return fragmentPostListBinding.root
    }

    inner class AllRecyclerViewAdapter : Adapter<AllRecyclerViewAdapter.AllViewHolder>() {
        inner class AllViewHolder(rowPostListBinding: RowPostListBinding) :
            ViewHolder(rowPostListBinding.root) {
            val rowPostListSubject = rowPostListBinding.rowPostListSubject
            val rowPostListNickName = rowPostListBinding.rowPostListNickName

            init {

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