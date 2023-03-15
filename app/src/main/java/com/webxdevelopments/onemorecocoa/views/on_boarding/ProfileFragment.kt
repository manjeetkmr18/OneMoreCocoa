package com.webxdevelopments.onemorecocoa.views.on_boarding

import com.google.gson.Gson
import com.webxdevelopments.onemorecocoa.R
import com.webxdevelopments.onemorecocoa.common_utils.BaseBindingFragment
import com.webxdevelopments.onemorecocoa.common_utils.CommonUtils
import com.webxdevelopments.onemorecocoa.common_utils.PrefUtils
import com.webxdevelopments.onemorecocoa.databinding.FragmentProfileBinding
import com.webxdevelopments.onemorecocoa.views.firebase_utils.FirebaseUsersDB
import com.webxdevelopments.onemorecocoa.views.on_boarding.model.UsersDbModel

class ProfileFragment : BaseBindingFragment<FragmentProfileBinding>() {
    private var TAG = ProfileFragment::class.java.simpleName.toString()
    private lateinit var userModel: UsersDbModel

    override fun getLayoutID(): Int {
        return R.layout.fragment_profile
    }

    override fun initUI() {
        userModel = PrefUtils(requireActivity()).getUserLoginData()!!
        if(userModel != UsersDbModel()){
            val firstName = userModel!!.user_first_name
            binding.etCreateAccountFirstName.setText(firstName.toString().trim())
            val lastName = userModel!!.user_last_name
            binding.etCreateAccountLastName.setText(lastName.toString().trim())
            val email = userModel!!.email
            binding.etCreateAccountEmail.setText(email.toString().trim())
        }
        binding.btnCreateAccount.setOnClickListener {
            if(userModel != UsersDbModel()) {
                profileUpdate()
            }
        }
    }


    private fun profileUpdate() {
        var isValid = true
        var firstName = binding.etCreateAccountFirstName.text.toString()
        var lastName = binding.etCreateAccountLastName.text.toString()
        var email = binding.etCreateAccountEmail.text.toString()

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



        if(isValid){
            if(CommonUtils.isConnectingToInternet(requireActivity()) == true){
                CommonUtils.showLoader(requireActivity())
                val userModel = UsersDbModel(userModel.user_id, firstName, lastName, email)
                FirebaseUsersDB().addUserToDB(requireActivity(), userId = userModel.user_id, userModel,
                    object : FirebaseUsersDB.AddUserToDbCallback{
                        override fun addUserDbSuccess(usersDbModel: UsersDbModel) {
                            CommonUtils.hideLoader()
                            PrefUtils(requireActivity()).saveLoginData(Gson().toJson(usersDbModel))
                            CommonUtils.showToast(requireActivity(), "Profile Updated Successfully!!")

                        }

                        override fun addUserDbFailure(msg: String) {
                            CommonUtils.hideLoader()
                            CommonUtils.showToast(requireActivity(), msg)
                        }

                    })

            }else{
                CommonUtils.hideLoader()
                CommonUtils.showToast(requireActivity(), resources.getString(R.string.no_internet_connection))
            }
        }
    }
}