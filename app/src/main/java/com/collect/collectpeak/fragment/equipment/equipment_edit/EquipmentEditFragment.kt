package com.collect.collectpeak.fragment.equipment.equipment_edit

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
import com.collect.collectpeak.databinding.FragmentEquipmentEditBinding
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentUserData
import com.collect.collectpeak.tool.Tool


class EquipmentEditFragment : MtCollectorFragment() {

    private lateinit var dataBinding : FragmentEquipmentEditBinding

    private lateinit var fragmentActivity: FragmentActivity

    private lateinit var userEquipmentData : EquipmentUserData

    private val viewModel : EquipmentEditViewModel by activityViewModels {
        EquipmentEditViewModel.EquipmentEditFactory()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }



    companion object {
        @JvmStatic
        fun newInstance(data: EquipmentUserData) =
            EquipmentEditFragment().apply {
                arguments = Bundle().apply {
                    this.putParcelable("data",data)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userEquipmentData = it.getParcelable<EquipmentUserData>("data")!!
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onFragmentResume(userEquipmentData)

        observerHandle()
    }

    private fun observerHandle() {
        viewModel.equipmentListLiveData.observe(this,{ dataArray ->

            val adapter = UserEquipmentListAdapter()

            adapter.setDataArray(dataArray)

            dataBinding.editRecylerView.adapter = adapter


        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_equipment_edit,container,false)

        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this
        initView(dataBinding.root)

        return dataBinding.root
    }

    private fun initView(root: View) {
        dataBinding.btnBack.setOnClickListener {
            fragmentActivity.finish()
            Tool.startActivityOutAnim(fragmentActivity,2)
        }

        dataBinding.editRecylerView.layoutManager = LinearLayoutManager(fragmentActivity)

    }


}