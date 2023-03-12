package com.webxdevelopments.onemorecocoa.views.on_boarding

import android.content.Intent
import android.os.Handler
import com.webxdevelopments.onemorecocoa.R
import com.webxdevelopments.onemorecocoa.common_utils.BaseBindingActivity
import com.webxdevelopments.onemorecocoa.common_utils.PrefUtils
import com.webxdevelopments.onemorecocoa.databinding.ActivitySplashBinding
import com.webxdevelopments.onemorecocoa.views.dashboard.DashboardActivity
import com.webxdevelopments.onemorecocoa.views.on_boarding.model.UsersDbModel


class SplashActivity : BaseBindingActivity<ActivitySplashBinding>() {
    private var TAG = SplashActivity::class.java.simpleName.toString()

    override fun getLayoutID(): Int {
        return R.layout.activity_splash
    }

    override fun initUI() {
        val userDbModel = PrefUtils(this).getUserLoginData()
        Handler().postDelayed(
            Runnable {
                if(userDbModel == UsersDbModel()) {
                    finishAffinity()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }else{
                    finishAffinity()
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }
            },
            2000
        )


    }

}