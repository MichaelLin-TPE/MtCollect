package com.collect.collectpeak.fragment.member

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.database.DatabaseUtils
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.collect.collectpeak.MtCollectorFragment
import com.collect.collectpeak.R
import com.collect.collectpeak.activity.MemberProfileActivity
import com.collect.collectpeak.activity.SettingActivity
import com.collect.collectpeak.databinding.FragmentMemberBinding
import com.collect.collectpeak.dialog.LoadingDialog
import com.collect.collectpeak.tool.ButtonClickHandler
import com.collect.collectpeak.tool.GoogleLoginTool
import com.collect.collectpeak.tool.GoogleLoginTool.Companion.RC_SIGN_IN
import com.collect.collectpeak.tool.Tool
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.SignInButton
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.ByteArrayOutputStream
import java.lang.Exception


class MemberFragment : MtCollectorFragment() {


    private lateinit var dataBinding: FragmentMemberBinding

    private lateinit var loadingDialog: LoadingDialog

    private lateinit var adapter: MemberAdapter

    private val viewModel: MemberViewModel by activityViewModels {
        val repository = MemberRepositoryImpl()
        MemberViewModel.MemberViewModelFactory(repository)

    }

    private lateinit var fragmentActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }


    companion object {
        @JvmStatic
        fun newInstance() = MemberFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_member, container, false)
        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this
        dataBinding.clickListener = ButtonClickHandler(viewModel)
        initView(dataBinding.root)

        // Inflate the layout for this fragment
        return dataBinding.root
    }

    private fun initView(root: View) {
        GoogleLoginTool.setActivity(fragmentActivity)
        Tool.setGoogleButtonText(
            dataBinding.loginGoogle,
            fragmentActivity.getString(R.string.google_login)
        )

        dataBinding.memberRecyclerView.layoutManager = LinearLayoutManager(fragmentActivity)

        viewModel.setMemberClickEventCallBackListener(object : MemberClickEventCallBackListener{
            override fun onSettingClickListener() {
                intentToSettingPage()
            }
        })

        dataBinding.loginGoogle.setOnClickListener {
            viewModel.onGoogleLoginClickListener()
        }

    }


    private fun intentToSettingPage() {
        val intent = Intent(fragmentActivity,SettingActivity::class.java)
        fragmentActivity.startActivity(intent)
        Tool.startActivityInAnim(fragmentActivity,2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN && data != null) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            GoogleLoginTool.onCatchLoginResult(task, onGoogleLoginResultListener)
            return
        }

        //這邊皆選擇完的畫面
        val result = CropImage.getActivityResult(data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && result != null && resultCode == RESULT_OK) {

            try {
                handlePhoto(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }

    }

    private fun handlePhoto(result: CropImage.ActivityResult) {

        val resultUri = result.uri

        val bitmap = MediaStore.Images.Media.getBitmap(fragmentActivity.contentResolver, resultUri)

        val baos = ByteArrayOutputStream()

        val quality = 80

        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)

        viewModel.onShowLoadingDialog(fragmentActivity.getString(R.string.uploading))

        val bytes = baos.toByteArray()

        viewModel.onUploadPhoto(bytes)


    }

    private val onGoogleLoginResultListener = object : GoogleLoginTool.OnGoogleLoginResultListener {
        override fun onSuccess() {
            viewModel.onLoginSuccess()
        }

        override fun onNeedAddNewProfile() {
            viewModel.onLoginSuccessAndNeedEditProfile()

            intentToMemberProfilePage()
        }

        override fun onFail() {
            viewModel.onLoginFail()
        }

    }

    override fun onResume() {
        super.onResume()

        viewModel.onFragmentStart()

        onObserversHandle()
    }

    override fun onPause() {
        super.onPause()
        viewModel.memberInfoViewLiveData.value = false
        viewModel.memberInfoViewLiveData.removeObservers(this)
        viewModel.photoSelectLiveData.value = false
        viewModel.photoSelectLiveData.removeObservers(this)
        viewModel.loadingDialogLiveData.value = ""
        viewModel.loadingDialogLiveData.removeObservers(this)
        viewModel.dismissLoadingDialog.value = false
        viewModel.dismissLoadingDialog.removeObservers(this)
        viewModel.updateMemberViewLiveData.value = false
        viewModel.updateMemberViewLiveData.removeObservers(this)

    }

    private fun onObserversHandle() {
        viewModel.defaultLoginViewLiveData.observe(this, {
            dataBinding.memberDefaultView.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.memberInfoViewLiveData.observe(this, {

            if (!it) {
                dataBinding.memberRecyclerView.visibility = View.GONE
                return@observe
            }

            setUpMemberView()
        })


        viewModel.photoSelectLiveData.observe(this, {
            if (!it) {
                return@observe
            }

            showPhotoSelectView()

        })

        viewModel.loadingDialogLiveData.observe(this, {
            if (it.isEmpty()) {
                return@observe
            }
            loadingDialog = LoadingDialog.newInstance(it)
            loadingDialog.show(fragmentActivity.supportFragmentManager, "loadingDialog")
        })

        viewModel.dismissLoadingDialog.observe(this, {
            if (!it) {
                return@observe
            }
            loadingDialog.dismiss()
        })

        viewModel.updateMemberViewLiveData.observe(this, {
            if (!it || adapter == null) {
                return@observe
            }
            adapter.notifyDataSetChanged()

        })

    }

    private fun showPhotoSelectView() {
        //啟動選擇圖片
        context?.let {
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                .start(it, this)
        }

    }

    private fun setUpMemberView() {
        dataBinding.memberRecyclerView.visibility = View.VISIBLE

        adapter = MemberAdapter()

        adapter.setFragmentManager(childFragmentManager)

        dataBinding.memberRecyclerView.adapter = adapter

        adapter.setOnMemberInfoClickListener(object : MemberAdapter.OnMemberInfoClickListener {

            override fun onPhotoSelectListener() {
                viewModel.onPhotoSelectListener()
            }

            override fun onEditUserProfileClickListener() {
                intentToMemberProfilePage()
            }

        })
    }

    private fun intentToMemberProfilePage() {
        val it = Intent(fragmentActivity, MemberProfileActivity::class.java)
        fragmentActivity.startActivity(it)
        Tool.startActivityInAnim(fragmentActivity, 2)
    }


}