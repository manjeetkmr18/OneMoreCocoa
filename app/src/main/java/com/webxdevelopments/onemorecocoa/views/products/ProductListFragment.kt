package com.webxdevelopments.onemorecocoa.views.products

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import com.webxdevelopments.onemorecocoa.R
import com.webxdevelopments.onemorecocoa.common_utils.BaseBindingFragment
import com.webxdevelopments.onemorecocoa.common_utils.CommonUtils
import com.webxdevelopments.onemorecocoa.common_utils.Constants
import com.webxdevelopments.onemorecocoa.databinding.FragmentProductListBinding
import com.webxdevelopments.onemorecocoa.views.firebase_utils.FirebaseProductsDB
import com.webxdevelopments.onemorecocoa.views.products.model.ProductsModel

class ProductListFragment :  BaseBindingFragment<FragmentProductListBinding>() {
    private var TAG = ProductListFragment::class.java.simpleName.toString()

    private var productsList = ArrayList<ProductsModel>()
    private lateinit var productListAdapter:ProductListAdapter

    override fun getLayoutID(): Int {
        return R.layout.fragment_product_list
    }

    override fun initUI() {
        setProductListAdapter()
        getProductsList()
    }

    private fun setProductListAdapter() {
        binding.recyclerProductList.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        productListAdapter = ProductListAdapter(requireActivity(), productsList, "",object :ProductListAdapter.ProductListCallback{
            override fun onItemClick(position: Int, cardView: MaterialCardView) {

                val intent = Intent(requireActivity(), ProductDetailsActivity::class.java)
                val productsModel = productsList[position]
                val bundle = Bundle()
                bundle.putString(Constants.INTENT_PRODUCT_DETAIL, Gson().toJson(productsModel))
                intent.putExtras(bundle)
                val options = ActivityOptions.makeSceneTransitionAnimation(requireActivity(), cardView, "product_details")
                startActivity(intent, options.toBundle())
            }
        })
        binding.recyclerProductList.adapter = productListAdapter
    }



    private fun getProductsList(){
        if(CommonUtils.isConnectingToInternet(requireActivity()) == true){
            CommonUtils.showLoader(requireActivity())
            FirebaseProductsDB().getProductsList(requireActivity(), object :
                FirebaseProductsDB.GetProductsListFromDbCallback{
                override fun getProductsListDbSuccess(productsModelList: ArrayList<ProductsModel>) {
                    productsList.clear()
                    productsList.addAll(productsModelList)
                    productListAdapter.notifyDataSetChanged()
                    if(productsModelList.size>0){
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

    private fun noProductsData(isVisi:Boolean){
        if(isVisi){
            binding.tvNoData.visibility = View.VISIBLE
            binding.recyclerProductList.visibility = View.GONE
        }else{
            binding.tvNoData.visibility = View.GONE
            binding.recyclerProductList.visibility = View.VISIBLE
        }
    }
}