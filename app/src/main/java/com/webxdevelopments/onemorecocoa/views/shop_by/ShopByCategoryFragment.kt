package com.webxdevelopments.onemorecocoa.views.shop_by

import android.app.ActivityOptions
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.card.MaterialCardView
import com.webxdevelopments.onemorecocoa.R
import com.webxdevelopments.onemorecocoa.common_utils.BaseBindingFragment
import com.webxdevelopments.onemorecocoa.common_utils.CommonUtils
import com.webxdevelopments.onemorecocoa.databinding.FragmentShopByCategoriesBinding
import com.webxdevelopments.onemorecocoa.views.firebase_utils.FirebaseProductsDB
import com.webxdevelopments.onemorecocoa.views.products.ProductDetailsActivity
import com.webxdevelopments.onemorecocoa.views.products.model.ProductsModel
import java.util.*
import kotlin.collections.ArrayList

class ShopByCategoryFragment : BaseBindingFragment<FragmentShopByCategoriesBinding>() {
    private var TAG = ShopByCategoryFragment::class.java.simpleName.toString()

    private var whiteChocolateList = ArrayList<ProductsModel>()
    private var milkChocolateList = ArrayList<ProductsModel>()
    private var darkChocolateList = ArrayList<ProductsModel>()
    private var selectedCategoryProducts = ArrayList<ProductsModel>()
    private lateinit var shopByCategoriesAdapter:ShopByCategoriesAdapter
    var categories: ArrayList<String> = ArrayList<String>(
        listOf("Milk Chocolate", "White Chocolate", "Dark Chocolate")
    )
    override fun getLayoutID(): Int {
        return R.layout.fragment_shop_by_categories
    }

    override fun initUI() {
        setCategorySpinnerAdapter()
        setShopByCategoriesAdapter()
        getProductsList()
    }

    private fun setCategorySpinnerAdapter() {
        val adapter = ShopByCategorySpinnerAdapter(requireActivity(),categories)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                getSelectedCategoryList(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.e(TAG, "onNothingSelected")
            }

         }
    }


    private fun setShopByCategoriesAdapter() {
        binding.recyclerCategoriesDataList.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        shopByCategoriesAdapter = ShopByCategoriesAdapter(requireActivity(),selectedCategoryProducts, object :ShopByCategoriesAdapter.ProductListCallback{
            override fun onItemClick(position: Int, cardView: MaterialCardView) {
                val intent = Intent(requireActivity(), ProductDetailsActivity::class.java)
                val options = ActivityOptions.makeSceneTransitionAnimation(requireActivity(), cardView, "product_details")
                startActivity(intent, options.toBundle())
            }
        })
        binding.recyclerCategoriesDataList.adapter = shopByCategoriesAdapter
    }

    private fun noProductsData(isVisi:Boolean){
        if(isVisi){
            binding.tvNoData.visibility = View.VISIBLE
            binding.recyclerCategoriesDataList.visibility = View.GONE
        }else{
            binding.tvNoData.visibility = View.GONE
            binding.recyclerCategoriesDataList.visibility = View.VISIBLE
        }
    }


    private fun getProductsList(){
        if(CommonUtils.isConnectingToInternet(requireActivity()) == true){
            CommonUtils.showLoader(requireActivity())
            FirebaseProductsDB().getProductsList(requireActivity(), object :
                FirebaseProductsDB.GetProductsListFromDbCallback{
                override fun getProductsListDbSuccess(productsModelList: ArrayList<ProductsModel>) {
                    if(productsModelList.size>0){
                        milkChocolateList.clear()
                        darkChocolateList.clear()
                        whiteChocolateList.clear()
                        for(i in 0 until productsModelList.size) {
                            var productsModel = productsModelList[i]
                            if(productsModel.category == categories[0]){
                                milkChocolateList.add(productsModel)
                            }
                            if(productsModel.category == categories[1]){
                                whiteChocolateList.add(productsModel)
                            }
                            if(productsModel.category == categories[2]){
                                darkChocolateList.add(productsModel)
                            }
                        }
                        
                        getSelectedCategoryList(binding.spinner.selectedItemPosition)
                        noProductsData(false)
                    }else{
                        noProductsData(true)
                    }
                    CommonUtils.hideLoader()
                }

                override fun getProductsListDbFailure(msg: String) {
                    CommonUtils.hideLoader()
                    CommonUtils.showToast(requireActivity(), msg)
                }

            })
        }else{
            CommonUtils.hideLoader()
            CommonUtils.showToast(requireActivity(), resources.getString(R.string.no_internet_connection))
        }
    }

    private fun getSelectedCategoryList(position:Int){
        if(position == 0){
            selectedCategoryProducts.clear()
            selectedCategoryProducts.addAll(milkChocolateList)
        }else if(position == 1){
            selectedCategoryProducts.clear()
            selectedCategoryProducts.addAll(whiteChocolateList)
        }else{
            selectedCategoryProducts.clear()
            selectedCategoryProducts.addAll(darkChocolateList)
        }
        shopByCategoriesAdapter.notifyDataSetChanged()
    }
}