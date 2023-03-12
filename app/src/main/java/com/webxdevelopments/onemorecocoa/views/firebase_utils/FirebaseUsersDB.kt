package com.webxdevelopments.onemorecocoa.views.firebase_utils

import android.app.Activity
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.webxdevelopments.onemorecocoa.views.on_boarding.model.UsersDbModel

/**
 * Created on 12/03/23.
 */
class FirebaseUsersDB {
    private var TAG = FirebaseUsersDB::class.java.simpleName.toString()

    public fun addUserToDB(activity: Activity, userId:String, usersDbModel: UsersDbModel, addUserToDbCallback:AddUserToDbCallback){
       Firebase.database.reference.child(FirebaseConstants.DB_USERS).child(userId).
       setValue(usersDbModel).addOnCompleteListener(activity) { task ->
           if (task.isSuccessful) {
               // Sign in success, update UI with the signed-in user's information
               Log.e(TAG, "signInWithEmail:success")
               addUserToDbCallback.addUserDbSuccess(usersDbModel)
           } else {
               // If sign in fails, display a message to the user.
               Log.e(TAG, "signInWithEmail:failure=="+ task.exception!!.message)
               addUserToDbCallback.addUserDbFailure(task.exception!!.message.toString())
           }
       }
    }

    public interface AddUserToDbCallback{
        public fun addUserDbSuccess(usersDbModel: UsersDbModel)
        public fun addUserDbFailure(msg:String)
    }


    public fun getUserFromDB(activity: Activity, userId:String, getUsersFromDbCallback:GetUsersFromDbCallback){
       Firebase.database.reference.child(FirebaseConstants.DB_USERS).child(userId).addValueEventListener(
           object: ValueEventListener{
           override fun onDataChange(snapshot: DataSnapshot) {
               if(snapshot.exists()) {
                   val usersDbModel= snapshot.getValue<UsersDbModel>(UsersDbModel::class.java)
                   getUsersFromDbCallback.getUserDbSuccess(usersDbModel!!)
               }else{
                   getUsersFromDbCallback.getUserDbFailure("User not found!!")
               }
           }

           override fun onCancelled(error: DatabaseError) {
               getUsersFromDbCallback.getUserDbFailure(error.message.toString())
           }

       })

    }

    public interface GetUsersFromDbCallback{
        public fun getUserDbSuccess(usersDbModel: UsersDbModel)
        public fun getUserDbFailure(msg:String)
    }
}