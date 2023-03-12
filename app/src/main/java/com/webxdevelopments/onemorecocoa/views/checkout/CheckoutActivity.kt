package com.webxdevelopments.onemorecocoa.views.checkout

import com.webxdevelopments.onemorecocoa.R
import com.webxdevelopments.onemorecocoa.common_utils.BaseBindingActivity
import com.webxdevelopments.onemorecocoa.common_utils.CommonUtils
import com.webxdevelopments.onemorecocoa.databinding.ActivityCheckoutBinding

class CheckoutActivity : BaseBindingActivity<ActivityCheckoutBinding>() {
    private var TAG = CheckoutActivity::class.java.simpleName.toString()

    override fun getLayoutID(): Int {
        return R.layout.activity_checkout
    }

    override fun initUI() {
        CommonUtils.showTopBack(binding.includeLayoutBack, true)
        CommonUtils.showTopTitle(binding.includeLayoutBack, true, "Checkout")
        binding.includeLayoutBack.cardBack.setOnClickListener {
            finish()
        }
    }
}