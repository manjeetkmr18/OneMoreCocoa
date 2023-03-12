package com.webxdevelopments.onemorecocoa.views.on_boarding

import com.webxdevelopments.onemorecocoa.R
import com.webxdevelopments.onemorecocoa.common_utils.BaseBindingFragment
import com.webxdevelopments.onemorecocoa.databinding.FragmentProfileBinding

class ProfileFragment : BaseBindingFragment<FragmentProfileBinding>() {
    private var TAG = ProfileFragment::class.java.simpleName.toString()

    override fun getLayoutID(): Int {
        return R.layout.fragment_profile
    }

    override fun initUI() {

    }
}