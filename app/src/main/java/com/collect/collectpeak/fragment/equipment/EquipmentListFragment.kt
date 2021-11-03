package com.collect.collectpeak.fragment.equipment

import android.content.Context
import android.content.Intent
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
import com.collect.collectpeak.activity.EquipmentActivity
import com.collect.collectpeak.activity.EquipmentActivity.Companion.EDIT
import com.collect.collectpeak.activity.EquipmentActivity.Companion.SELECT
import com.collect.collectpeak.databinding.FragmentEquipmentListBinding
import com.collect.collectpeak.dialog.ConfirmDialog
import com.collect.collectpeak.firebase.AuthHandler
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentUserData
import com.collect.collectpeak.tool.ButtonClickHandler
import com.collect.collectpeak.tool.Tool


class EquipmentListFragment : MtCollectorFragment() {

    private lateinit var dataBinding: FragmentEquipmentListBinding

    private lateinit var fragmentActivity: FragmentActivity

    private val adapter = EquipmentAdapter()

    private val viewModel: EquipmentListViewModel by activityViewModels {
        val repository = EquipmentListRepositoryImpl()
        EquipmentListViewModel.EquipmentListViewModelFactory(repository)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }


    companion object {
        const val NORMAL = 0
        const val DELETE = 1

        @JvmStatic
        fun newInstance() = EquipmentListFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_equipment_list, container, false)
        dataBinding.vm = viewModel
        dataBinding.clickListener = ButtonClickHandler(viewModel)
        dataBinding.lifecycleOwner = this
        initView(dataBinding.root)

        return dataBinding.root
    }

    private fun initView(root: View) {
        viewModel.setOnEquipmentClickResultCallBackListener(onEquipmentClickResultCallBackListener)

        dataBinding.equipmentRecyclerView.layoutManager = LinearLayoutManager(fragmentActivity)
    }


    private val onEquipmentClickResultCallBackListener =
        object : OnEquipmentClickResultCallBackListener {
            override fun onIntentToEquipmentSelectPage() {
                intentToEquipmentSelectPage()
            }

            override fun onShowDeleteConfirmDialog() {
                showConfirmDialog(fragmentActivity.supportFragmentManager,
                    fragmentActivity.getString(R.string.confirm_delete),
                    object : ConfirmDialog.OnConfirmDialogClickListener {
                        override fun onConfirm() {
                            viewModel.onConfirmDeleteClickListener()
                        }

                        override fun onCancel() {

                        }

                    })
            }

        }


    private fun intentToEquipmentSelectPage() {

        if (!AuthHandler.isLogin()) {

            showToast(fragmentActivity.getString(R.string.title_please_login_first))

            return
        }


        val intent = Intent(fragmentActivity, EquipmentActivity::class.java)
        intent.putExtra("type", SELECT)
        fragmentActivity.startActivity(intent)
        Tool.startActivityInAnim(fragmentActivity, 2)
    }

    override fun onPause() {
        super.onPause()

        viewModel.showDeleteListView.value = false
        viewModel.updateDeleteView.value = ArrayList()
    }


    override fun onStart() {
        super.onStart()
        viewModel.onFragmentStart()
        handleObserver()
    }

    private fun handleObserver() {
        viewModel.defaultViewLiveData.observe(this, {
            dataBinding.equipmentDefaultView.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.equipmentViewLiveData.observe(this, {
            dataBinding.equipmentRecyclerView.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.updateEquipmentListLiveData.observe(this, { data ->

            adapter.setType(NORMAL)
            adapter.setDataArray(data)

            dataBinding.equipmentRecyclerView.adapter = adapter

            adapter.setOnEquipmentItemClickListener(object :
                EquipmentAdapter.OnEquipmentItemClickListener {
                override fun onClick(data: EquipmentUserData) {
                    goToEditPage(data)
                }

                override fun onLongPress() {
                    viewModel.onEquipmentLongPressListener()
                }

            })

        })
        viewModel.showDeleteListView.observe(this, {
            if (!it) {
                return@observe
            }
            adapter.setType(DELETE)
            adapter.notifyDataSetChanged()
            adapter.setOnEquipmentDeleteClickListener(object :
                EquipmentAdapter.OnEquipmentDeleteClickListener {
                override fun onDelete(data: EquipmentUserData) {
                    viewModel.onDeleteEquipmentClickListener(data)
                }

            })
        })

        viewModel.updateDeleteView.observe(this, {

            if (it.isEmpty()) {
                return@observe
            }
            adapter.setType(DELETE)
            adapter.setDataArray(it)
            adapter.notifyDataSetChanged()
        })

    }

    private fun goToEditPage(data: EquipmentUserData) {
        val intent = Intent(fragmentActivity, EquipmentActivity::class.java)
        intent.putExtra("type", EDIT)
        intent.putExtra("data", data)
        fragmentActivity.startActivity(intent)
        Tool.startActivityInAnim(fragmentActivity, 2)
    }


}