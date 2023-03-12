package com.webxdevelopments.onemorecocoa.views.shop_by

import android.app.ActivityOptions
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.card.MaterialCardView
import com.webxdevelopments.onemorecocoa.R
import com.webxdevelopments.onemorecocoa.common_utils.BaseBindingFragment
import com.webxdevelopments.onemorecocoa.databinding.FragmentShopByCategoriesBinding
import com.webxdevelopments.onemorecocoa.views.products.ProductDetailsActivity

class ShopByCategoryFragment : BaseBindingFragment<FragmentShopByCategoriesBinding>() {
    private var TAG = ShopByCategoryFragment::class.java.simpleName.toString()

    override fun getLayoutID(): Int {
        return R.layout.fragment_shop_by_categories
    }

    override fun initUI() {
        setCategorySpinnerAdapter()
        setShopByCategoriesAdapter()
    }

    private fun setCategorySpinnerAdapter() {
        val categories = resources.getStringArray(R.array.shop_by_categories)
        val adapter = ShopByCategorySpinnerAdapter(requireActivity(),categories)
        binding.spinner.adapter = adapter
    }


    private fun setShopByCategoriesAdapter() {
        binding.recyclerCategoriesDataList.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        var productListAdapter = ShopByCategoriesAdapter(requireActivity(), ArrayList<String>(), object :ShopByCategoriesAdapter.ProductListCallback{
            override fun onItemClick(position: Int, cardView: MaterialCardView) {
                val intent = Intent(requireActivity(), ProductDetailsActivity::class.java)
                val options = ActivityOptions.makeSceneTransitionAnimation(requireActivity(), cardView, "product_details")
                startActivity(intent, options.toBundle())
            }
        })
        binding.recyclerCategoriesDataList.adapter = productListAdapter
    }
}