package com.webxdevelopments.onemorecocoa.common_utils

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Created on 19/02/23.
 */
abstract class BaseBindingActivity <DB : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var binding:DB
    //public lateinit var savedInstanceState: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this.savedInstanceState = savedInstanceState!!
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        performDataBinding()
        initUI()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }
    
    private fun performDataBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutID())
    }


    @LayoutRes
    protected abstract fun getLayoutID(): Int

    protected abstract fun initUI()

    public val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }
}