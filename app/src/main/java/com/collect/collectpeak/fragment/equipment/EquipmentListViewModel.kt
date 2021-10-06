package com.collect.collectpeak.fragment.equipment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EquipmentListViewModel(private val equipmentListRepository: EquipmentListRepository) : ViewModel() {



    fun onFragmentStart() {

    }


    open class EquipmentListViewModelFactory(private val equipmentListRepository: EquipmentListRepository) : ViewModelProvider.NewInstanceFactory(){

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EquipmentListViewModel(equipmentListRepository) as T
        }

    }

}