package com.collect.collectpeak.fragment.member

interface MemberRepository {
    fun onUpdateLoadPhoto(bytes: ByteArray , onUploadPhotoListener: OnUploadPhotoListener)


    interface OnUploadPhotoListener{
        fun onSuccess()
        fun onFail()
    }
}