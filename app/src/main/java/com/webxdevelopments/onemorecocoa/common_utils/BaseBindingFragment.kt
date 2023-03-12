package com.webxdevelopments.onemorecocoa.common_utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * Created on 01/03/23.
 */
abstract class BaseBindingFragment<DB : ViewDataBinding>: Fragment() {
    protected lateinit var binding: DB
    protected lateinit var savedInstanceState: Bundle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Must be before all mapbox operations
        return getBindingView(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            this.savedInstanceState = savedInstanceState
        }
        initUI()
    }

    private fun getBindingView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, getLayoutID(), container, false)
        return binding.root
    }

    protected abstract fun initUI()


    @LayoutRes
    protected abstract fun getLayoutID(): Int
}