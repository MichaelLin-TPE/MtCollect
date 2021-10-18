package com.collect.collectpeak.firebase

import com.collect.collectpeak.tool.GoogleLoginTool
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.*

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


        fun logout() {
            GoogleLoginTool.signOut()
            auth.signOut()
        }

    }


}