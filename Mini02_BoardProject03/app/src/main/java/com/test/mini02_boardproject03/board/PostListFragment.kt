package com.test.mini02_boardproject03.board

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.test.mini02_boardproject03.R
import com.test.mini02_boardproject03.databinding.FragmentPostListBinding
import com.test.mini02_boardproject03.databinding.RowPostListBinding

class PostListFragment : Fragment() {
    lateinit var fragmentPostListBinding: FragmentPostListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentPostListBinding = FragmentPostListBinding.inflate(layoutInflater)

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
//                val arg = Bundle()
//                arg.putBoolean("isModify", false)
                findNavController().navigate(R.id.action_postListFragment_to_postWriteFragment)
                true
            }
        }
        // Inflate the layout for this fragment
        return fragmentPostListBinding.root
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.AllViewHolder>() {
        inner class AllViewHolder(rowPostListBinding: RowPostListBinding) :
            RecyclerView.ViewHolder(rowPostListBinding.root) {
            val rowPostListSubject = rowPostListBinding.rowPostListSubject
            val rowPostListNickName = rowPostListBinding.rowPostListNickName

            init {
                rowPostListBinding.root.setOnClickListener {
                    findNavController().navigate(
                        R.id.action_postListFragment_to_postReadFragment
                    )
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllViewHolder {
            val rowPostListBinding = RowPostListBinding.inflate(layoutInflater)
            rowPostListBinding.root.layoutParams = ViewGroup.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
            return AllViewHolder(rowPostListBinding)
        }

        override fun getItemCount(): Int {
            return 100
        }

        override fun onBindViewHolder(holder: AllViewHolder, position: Int) {
            holder.rowPostListSubject.text = "데이터$position"
            holder.rowPostListNickName.text = "사용자$position"
        }
    }

}