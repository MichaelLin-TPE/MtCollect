package com.collect.collectpeak.fragment.equipment

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.AuthHandler
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentUserData
import com.collect.collectpeak.log.MichaelLog

class EquipmentListViewModel(private val equipmentListRepository: EquipmentListRepository) : ViewModel() {


    val defaultViewLiveData = MutableLiveData<Boolean>()

    val equipmentViewLiveData = MutableLiveData<Boolean>()

    val updateEquipmentListLiveData = MutableLiveData<ArrayList<EquipmentUserData>>()

    val showDeleteIconLiveData = MutableLiveData<Int>()

    val showDeleteListView = MutableLiveData<Boolean>()

    val updateDeleteView = MutableLiveData<ArrayList<EquipmentUserData>>()

    val showDeleteConfirmDialog = MutableLiveData<Boolean>()

    val deleteButtonEnable = MutableLiveData<Boolean>()

    private val userSelectDeleteData = ArrayList<EquipmentUserData>()

    private var totalUserEquipmentData = ArrayList<EquipmentUserData>()

    fun onFragmentStart() {
        deleteButtonEnable.value = false
        defaultViewLiveData.value = true
        equipmentViewLiveData.value=  false
        showDeleteIconLiveData.value = View.GONE
        showDeleteListView.value = false
        updateDeleteView.value = ArrayList()

        if (!AuthHandler.isLogin()){

            MichaelLog.i("尚未登入 不做拿取資料的動作")



            return
        }



        equipmentListRepository.getCurrentUserEquipment(object : FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<EquipmentUserData>>{
            override fun onCatchDataSuccess(data: ArrayList<EquipmentUserData>) {
                totalUserEquipmentData = data
                defaultViewLiveData.value = false
                equipmentViewLiveData.value =  true
                updateEquipmentListLiveData.value = data
            }

            override fun onCatchDataFail() {
                defaultViewLiveData.value = true
                equipmentViewLiveData.value = false
            }
        })
    }

    fun onEquipmentLongPressListener() {
        showDeleteIconLiveData.value = View.VISIBLE
        showDeleteListView.value = true
    }

    fun onDeleteEquipmentClickListener(data: EquipmentUserData) {
        MichaelLog.i("刪除選取了 : ${data.name}")

        collectUserSelectDeleteData(data)

        updateEquipmentList(data)

    }

    private fun updateEquipmentList(data: EquipmentUserData) {
        totalUserEquipmentData.forEach {
            if (data.equipmentID == it.equipmentID){
                it.isDeleteCheck = data.isDeleteCheck
            }
        }

        updateDeleteView.value = totalUserEquipmentData
    }

    private fun collectUserSelectDeleteData(data: EquipmentUserData) {
        if (userSelectDeleteData.isEmpty()){
            userSelectDeleteData.add(data)
            checkDeleteButtonEnable()
            MichaelLog.i("移除資料收集到的數量：${userSelectDeleteData.size}")
            return
        }
        var isFoundSameData = false
        userSelectDeleteData.forEach {
            if (data.equipmentID == it.equipmentID){
                isFoundSameData = true
                return@forEach
            }
        }
        if (isFoundSameData){
            userSelectDeleteData.remove(data)
            checkDeleteButtonEnable()
            MichaelLog.i("移除資料收集到的數量：${userSelectDeleteData.size}")
            return
        }
        userSelectDeleteData.add(data)
        checkDeleteButtonEnable()
        MichaelLog.i("移除資料收集到的數量：${userSelectDeleteData.size}")

    }

    fun onDeleteConfirmClickListener() {
        showDeleteConfirmDialog.value = true
    }

    fun onConfirmDeleteClickListener() {
        equipmentListRepository.deleteUserEquipmentData(userSelectDeleteData,object : FireStoreHandler.OnFireStoreCatchDataListener<Unit>{
            override fun onCatchDataSuccess(data: Unit) {
                MichaelLog.i("刪除成功")
                showDeleteListView.value = false
                showDeleteIconLiveData.value = View.GONE
            }

            override fun onCatchDataFail() {
                MichaelLog.i("刪除失敗")
            }
        })
    }


    private fun checkDeleteButtonEnable(){
        if (userSelectDeleteData.isEmpty()){
            deleteButtonEnable.value = false
            return
        }
        deleteButtonEnable.value = true
    }



    fun onDoneButtonClickListener() {
        totalUserEquipmentData.forEach {
            it.isDeleteCheck = false
        }
        updateEquipmentListLiveData.value = totalUserEquipmentData
        showDeleteListView.value = false
        showDeleteIconLiveData.value = View.GONE

    }


    open class EquipmentListViewModelFactory(private val equipmentListRepository: EquipmentListRepository) : ViewModelProvider.NewInstanceFactory(){

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EquipmentListViewModel(equipmentListRepository) as T
        }

    }

}