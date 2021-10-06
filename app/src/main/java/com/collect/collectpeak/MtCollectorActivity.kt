package com.collect.collectpeak

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * 這會是共同繼承的 Activity
 */

open class MtCollectorActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    /**
     * 顯示 Dialog
     */
    fun showDialog(title:String,content:String){

    }

}