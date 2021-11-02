package com.collect.collectpeak.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.collect.collectpeak.MtCollectorActivity
import com.collect.collectpeak.R
import com.collect.collectpeak.main_frame.MainFrameActivity
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.Tool
import com.collect.collectpeak.tool.Tool.Companion.START_ACTIVITY
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.lang.Exception


/**
 * 這個頁面沒什麼需要動的 所以就不走 MVVM
 */

class LaunchActivity : MtCollectorActivity() {


    private var permission: Int = 0

    companion object {

        private const val REQUEST_EXTERNAL_STORAGE = 1

        private val PERMISSIONS_STORAGE = arrayOf(
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTheme(R.style.Theme_CollectPeak)
        //詢問權限
        verifyStoragePermissions(this)


    }


    private fun verifyStoragePermissions(activity: Activity) {

        try {
            permission = ActivityCompat.checkSelfPermission(
                activity,
                "android.permission.WRITE_EXTERNAL_STORAGE"
            )

            if (permission != PackageManager.PERMISSION_GRANTED) {
                MichaelLog.i("沒有開啟內存的權限顯示 Dialog")
                ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
            } else {
                MichaelLog.i("有開啟內存權限 準備進入主頁面")
                goToMainPage();
            }

        } catch (e: Exception) {


            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {

                } else {
                    goToMainPage();
                }
            }
        }

    }

    private fun goToMainPage() {
        MichaelLog.i("有開啟內存權限 準備進入主頁面")
        Handler(Looper.myLooper()!!).postDelayed({

            val intent = Intent(this, MainFrameActivity::class.java)
            startActivity(intent)
            Tool.startActivityInAnim(this, START_ACTIVITY)
            finish()
        }, 1000)
    }
}