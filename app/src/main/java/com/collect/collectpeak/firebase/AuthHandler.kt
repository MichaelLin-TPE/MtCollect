package com.collect.collectpeak.firebase

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthHandler {

    companion object{

        private val auth = FirebaseAuth.getInstance()

        fun loginWithFireabseAuth(credential: AuthCredential , authResultOnCompleteListener: OnCompleteListener<AuthResult>){

            auth.signInWithCredential(credential).addOnCompleteListener {
                authResultOnCompleteListener.onComplete(it)
            }

        }

        fun getCurrentUser(): FirebaseUser? = auth.currentUser

        fun isLogin(): Boolean = getCurrentUser() != null

    }


}