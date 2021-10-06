package com.collect.collectpeak.firebase

import android.os.Handler
import android.os.Looper
import com.collect.collectpeak.fragment.member.MemberRepository
import com.google.firebase.storage.FirebaseStorage

class StorageHandler {

    companion object{

        private val storage = FirebaseStorage.getInstance().reference

        fun uploadPhoto(byteArray : ByteArray,onUploadPhotoListener: MemberRepository.OnUploadPhotoListener){

            val river = storage.child("userPhoto/${AuthHandler.getCurrentUser()?.uid}/${AuthHandler.getCurrentUser()?.uid}.jpg")
            val task = river.putBytes(byteArray)
            task.addOnSuccessListener {
                 river.downloadUrl.addOnSuccessListener {
                     val photoUrl = it.toString()
                     updateUserData(photoUrl,onUploadPhotoListener)
                 }
            }.addOnFailureListener{
                onUploadPhotoListener.onFail()
            }

        }

        private fun updateUserData(photoUrl: String, onUploadPhotoListener: MemberRepository.OnUploadPhotoListener) {
            val map = HashMap<String,String>()
            map["photo"] = photoUrl
            FireStoreHandler.getInstance().setUserPhotoData(map)
            Handler(Looper.myLooper()!!).postDelayed({

                onUploadPhotoListener.onSuccess()

            },500)

        }


    }


}