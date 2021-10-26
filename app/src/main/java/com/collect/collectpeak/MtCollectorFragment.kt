package com.collect.collectpeak

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.collect.collectpeak.dialog.ConfirmDialog
import com.collect.collectpeak.dialog.EditContentDialog
import com.collect.collectpeak.dialog.LoadingDialog
import com.collect.collectpeak.log.MichaelLog


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

        if (dialog.dialog != null && dialog.dialog != null && dialog.dialog!!.isShowing && dialog.isRemoving){
            return
        }
        MichaelLog.i("顯示ConfirmDialog")
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

    private fun showDialog(
        dialog: ConfirmDialog,
        fragmentManager: FragmentManager,
        onConfirmDialogClickListener: ConfirmDialog.OnConfirmDialogClickListener
    ) {

    }

    fun showEditContentDialog(fragmentManager: FragmentManager,content: String,onEditContentDialogClickListener: EditContentDialog.OnEditContentDialogClickListener){
        val dialog = EditContentDialog.newInstance(content)
        dialog.show(fragmentManager,"dialog")
        dialog.setOnEditContentDialogClickListener(object : EditContentDialog.OnEditContentDialogClickListener{
            override fun onConfirm(content: String) {
                onEditContentDialogClickListener.onConfirm(content)
            }

            override fun onCancel() {
               onEditContentDialogClickListener.onCancel()
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