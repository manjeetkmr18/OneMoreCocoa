package com.webxdevelopments.onemorecocoa.common_utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.webxdevelopments.onemorecocoa.R
import com.webxdevelopments.onemorecocoa.databinding.IncludeLayoutBackBinding
import com.webxdevelopments.onemorecocoa.views.on_boarding.LoginActivity
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * Created on 01/03/23.
 */
object CommonUtils {

    public fun showTopTitle(binding: IncludeLayoutBackBinding, isVisi: Boolean, text:String){
        if(isVisi){
            binding.tvLayoutTitle.visibility= View.VISIBLE
        }else{
            binding.tvLayoutTitle.visibility= View.GONE
        }
        binding.tvLayoutTitle.text = text
    }

    public fun showTopBack(binding: IncludeLayoutBackBinding, isVisi: Boolean){
        if(isVisi){
            binding.cardBack.visibility= View.VISIBLE
        }else{
            binding.cardBack.visibility= View.INVISIBLE
        }
    }

    public fun setFontToTextView(activity: Activity, fontName:String, textView:TextView){
        val tf = Typeface.createFromAsset(
            activity.assets,
            fontName
        )
        textView.typeface = tf
    }

    fun isNullOrEmpty(str: String): Boolean {
        return (str == "" || str == null || str == "null" || str.isEmpty())
    }

    fun firstLetterUppercase(content: String): String? {
        return if (!isNullOrEmpty(content)) {
            content.substring(0, 1).uppercase(Locale.getDefault()) + content.substring(1).toString().lowercase()
        } else content
    }


    fun isEmailValid(email: String?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isNameValid(name:String): Boolean {
        val pattern1: Pattern = Pattern.compile("^[A-Za-z]+\$")
        val matcher1: Matcher = pattern1.matcher(name)
        return matcher1.matches()
    }

    fun setEdiTextChangeListener(editText: EditText, errTv: TextView) {

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.e("setEdiTextChangeListener---","beforeTextChanged")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.e("setEdiTextChangeListener---","onTextChanged")
                errTv.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
                Log.e("setEdiTextChangeListener---","afterTextChanged")
            }

        })
    }

    public fun showErrorTv(isShow:Boolean, errTv:TextView, errMsg:String = ""){
        if(isShow){
            errTv.visibility = View.VISIBLE
            errTv.text = errMsg
        }else{
            errTv.visibility = View.GONE
            errTv.text = ""
        }
    }

    public fun showToast(context: Context, msg:String){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun isConnectingToInternet(context: Context?): Boolean? {
        val connectivity =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            val info = connectivity.allNetworkInfo
            if (info != null) for (i in info.indices) {
                if (info[i].state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
        }
        return false
    }

    lateinit var dialog: Dialog
    fun showLoader(context: Context) {
        if(this::dialog.isInitialized){
            if(dialog == null){
                dialog = getLoadingDialog(context)
                dialog.setCanceledOnTouchOutside(false)
                dialog.setCancelable(false)
                dialog?.show()
            }else{
                if(!dialog.isShowing){
                    dialog = getLoadingDialog(context)
                    dialog.setCanceledOnTouchOutside(false)
                    dialog.setCancelable(false)
                    dialog?.show()
                }
            }
        }else{
            dialog = getLoadingDialog(context)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
            dialog?.show()
        }
    }

    fun hideLoader() {
        try {
            if(this::dialog.isInitialized) {
                if (dialog != null) {
                    if (dialog.isShowing) {
                        dialog.cancel()
                        dialog.dismiss()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getLoadingDialog(context: Context): Dialog {
        val dialog = Dialog(context,android.R.style.Theme_Material_Light_NoActionBar)
        dialog.window?.setBackgroundDrawableResource(R.color.new_transparent);

        dialog.setContentView(R.layout.loader_layout)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(false)
        return dialog

    }

    public fun logoutUser(activity:Activity){
        PrefUtils(activity).clearAllData()
        FirebaseAuth.getInstance().signOut()
        activity.finishAffinity()
        activity.finishAffinity()
        val intent = Intent(activity, LoginActivity::class.java)
        activity.startActivity(intent)
    }
}