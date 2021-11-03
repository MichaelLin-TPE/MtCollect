package com.collect.collectpeak.fragment.member.page_fragment.goal_edit

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.collect.collectpeak.MtCollectorFragment
import com.collect.collectpeak.R
import com.collect.collectpeak.databinding.FragmentGoalEditBinding
import com.collect.collectpeak.dialog.ConfirmDialog
import com.collect.collectpeak.dialog.PeakSelectDialog
import com.collect.collectpeak.firebase.MountainData
import com.collect.collectpeak.fragment.mountain.peak_preview.SummitData
import com.collect.collectpeak.fragment.mountain.peak_time.PeakTimeFragment
import com.collect.collectpeak.fragment.mountain.peak_time.PeakTimeFragment.Companion.SELECT
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.*
import io.reactivex.disposables.CompositeDisposable



class GoalEditFragment : MtCollectorFragment(){

    private lateinit var targetSummitData: SummitData

    private lateinit var fragmentActivity: FragmentActivity

    private lateinit var dataBinding: FragmentGoalEditBinding

    private val adapter = GoalEditPhotoAdapter()

    private val disposable = CompositeDisposable()

    private val peakSelectDialog = PeakSelectDialog.newInstance()

    private val viewModel: GoalEditViewModel by activityViewModels {
        GoalEditViewModel.GoalEditFactory()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }

    override fun onPause() {
        super.onPause()
        viewModel.showPhotoSelectorViewLiveData.removeObservers(this)
        TempDataHandler.setUserSelectDate(0)
        disposable.clear()
    }

    companion object {

        @JvmStatic
        fun newInstance(summitData: SummitData) =
            GoalEditFragment().apply {
                arguments = Bundle().apply {
                    this.putParcelable("data", summitData)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            targetSummitData = it.getParcelable<SummitData>("data") as SummitData
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_goal_edit, container, false)

        dataBinding.vm = viewModel

        dataBinding.lifecycleOwner = this

        dataBinding.clickListener = ButtonClickHandler(viewModel)

        initView(dataBinding.root)

        // Inflate the layout for this fragment
        return dataBinding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun observerHandle() {
        viewModel.mtPhotoListLivData.observe(this, {

            adapter.setData(it)

            dataBinding.editRecyclerView.adapter = adapter

            adapter.setonPhotoRemoveClickListener(object :
                GoalEditPhotoAdapter.OnPhotoRemoveClickListener {
                override fun onRemove(photoUrl: String) {
                    MichaelLog.i("onRemove")
                    viewModel.onPhotoRemoveClickListener(photoUrl)
                }

                override fun onRemoveBitmap(position: Int) {
                    MichaelLog.i("onRemoveBitmap")
                    viewModel.onPhotoRemoveBitmapClickListener(position)
                }

            })

            adapter.setOnAddPhotoClickListener(object :
                GoalEditPhotoAdapter.OnAddPhotoClickListener {
                override fun onAdd() {
                    viewModel.onAddPhotoClickListener()
                }

            })

        })

        viewModel.updatePhotoListLiveData.observe(this, {
            adapter.setData(it)
            adapter.notifyDataSetChanged()
        })


        viewModel.showPhotoSelectorViewLiveData.observe(this, {


            MichaelLog.i("顯示照片篩選")

            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {

                if (it == 0) {
                    showRemovePhotoDialog()
                    return@observe
                }

                PhotoSelector.instance.showPhotoSelectorView(fragmentActivity, it) { result ->

                    viewModel.onCatchPhotoResultListener(result, fragmentActivity.contentResolver)

                }
            }
        })

        viewModel.setOnMtListButtonClickListener(object : OnButtonCallBackListener<String>{
            override fun onCatchResult(result: String) {
                peakSelectDialog.setPeak(result)
                peakSelectDialog.show(fragmentActivity.supportFragmentManager,"dialog")
                peakSelectDialog.setOnPeakConfirmButtonClickListener(object : PeakSelectDialog.OnPeakConfirmButtonClickListener{
                    override fun onClick(peak: String, level: String) {
                        viewModel.onPeakSelectClickListener(peak,level)
                    }
                })
            }

            override fun showLoadingDialog(result: String) {
                showProgressDialog(fragmentActivity.supportFragmentManager,result)
            }

            override fun dismissLoadingDialog() {
                dismissProgressDialog()
            }

            override fun showToast(result: String) {
                showToast(result)
            }

            override fun finishPage() {
                fragmentActivity.finish()
                Tool.startActivityOutAnim(fragmentActivity,2)
            }

            override fun goToSelectDatePage() {
                onGoToSelectDatePage()
            }
        })


    }


    override fun onResume() {
        super.onResume()
        viewModel.onFragmentStart(targetSummitData)

        observerHandle()

    }

    private fun showRemovePhotoDialog() {
        showConfirmDialog(
            fragmentActivity.supportFragmentManager,
            fragmentActivity.getString(R.string.title_remove_some_photo),
            object : ConfirmDialog.OnConfirmDialogClickListener {
                override fun onConfirm() {

                }

                override fun onCancel() {

                }

            })
    }


    private fun initView(view: View) {

        val linearLayoutManager = LinearLayoutManager(fragmentActivity)

        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        dataBinding.editRecyclerView.layoutManager = linearLayoutManager

        viewModel.setOnBackButtonClickEventCallBackListener{
            MichaelLog.i("關閉編輯頁面")
            fragmentActivity.finish()
            fragmentActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        dataBinding.editDesc.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onCatchDescription(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }




    private fun onGoToSelectDatePage() {
        FragmentUtil.replace(
            R.id.container,
            fragmentActivity.supportFragmentManager,
            PeakTimeFragment.newInstance(MountainData(), SELECT),
            true,
            PeakTimeFragment.javaClass.simpleName,
            1
        )
    }



}