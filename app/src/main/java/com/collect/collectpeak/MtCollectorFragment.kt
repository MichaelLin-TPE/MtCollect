package com.collect.collectpeak

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.collect.collectpeak.dialog.LoadingDialog


/**
 * 有共同的Function 會寫在這邊
 */
open class MtCollectorFragment : Fragment() {

    private var progressDialog = LoadingDialog.newInstance("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun showProgressDialog(fragmentManager: FragmentManager, content: String){
        progressDialog = LoadingDialog.newInstance(content)
        progressDialog.show(fragmentManager, "progressDialog")
    }

    fun dismissProgressDialog(){
        progressDialog.dismiss()
    }

}