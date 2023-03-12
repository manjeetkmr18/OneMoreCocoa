package com.webxdevelopments.onemorecocoa.views.firebase_utils

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


/**
 * Created on 12/03/23.
 */
class FirebaseAuthAndCreateUser {
    var TAG = FirebaseAuthAndCreateUser::class.java.simpleName

    fun userFirebaseAuth(activity: Activity,
                         email:String,
                         password:String,
                         firebaseAuthCallback: FirebaseAuthCallback
    ){

        val auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.e(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    firebaseAuthCallback.firebaseAuthSuccess(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e(TAG, "signInWithEmail:failure=="+ task.exception!!.message)
                    firebaseAuthCallback.firebaseAuthFailure(task.exception!!.message.toString())
                }
            }
    }

    fun userFirebaseCreate(activity: Activity,
                         email:String,
                         password:String,
                         firebaseAuthCallback: FirebaseAuthCallback
    ){
        val auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    firebaseAuthCallback.firebaseAuthSuccess(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e(TAG, "createUserWithEmail:failure=="+ task.exception!!.message)
                    firebaseAuthCallback.firebaseAuthFailure(task.exception!!.message.toString())

                }
            }
    }

    public interface FirebaseAuthCallback{
        fun firebaseAuthSuccess(user: FirebaseUser?)
        fun firebaseAuthFailure(msg:String)
    }

    public fun forgotPassword(email:String, firebaseForgotPasswordCallback:FirebaseForgotPasswordCallback){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseForgotPasswordCallback.firebaseForgotPasswordSuccessOrFailure(true, "Email sent!!")
                }else{
                    firebaseForgotPasswordCallback.firebaseForgotPasswordSuccessOrFailure(false, task.exception!!.message.toString())
                }
            }
    }

    public interface FirebaseForgotPasswordCallback{
        fun firebaseForgotPasswordSuccessOrFailure(isSuccess:Boolean, msg:String)
    }
}