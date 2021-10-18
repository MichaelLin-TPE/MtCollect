package com.collect.collectpeak.fragment.equipment.equipment_select

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.collect.collectpeak.MtCollectorFragment
import com.collect.collectpeak.R
import com.collect.collectpeak.activity.EquipmentActivity.Companion.SELECT
import com.collect.collectpeak.databinding.FragmentEquipmentBinding
import com.collect.collectpeak.fragment.equipment.equipment_edit.EquipmentEditFragment
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentNewAdapter.Companion.TITLE
import com.collect.collectpeak.tool.FragmentUtil
import com.collect.collectpeak.tool.HeaderItemDecoration
import com.collect.collectpeak.tool.TimeTool
import com.collect.collectpeak.tool.Tool

class EquipmentFragment : MtCollectorFragment() {

    private lateinit var fragmentActivity: FragmentActivity

    private lateinit var adapter: EquipmentNewAdapter

    private var type = 0

    private val viewModel: EquipmentViewModel by activityViewModels {
        val repository = EquipmentRepositoryImpl()
        EquipmentViewModel.EquipmentViewModelFactory(repository)
    }

    private lateinit var dataBinding: FragmentEquipmentBinding


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }

    companion object {
        @JvmStatic
        fun newInstance(type: Int) =
            EquipmentFragment().apply {
                arguments = Bundle().apply {
                    this.putInt("type",type)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getInt("type",SELECT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_equipment, container, false)
        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this

        initView(dataBinding.root)

        return dataBinding.root
    }

    private fun initView(view: View) {
        dataBinding.equipmentRecyclerView.layoutManager = LinearLayoutManager(fragmentActivity)
        adapter = EquipmentNewAdapter()
        dataBinding.equipmentRecyclerView.adapter = adapter
        adapter.setOnEquipmentCheckClickListener(object :
            EquipmentItemViewModel.OnEquipmentCheckClickListener {
            override fun onCheck(data: EquipmentData) {
                viewModel.onEquipmentItemCheckListener(data)
            }
        })


        dataBinding.btnBack.setOnClickListener {
            finishPage()
        }

        dataBinding.equipmentEditName.hint = TimeTool.getCurrentDate() + " ${fragmentActivity.getString(R.string.equipment_select_list)}"

        dataBinding.equipmentEditName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onEditNameChangeListener(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        dataBinding.mtActionBarDone.setOnClickListener {
            viewModel.onButtonDoneClickListener()
        }


    }

    override fun onPause() {
        super.onPause()

        viewModel.equipmentListLiveData.value = EquipmentListData()

        viewModel.equipmentListLiveData.removeObservers(this)

        viewModel.progressBarLiveData.removeObservers(this)
    }


    override fun onStart() {
        super.onStart()
        viewModel.onFragmentStart(type)

        observerHandler()
    }

    private fun observerHandler() {
        viewModel.equipmentListLiveData.observe(this, { data ->
            adapter.setData(data)
            dataBinding.equipmentRecyclerView.addItemDecoration(
                HeaderItemDecoration(
                    dataBinding.equipmentRecyclerView,
                    true
                ) {
                    adapter.getItemViewType(it) == TITLE
                })

        })

        viewModel.finishButtonEnable.observe(this,{
            dataBinding.mtActionBarDone.isEnabled = it
        })

        viewModel.loadingDialogLiveData.observe(this,{
            if (!it){
                return@observe
            }
            showProgressDialog(fragmentActivity.supportFragmentManager,"新增中")
        })

        viewModel.dismissLoadingDialogLiveData.observe(this,{
            if (!it){
                return@observe
            }
            dismissProgressDialog()
        })

        viewModel.toastLiveData.observe(this,{
            showToast(it)
        })

        viewModel.finishPageLiveData.observe(this,{
            if (!it){
                return@observe
            }
            finishPage()

        })

    }

    private fun finishPage(){

        if(type == SELECT){
            fragmentActivity.finish()
            Tool.startActivityOutAnim(fragmentActivity,2)
            return
        }

        FragmentUtil.popBackStackTo(fragmentActivity.supportFragmentManager,EquipmentEditFragment.javaClass.simpleName)



    }

}