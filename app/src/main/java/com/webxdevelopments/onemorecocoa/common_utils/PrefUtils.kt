package com.webxdevelopments.onemorecocoa.common_utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.webxdevelopments.onemorecocoa.views.on_boarding.model.UsersDbModel

/**
 * Created on 12/03/23.
 */
class PrefUtils(val context: Context) {

    companion object {
        var PRIVATE_MODE = 0
        const val LITTLE_LIT_PREF = "little_lit_pref"
        const val LOGIN_DATA = "login_data"
        const val AUTH_TOKEN = "AUTH_TOKEN"
    }


    private val TAG = "PrefUtils"

    var mPreference: SharedPreferences = context.getSharedPreferences(LITTLE_LIT_PREF, Context.MODE_PRIVATE)

    val gson = Gson()

    fun clearAllData() {
        context.getSharedPreferences(LITTLE_LIT_PREF,  Context.MODE_PRIVATE).edit().clear().commit();
    }

    fun clear(tag: String) {
        mPreference.edit().remove(tag).apply()
    }

    fun saveLoginData(data: String?) {
        Log.e(TAG+"saveLoginData---",""+data)
        val editor = mPreference.edit()
        editor.putString(LOGIN_DATA, data).apply()
    }

    fun getUserLoginData(): UsersDbModel?{
        var vehicleStr = mPreference.getString(LOGIN_DATA, "{}")
        Log.e(TAG+"getUserLoginData---",""+vehicleStr)

        if(vehicleStr != "{}"){
            return Gson().fromJson(vehicleStr, UsersDbModel::class.java)
        }
        return UsersDbModel()
    }
}