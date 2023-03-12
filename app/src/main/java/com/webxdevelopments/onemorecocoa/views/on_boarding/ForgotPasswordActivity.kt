package com.webxdevelopments.onemorecocoa.views.on_boarding

import android.widget.Toast
import com.webxdevelopments.onemorecocoa.R
import com.webxdevelopments.onemorecocoa.common_utils.BaseBindingActivity
import com.webxdevelopments.onemorecocoa.common_utils.CommonUtils
import com.webxdevelopments.onemorecocoa.databinding.ActivityForgotPasswordBinding
import com.webxdevelopments.onemorecocoa.views.firebase_utils.FirebaseAuthAndCreateUser

class ForgotPasswordActivity : BaseBindingActivity<ActivityForgotPasswordBinding>() {
    private var TAG = ForgotPasswordActivity::class.java.simpleName.toString()

    override fun getLayoutID(): Int {
        return R.layout.activity_forgot_password
    }

    override fun initUI() {
        CommonUtils.setEdiTextChangeListener(binding.etForgotPassEmail, binding.tvForgotPasswordEmailError)
        CommonUtils.showTopBack(binding.includeLayoutBack, true)
        CommonUtils.showTopTitle(binding.includeLayoutBack, false, "")
        binding.includeLayoutBack.cardBack.setOnClickListener {
            finish()
        }
        binding.btnForgotPasswordSubmit.setOnClickListener {
            forgotPasswordLink()
        }
    }

    private fun forgotPasswordLink() {
        var isValid = true
        var email = binding.etForgotPassEmail.text.toString()

        if(CommonUtils.isNullOrEmpty(email)){
            isValid = false
            CommonUtils.showErrorTv(true, binding.tvForgotPasswordEmailError, resources.getString(R.string.err_email_empty))
        }else{
            if(CommonUtils.isEmailValid(email)){
                CommonUtils.showErrorTv(false, binding.tvForgotPasswordEmailError, resources.getString(R.string.err_email_invalid))
            }else{
                isValid = false
                CommonUtils.showErrorTv(true, binding.tvForgotPasswordEmailError, resources.getString(R.string.err_email_invalid))
            }
        }

        if(isValid){
            if(CommonUtils.isConnectingToInternet(this) == true) {
                CommonUtils.showLoader(this)
                FirebaseAuthAndCreateUser().forgotPassword(email, object : FirebaseAuthAndCreateUser.FirebaseForgotPasswordCallback {
                    override fun firebaseForgotPasswordSuccessOrFailure(
                        isSuccess: Boolean,
                        msg: String
                    ) {
                        CommonUtils.hideLoader()
                        CommonUtils.showToast(this@ForgotPasswordActivity, msg)
                    }

                })
            }else{
                CommonUtils.hideLoader()
                CommonUtils.showToast(this@ForgotPasswordActivity, resources.getString(R.string.no_internet_connection))
            }
        }
    }

}