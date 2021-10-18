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
import com.collect.collectpeak.activity.EquipmentActivity.Companion.EDIT_LIST
import com.collect.collectpeak.activity.EquipmentActivity.Companion.SELECT
import com.collect.collectpeak.databinding.FragmentEquipmentEditBinding
import com.collect.collectpeak.dialog.EditContentDialog
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentFragment
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentUserData
import com.collect.collectpeak.tool.FragmentUtil
import com.collect.collectpeak.tool.FragmentUtil.Companion.ANIM_LEFT_RIGHT
import com.collect.collectpeak.tool.Tool


class EquipmentEditFragment : MtCollectorFragment() {

    private lateinit var dataBinding: FragmentEquipmentEditBinding

    private lateinit var fragmentActivity: FragmentActivity

    private lateinit var userEquipmentData: EquipmentUserData

    private val viewModel: EquipmentEditViewModel by activityViewModels {
        val repository = EquipmentEditRepositoryImpl()
        EquipmentEditViewModel.EquipmentEditFactory(repository)
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
                    this.putParcelable("data", data)
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
        viewModel.equipmentListLiveData.observe(this, { dataArray ->

            val adapter = UserEquipmentListAdapter()

            adapter.setDataArray(dataArray)

            dataBinding.editRecylerView.adapter = adapter


        })

        viewModel.showEditNameDialogLiveData.observe(this, {

            if (!it) {
                return@observe
            }
            showEditContentDialog(
                fragmentActivity.supportFragmentManager,
                "輸入裝備清單名稱",
                object : EditContentDialog.OnEditContentDialogClickListener {
                    override fun onConfirm(content: String) {
                        viewModel.onCatchEquipmentListName(content)
                    }

                    override fun onCancel() {

                    }
                })


        })

        viewModel.showToastLiveData.observe(this,{
            if (it.isEmpty()){
                return@observe
            }
            showToast(it)
        })


        viewModel.goToEditEquipmentListPageLiveData.observe(this, {
            if (!it) {
                return@observe
            }
            val fragment = EquipmentFragment.newInstance(EDIT_LIST)
            FragmentUtil.replace(R.id.container,fragmentActivity.supportFragmentManager,fragment,true,fragment.javaClass.simpleName,ANIM_LEFT_RIGHT)

        })

        viewModel.finishPageLiveData.observe(this,{

            if (!it){
                return@observe
            }
            finishPage()

        })

        viewModel.showProgressLiveData.observe(this,{
            if (!it){
                return@observe
            }
            showProgressDialog(fragmentActivity.supportFragmentManager,fragmentActivity.getString(R.string.title_editing))
        })

        viewModel.dismissProgressDialog.observe(this,{
            if (!it){
                return@observe
            }
            dismissProgressDialog()
        })

    }

    override fun onPause() {
        super.onPause()

        viewModel.showEditNameDialogLiveData.value = false

        viewModel.goToEditEquipmentListPageLiveData.value = false

        viewModel.showToastLiveData.value = ""

        viewModel.finishPageLiveData.value = false

        viewModel.showProgressLiveData.value = false

        viewModel.dismissProgressDialog.value = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_equipment_edit, container, false)

        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this
        initView(dataBinding.root)

        return dataBinding.root
    }

    private fun finishPage(){
        fragmentActivity.finish()
        Tool.startActivityOutAnim(fragmentActivity, 2)
    }

    private fun initView(root: View) {
        dataBinding.btnBack.setOnClickListener {
            finishPage()
        }

        dataBinding.editRecylerView.layoutManager = LinearLayoutManager(fragmentActivity)


        //修改名字
        dataBinding.editButton.setOnClickListener {
            viewModel.editNameButtonClickListener()
        }
        //修改裝備清單
        dataBinding.editListButton.setOnClickListener {
            viewModel.editEquipmentListClickListener()
        }

        dataBinding.mtActionBarDone.setOnClickListener {
            viewModel.onButtonDoneClickListener()
        }


    }


}