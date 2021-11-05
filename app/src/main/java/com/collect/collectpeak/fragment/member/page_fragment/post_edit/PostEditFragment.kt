package com.collect.collectpeak.fragment.member.page_fragment.post_edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.collect.collectpeak.R
import com.collect.collectpeak.fragment.share.ShareData


class PostEditFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_edit, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(data: ShareData) =
            PostEditFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}