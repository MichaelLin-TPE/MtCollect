package com.collect.collectpeak

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.collect.collectpeak.dialog.ConfirmDialog
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

    fun showConfirmDialog(fragmentManager: FragmentManager,content: String,onConfirmDialogClickListener: ConfirmDialog.OnConfirmDialogClickListener){

        val dialog = ConfirmDialog.newInstance(content)

        dialog.show(fragmentManager,"dialog")

        dialog.setOnConfirmDialogClickListener(object : ConfirmDialog.OnConfirmDialogClickListener{
            override fun onConfirm() {
                onConfirmDialogClickListener.onConfirm()
            }

            override fun onCancel() {
                onConfirmDialogClickListener.onCancel()
            }

        })

    }

    fun dismissProgressDialog(){
        progressDialog.dismiss()
    }

    fun showToast(content: String){

        Toast.makeText(MtCollectorApplication.getInstance().getContext(),content,Toast.LENGTH_LONG).show()

    }

}