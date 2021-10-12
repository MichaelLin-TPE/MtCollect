package com.collect.collectpeak.fragment.equipment.equipment_select

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.MtCollectorApplication
import com.collect.collectpeak.R
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.TimeTool
import com.google.gson.Gson
import java.util.ArrayList

class EquipmentViewModel(private val equipmentRepository: EquipmentRepository) : ViewModel() {


    val equipmentListLiveData = MutableLiveData<EquipmentListData>()

    val progressBarLiveData = MutableLiveData(View.VISIBLE)

    val loadingDialogLiveData = MutableLiveData<Boolean>()

    val dismissLoadingDialogLiveData = MutableLiveData<Boolean>()

    val finishPageLiveData = MutableLiveData<Boolean>()

    val finishButtonEnable = MutableLiveData<Boolean>()

    val toastLiveData = MutableLiveData<String>()

    private lateinit var selectEquipmentArray : ArrayList<EquipmentData>

    private var targetEquipmentData = EquipmentListData()

    private var equipmentListTitle = TimeTool.getCurrentDate() + " ${
        MtCollectorApplication.getInstance().getContext().getString(
            R.string.equipment_select_list
        )
    }"

    fun onFragmentStart() {

        selectEquipmentArray = ArrayList()

        loadingDialogLiveData.value = false

        dismissLoadingDialogLiveData.value = false;

        finishPageLiveData.value = false;

        finishButtonEnable.value = false

        FireStoreHandler.getInstance().getCurrentUserEquipmentData()


        equipmentRepository.getEquipmentList(object :
            FireStoreHandler.OnFireStoreCatchDataListener<EquipmentListData> {
            override fun onCatchDataSuccess(data: EquipmentListData) {

                progressBarLiveData.value = View.GONE

                MichaelLog.i("最終的資料：${Gson().toJson(data)}")
                targetEquipmentData = data
                equipmentListLiveData.value = data

            }

            override fun onCatchDataFail() {

            }

        })
    }

    fun onEquipmentItemCheckListener(data: EquipmentData) {
        MichaelLog.i("被勾選的裝備：${data.name}")

        targetEquipmentData.equipmentList.forEach {
            updateEquipmentList(it, data)
        }

        equipmentListLiveData.value = targetEquipmentData

        if (selectEquipmentArray.isEmpty()){
            selectEquipmentArray.add(data)
            checkFinishButtonEnable()
            MichaelLog.i("目前選取得裝備數量：${selectEquipmentArray.size}")
            return
        }
        var isFoundSameData = false
        for (equipment in selectEquipmentArray){
            if (equipment.name == data.name){
                selectEquipmentArray.remove(equipment)
                isFoundSameData = true
                break
            }
        }
        if (isFoundSameData){
            checkFinishButtonEnable()
            MichaelLog.i("目前選取得裝備數量：${selectEquipmentArray.size}")
            return
        }
        selectEquipmentArray.add(data)
        checkFinishButtonEnable()
        MichaelLog.i("目前選取得裝備數量：${selectEquipmentArray.size}")

    }

    private fun checkFinishButtonEnable() {
        finishButtonEnable.value = !selectEquipmentArray.isEmpty()
    }

    private fun updateEquipmentList(list: ArrayList<EquipmentData>, data: EquipmentData) {

        run lit@{
            list.forEach {
                if (it.name == data.name) {
                    it.isCheck = data.isCheck
                    return@lit
                }
            }
        }

    }

    fun onEditNameChangeListener(equipmentListTitle: String) {
        this.equipmentListTitle = equipmentListTitle
    }

    fun onButtonDoneClickListener() {

        loadingDialogLiveData.value = true

        //等有使用者了這邊才會開始做
        FireStoreHandler.getInstance().addEquipmentData(selectEquipmentArray,equipmentListTitle,object : FireStoreHandler.OnFireStoreCatchDataListener<Unit>{
            override fun onCatchDataSuccess(data: Unit) {
                dismissLoadingDialogLiveData.value = true
                finishPageLiveData.value = true
            }

            override fun onCatchDataFail() {
                dismissLoadingDialogLiveData.value = true;
                toastLiveData.value = "錯誤，請重新再試一次。"
            }

        })



    }


    open class EquipmentViewModelFactory(private val equipmentRepository: EquipmentRepository) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EquipmentViewModel(equipmentRepository) as T
        }
    }

}