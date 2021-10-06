package com.collect.collectpeak.tool

import android.annotation.SuppressLint
import android.app.Activity
import com.collect.collectpeak.MtCollectorApplication
import com.collect.collectpeak.R
import com.collect.collectpeak.firebase.AuthHandler
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.log.MichaelLog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.Exception

class GoogleLoginTool {

    companion object {

        private lateinit var currentUser: FirebaseUser

        private val options: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(
                    MtCollectorApplication.getInstance().getContext()
                        .getString(R.string.default_web_client_id)
                )
                .requestEmail()
                .build()

        @SuppressLint("StaticFieldLeak")
        private val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(
            MtCollectorApplication.getInstance().getContext(),
            options
        )

        @SuppressLint("StaticFieldLeak")
        private lateinit var activity: Activity

        const val RC_SIGN_IN = 666

        /**
         * 要先給Activity
         */
        fun setActivity(activity: Activity) {
            this.activity = activity
        }

        /**
         * 才可以開始執行登入作業
         */
        fun startToLoginFlow() {
            MichaelLog.i("開始登入流程")
            val it = googleSignInClient.signInIntent
            activity.startActivityForResult(it, RC_SIGN_IN)
        }

        fun onCatchLoginResult(
            task: Task<GoogleSignInAccount>,
            onGoogleLoginResultListener: OnGoogleLoginResultListener
        ) {

            try {

                MichaelLog.i("登入成功")
                val account = task.getResult(ApiException::class.java)
                if (account == null) {
                    MichaelLog.i("account is null login fail")
                    onGoogleLoginResultListener.onFail()
                    return
                }
                registerFirebaseAuth(account, onGoogleLoginResultListener)


            } catch (e: Exception) {
                e.printStackTrace()
                MichaelLog.i("exception : $e")
                onGoogleLoginResultListener.onFail()
            }


        }

        private fun registerFirebaseAuth(
            account: GoogleSignInAccount,
            onGoogleLoginResultListener: OnGoogleLoginResultListener
        ) {

            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            AuthHandler.loginWithFireabseAuth(credential) {

                checkLoginSuccess(it, onGoogleLoginResultListener)

            }


        }

        private fun checkLoginSuccess(
            it: Task<AuthResult>,
            onGoogleLoginResultListener: OnGoogleLoginResultListener
        ) {
            if (it.isSuccessful) {

                val currentUser = AuthHandler.getCurrentUser()

                if (currentUser == null) {
                    onGoogleLoginResultListener.onFail()
                    return
                }

                this.currentUser = currentUser

                checkNeedToRegisterAccount(onGoogleLoginResultListener)

            } else {

                onGoogleLoginResultListener.onFail()

            }
        }

        private fun checkNeedToRegisterAccount(onGoogleLoginResultListener: OnGoogleLoginResultListener) {
            FireStoreHandler.getInstance().checkUserExistListener(currentUser,
                object : FireStoreHandler.OnCheckUserExistResultListener {
                    override fun onUserExist() {
                        MichaelLog.i("userExist")
                        onGoogleLoginResultListener.onSuccess()
                    }

                    override fun onNeedToAddNewProfile() {
                        MichaelLog.i("onNeedToAddNewProfile")
                        onGoogleLoginResultListener.onNeedAddNewProfile()
                    }

                    override fun onFail() {
                        MichaelLog.i("onFail")
                        onGoogleLoginResultListener.onFail()
                    }
                }
            )
        }


    }

    interface OnGoogleLoginResultListener {
        fun onSuccess()
        fun onNeedAddNewProfile()
        fun onFail()
    }

}