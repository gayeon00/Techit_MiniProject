package com.test.mini02_boardproject03.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.test.mini02_boardproject03.R

/**
 * A simple [Fragment] subclass.
 * Use the [ModifyUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ModifyUserFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modify_user, container, false)
    }

}