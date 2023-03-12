package com.webxdevelopments.onemorecocoa.views.on_boarding

import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import com.webxdevelopments.onemorecocoa.R
import com.webxdevelopments.onemorecocoa.common_utils.BaseBindingActivity
import com.webxdevelopments.onemorecocoa.common_utils.CommonUtils
import com.webxdevelopments.onemorecocoa.common_utils.PrefUtils
import com.webxdevelopments.onemorecocoa.databinding.ActivityCreateAccountBinding
import com.webxdevelopments.onemorecocoa.views.dashboard.DashboardActivity
import com.webxdevelopments.onemorecocoa.views.firebase_utils.FirebaseAuthAndCreateUser
import com.webxdevelopments.onemorecocoa.views.firebase_utils.FirebaseUsersDB
import com.webxdevelopments.onemorecocoa.views.on_boarding.model.UsersDbModel

class CreateAccountActivity : BaseBindingActivity<ActivityCreateAccountBinding>() {
    private var TAG = CreateAccountActivity::class.java.simpleName.toString()

    override fun getLayoutID(): Int {
        return R.layout.activity_create_account
    }

    override fun initUI() {
        CommonUtils.setEdiTextChangeListener(binding.etCreateAccountEmail, binding.tvCreateAccountEmailError)
        CommonUtils.setEdiTextChangeListener(binding.etCreateAccountFirstName, binding.tvCreateAccountFirstNameError)
        CommonUtils.setEdiTextChangeListener(binding.etCreateAccountLastName, binding.tvCreateAccountLastNameError)
        CommonUtils.setEdiTextChangeListener(binding.etCreateAccountPassword, binding.tvCreateAccountPasswordError)

        CommonUtils.showTopBack(binding.includeLayoutBack, true)
        CommonUtils.showTopTitle(binding.includeLayoutBack, false, "")

        binding.includeLayoutBack.cardBack.setOnClickListener {
            finish()
        }

        binding.tvCreateAccountLoginText.setOnClickListener {
            finish()
        }

        binding.btnCreateAccount.setOnClickListener {
            createAccountValidations()
        }
    }

    private fun createAccountValidations() {
        var isValid = true
        var firstName = binding.etCreateAccountFirstName.text.toString()
        var lastName = binding.etCreateAccountLastName.text.toString()
        var email = binding.etCreateAccountEmail.text.toString()
        var password = binding.etCreateAccountPassword.text.toString()

        if(CommonUtils.isNullOrEmpty(firstName)){
            isValid = false
            CommonUtils.showErrorTv(true, binding.tvCreateAccountFirstNameError, resources.getString(R.string.err_first_name_empty))
        }else{
            if(CommonUtils.isNameValid(firstName)){
                CommonUtils.showErrorTv(false, binding.tvCreateAccountFirstNameError, resources.getString(R.string.err_first_name_invalid))
            }else{
                isValid = false
                CommonUtils.showErrorTv(true, binding.tvCreateAccountFirstNameError, resources.getString(R.string.err_first_name_invalid))
            }
        }

        if(CommonUtils.isNullOrEmpty(lastName)){
            isValid = false
            CommonUtils.showErrorTv(true, binding.tvCreateAccountLastNameError, resources.getString(R.string.err_last_name_empty))
        }else{
            if(CommonUtils.isNameValid(lastName)){
                CommonUtils.showErrorTv(false, binding.tvCreateAccountLastNameError, resources.getString(R.string.err_last_name_invalid))
            }else{
                isValid = false
                CommonUtils.showErrorTv(true, binding.tvCreateAccountLastNameError, resources.getString(R.string.err_last_name_invalid))
            }
        }

        if(CommonUtils.isNullOrEmpty(email)){
            CommonUtils.showErrorTv(true, binding.tvCreateAccountEmailError, resources.getString(R.string.err_email_empty))
        }else{
            if(CommonUtils.isEmailValid(email)){
                CommonUtils.showErrorTv(false, binding.tvCreateAccountEmailError, resources.getString(R.string.err_email_invalid))
            }else{
                isValid = false
                CommonUtils.showErrorTv(true, binding.tvCreateAccountEmailError, resources.getString(R.string.err_email_invalid))
            }
        }

        if(CommonUtils.isNullOrEmpty(password)){
            isValid = false
            CommonUtils.showErrorTv(true, binding.tvCreateAccountPasswordError, resources.getString(R.string.err_password_empty))
        }else{
            CommonUtils.showErrorTv(false, binding.tvCreateAccountPasswordError, resources.getString(R.string.err_password_empty))
        }


        if(isValid){
            if(CommonUtils.isConnectingToInternet(this) == true){
                CommonUtils.showLoader(this)
                FirebaseAuthAndCreateUser().userFirebaseCreate(activity = this, email = email, password = password,
                    object : FirebaseAuthAndCreateUser.FirebaseAuthCallback {
                        override fun firebaseAuthSuccess(user: FirebaseUser?) {
                            val userModel = UsersDbModel(user!!.uid, firstName, lastName, email)
                            FirebaseUsersDB().addUserToDB(this@CreateAccountActivity, userId = user!!.uid, userModel,
                                object : FirebaseUsersDB.AddUserToDbCallback{
                                    override fun addUserDbSuccess(usersDbModel: UsersDbModel) {
                                        CommonUtils.hideLoader()
                                        PrefUtils(this@CreateAccountActivity).saveLoginData(Gson().toJson(usersDbModel))
                                        CommonUtils.showToast(this@CreateAccountActivity, "User Created Successfully!!")
                                        finishAffinity()
                                        val intent = Intent(this@CreateAccountActivity, DashboardActivity::class.java)
                                        startActivity(intent)
                                    }

                                    override fun addUserDbFailure(msg: String) {
                                        CommonUtils.hideLoader()
                                        CommonUtils.showToast(this@CreateAccountActivity, msg)
                                    }

                                })
                        }

                        override fun firebaseAuthFailure(msg: String) {
                            CommonUtils.hideLoader()
                            CommonUtils.showToast(this@CreateAccountActivity, msg)
                        }

                    })

            }else{
                CommonUtils.hideLoader()
                CommonUtils.showToast(this@CreateAccountActivity, resources.getString(R.string.no_internet_connection))
            }
        }
    }

}