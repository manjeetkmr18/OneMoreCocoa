package com.webxdevelopments.onemorecocoa.views.on_boarding

import android.app.ActivityOptions
import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import com.webxdevelopments.onemorecocoa.R
import com.webxdevelopments.onemorecocoa.common_utils.BaseBindingActivity
import com.webxdevelopments.onemorecocoa.common_utils.CommonUtils
import com.webxdevelopments.onemorecocoa.common_utils.PrefUtils
import com.webxdevelopments.onemorecocoa.databinding.ActivityLoginBinding
import com.webxdevelopments.onemorecocoa.views.dashboard.DashboardActivity
import com.webxdevelopments.onemorecocoa.views.firebase_utils.FirebaseAuthAndCreateUser
import com.webxdevelopments.onemorecocoa.views.firebase_utils.FirebaseUsersDB
import com.webxdevelopments.onemorecocoa.views.on_boarding.model.UsersDbModel

class LoginActivity : BaseBindingActivity<ActivityLoginBinding>() {
    private var TAG = LoginActivity::class.java.simpleName.toString()

    override fun getLayoutID(): Int {
        return R.layout.activity_login
    }

    override fun initUI() {
        CommonUtils.setEdiTextChangeListener(binding.etLoginEmail, binding.tvLoginEmailError)
        CommonUtils.setEdiTextChangeListener(binding.etLoginPassword, binding.tvLoginPasswordError)
        binding.tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(this, binding.tvForgotPassword, "forgot_password")
            startActivity(intent, options.toBundle())
        }
        binding.tvLoginRegisterText.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(this, binding.tvLoginRegisterText, "create_account")
            startActivity(intent, options.toBundle())
        }
        binding.btnLogin.setOnClickListener {
            loginValidations()
        }

    }


    private fun loginValidations() {
        var isValid = true
        var email = binding.etLoginEmail.text.toString()
        var password = binding.etLoginPassword.text.toString()

        if(CommonUtils.isNullOrEmpty(email)){
            isValid = false
            CommonUtils.showErrorTv(true, binding.tvLoginEmailError, resources.getString(R.string.err_email_empty))
        }else{
            if(CommonUtils.isEmailValid(email)){
                CommonUtils.showErrorTv(false, binding.tvLoginEmailError, resources.getString(R.string.err_email_invalid))
            }else{
                isValid = false
                CommonUtils.showErrorTv(true, binding.tvLoginEmailError, resources.getString(R.string.err_email_invalid))
            }
        }

        if(CommonUtils.isNullOrEmpty(password)){
            isValid = false
            CommonUtils.showErrorTv(true, binding.tvLoginPasswordError, resources.getString(R.string.err_password_empty))
        }else{
            CommonUtils.showErrorTv(false, binding.tvLoginPasswordError, resources.getString(R.string.err_password_empty))
        }


        if(isValid){
            if(CommonUtils.isConnectingToInternet(this) == true){
                CommonUtils.showLoader(this)
                FirebaseAuthAndCreateUser().userFirebaseAuth(activity = this, email = email, password = password,
                    object : FirebaseAuthAndCreateUser.FirebaseAuthCallback {
                        override fun firebaseAuthSuccess(user: FirebaseUser?) {
                           FirebaseUsersDB().getUserFromDB(this@LoginActivity, userId = user!!.uid, object : FirebaseUsersDB.GetUsersFromDbCallback{
                               override fun getUserDbSuccess(usersDbModel: UsersDbModel) {
                                   PrefUtils(this@LoginActivity).saveLoginData(Gson().toJson(usersDbModel))
                                   CommonUtils.hideLoader()
                                   CommonUtils.showToast(this@LoginActivity, "User Login Successfully!!")
                                   finishAffinity()
                                   val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                                   startActivity(intent)
                               }

                               override fun getUserDbFailure(msg: String) {
                                   CommonUtils.hideLoader()
                                   CommonUtils.showToast(this@LoginActivity, msg)
                               }

                           })
                        }

                        override fun firebaseAuthFailure(msg: String) {
                            CommonUtils.hideLoader()
                            CommonUtils.showToast(this@LoginActivity, msg)
                        }
                    })
            }else{
                CommonUtils.hideLoader()
                CommonUtils.showToast(this@LoginActivity, resources.getString(R.string.no_internet_connection))
            }
        }
    }


}