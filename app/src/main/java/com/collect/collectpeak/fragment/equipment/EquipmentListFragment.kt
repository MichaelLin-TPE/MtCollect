package com.collect.collectpeak.fragment.equipment

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.collect.collectpeak.MtCollectorFragment
import com.collect.collectpeak.R
import com.collect.collectpeak.activity.EquipmentActivity
import com.collect.collectpeak.activity.EquipmentActivity.Companion.EDIT
import com.collect.collectpeak.activity.EquipmentActivity.Companion.SELECT
import com.collect.collectpeak.databinding.FragmentEquipmentListBinding
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentUserData
import com.collect.collectpeak.tool.Tool
import kotlin.concurrent.fixedRateTimer


class EquipmentListFragment : MtCollectorFragment() {

    private lateinit var dataBinding: FragmentEquipmentListBinding

    private lateinit var fragmentActivity: FragmentActivity

    private val viewModel : EquipmentListViewModel by activityViewModels {
        val repository = EquipmentListRepositoryImpl()
        EquipmentListViewModel.EquipmentListViewModelFactory(repository)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }


    companion object {
        @JvmStatic
        fun newInstance() = EquipmentListFragment().apply {
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

        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_equipment_list,container,false)
        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this
        initView(dataBinding.root)

        return dataBinding.root
    }

    private fun initView(root: View) {

        dataBinding.equipmentCreate.setOnClickListener {
            intentToEquipmentSelectPage()
        }

        dataBinding.equipmentAddIcon.setOnClickListener {
            intentToEquipmentSelectPage()
        }

        dataBinding.equipmentRecyclerView.layoutManager = LinearLayoutManager(fragmentActivity)


    }

    private fun intentToEquipmentSelectPage(){
        val intent = Intent(fragmentActivity,EquipmentActivity::class.java)
        intent.putExtra("type",SELECT)
        fragmentActivity.startActivity(intent)
        Tool.startActivityInAnim(fragmentActivity,2)
    }

    override fun onPause() {
        super.onPause()
        viewModel.goToEditPageLiveData.value = EquipmentUserData()
        viewModel.goToEditPageLiveData.removeObservers(this)
    }


    override fun onResume() {
        super.onResume()
        viewModel.onFragmentStart()
        handleObserver()
    }

    private fun handleObserver() {
        viewModel.defaultViewLiveData.observe(this,{
            dataBinding.equipmentDefaultView.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.equipmentViewLiveData.observe(this,{
            dataBinding.equipmentRecyclerView.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.updateEquipmentListLiveData.observe(this,{ data->

            val adapter = EquipmentAdapter()

            adapter.setDataArray(data)

            dataBinding.equipmentRecyclerView.adapter = adapter

            adapter.setOnEquipmentItemClickListener(object : EquipmentAdapter.OnEquipmentItemClickListener{
                override fun onClick(data: EquipmentUserData) {
                    viewModel.onEquipmentItemClickListener(data)
                }

            })

        })

        viewModel.goToEditPageLiveData.observe(this,{

            if (it.name.isEmpty()){
                return@observe
            }

            val intent = Intent(fragmentActivity,EquipmentActivity::class.java)
            intent.putExtra("type",EDIT)
            intent.putExtra("data",it)
            fragmentActivity.startActivity(intent)
            Tool.startActivityInAnim(fragmentActivity,2)
        })


    }


}