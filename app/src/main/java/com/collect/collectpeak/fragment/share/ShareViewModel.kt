package com.collect.collectpeak.fragment.share

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ShareViewModel(private val repository: ShareRepository) : ViewModel() {


    open class ShareViewModelFactory(private val repository: ShareRepository) : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ShareViewModel(repository) as T
        }
    }

}