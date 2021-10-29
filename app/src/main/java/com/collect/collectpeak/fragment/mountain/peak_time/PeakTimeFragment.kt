package com.collect.collectpeak.fragment.mountain.peak_time

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.collect.collectpeak.MtCollectorFragment
import com.collect.collectpeak.R
import com.collect.collectpeak.databinding.FragmentPeakTimeBinding
import com.collect.collectpeak.dialog.YearSelectDialog
import com.collect.collectpeak.firebase.MountainData
import com.collect.collectpeak.fragment.mountain.peak_photo.PhotoFragment
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.FragmentUtil
import com.collect.collectpeak.tool.FragmentUtil.Companion.ANIM_LEFT_RIGHT
import com.collect.collectpeak.tool.Tool


class PeakTimeFragment : MtCollectorFragment() {

    private lateinit var fragmentActivity: FragmentActivity

    private lateinit var dataBinding : FragmentPeakTimeBinding

    private var mountainData = MountainData()

    private var type = 0

    private val adapter = CalendarAdapter()

    private val dialog = YearSelectDialog.newInstance()

    private val viewModel : PeakTimeViewModel by activityViewModels {
        PeakTimeViewModel.PeakTimeViewModelFactory()
    }

    companion object {

        const val NORMAL = 0

        const val SELECT = 1

        @JvmStatic
        fun newInstance(data: MountainData,type : Int) =
            PeakTimeFragment().apply {
                arguments = Bundle().apply {
                    this.putSerializable("data",data)
                    this.putInt("type", type)
                }
            }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }

    override fun onStart() {
        super.onStart()
        viewModel.onFragmentStart(mountainData,type)

        observerHandle()
    }

    private fun observerHandle() {

        viewModel.finishLiveData.observe(this,{


            if (!it){

                return@observe
            }

            fragmentActivity.supportFragmentManager.popBackStack()
        })

        viewModel.btnNextValueLiveData.observe(this,{
            dataBinding.peakTimeButton.text = it
        })

        viewModel.calendarLiveData.observe(this,{

            adapter.setDataArray(it)

            dataBinding.peakRecyclerView.adapter = adapter

            adapter.setOnCalendarDateClickListener(object : CalendarListAdapter.OnCalendarDateClickListener{
                override fun onClick(data: CalendarData) {
                    MichaelLog.i("點擊年份：${data.year} 點擊月份：${data.month} 點擊日期：${data.date}")

                    viewModel.onCalendarDateClickListener(data)

                }

            })

        })

        viewModel.showYeaListDialogLiveData.observe(this,{
            if (it.isEmpty()){
                return@observe
            }
            dialog.setYear(it)

            dialog.show(fragmentActivity.supportFragmentManager,"dialog")

            dialog.setOnYearConfirmButtonClickListener(object : YearSelectDialog.OnYearConfirmButtonClickListener{
                override fun onClick(year: String) {
                    viewModel.onYearConfirmClickListener(year)
                }
            })
        })

        viewModel.updateCalendarLiveData.observe(this,{

            if (it.isEmpty()){
                return@observe
            }

            adapter.setDataArray(it)
            adapter.notifyDataSetChanged()
        })


        viewModel.goToSelectPhotoPageLiveData.observe(this,{
            if (it.mtName.isEmpty()){
                return@observe
            }
            MichaelLog.i("前進選擇照片頁面")
            val fragment = PhotoFragment.newInstance(it) as MtCollectorFragment

            FragmentUtil.replace(R.id.container,fragmentActivity.supportFragmentManager,fragment,true,PhotoFragment.javaClass.simpleName,
                ANIM_LEFT_RIGHT)
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.showYeaListDialogLiveData.value = ""
        viewModel.yearLiveData.value = "2021"
        viewModel.goToSelectPhotoPageLiveData.value = MtPeakData()
        viewModel.goToSelectPhotoPageLiveData.removeObservers(this)
        viewModel.showYeaListDialogLiveData.removeObservers(this)
        viewModel.onPause()
        viewModel.enableNextButtonLiveData.removeObservers(this)
        viewModel.finishLiveData.value = false
        viewModel.finishLiveData.removeObservers(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mountainData = it.getSerializable("data") as MountainData
            type = it.getInt("type", NORMAL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_peak_time,container,false)

        dataBinding.vm = viewModel

        dataBinding.lifecycleOwner = this

        initView(dataBinding.root)

        // Inflate the layout for this fragment
        return dataBinding.root
    }

    private fun initView(root: View) {

        dataBinding.btnBack.setOnClickListener {

            if(type == SELECT){
                fragmentActivity.supportFragmentManager.popBackStack()

                return@setOnClickListener
            }

            fragmentActivity.finish()
            Tool.startActivityOutAnim(fragmentActivity,2)

        }

        dataBinding.peakRecyclerView.layoutManager = LinearLayoutManager(fragmentActivity)

        dataBinding.peakSpinnerView.setOnClickListener {

            viewModel.onPeakYearSpinnerClickListener()

        }

        dataBinding.peakTimeButton.setOnClickListener {
            viewModel.onNextButtonClickListener()
        }

    }


}