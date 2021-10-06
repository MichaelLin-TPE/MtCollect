package com.collect.collectpeak.fragment.member

import com.collect.collectpeak.firebase.StorageHandler

class MemberRepositoryImpl : MemberRepository{


    /**
     * 上傳照片
     */
    override fun onUpdateLoadPhoto(bytes: ByteArray, onUploadPhotoListener: MemberRepository.OnUploadPhotoListener) {


        StorageHandler.uploadPhoto(bytes,object : MemberRepository.OnUploadPhotoListener{
            override fun onSuccess() {
               onUploadPhotoListener.onSuccess()
            }

            override fun onFail() {
                onUploadPhotoListener.onFail()
            }

        })

    }
}