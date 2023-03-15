package com.webxdevelopments.onemorecocoa.views.home

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.webxdevelopments.onemorecocoa.R
import com.webxdevelopments.onemorecocoa.common_utils.BaseBindingFragment
import com.webxdevelopments.onemorecocoa.databinding.FragmentHomeBinding
import com.webxdevelopments.onemorecocoa.views.dashboard.DashboardActivity
import com.webxdevelopments.onemorecocoa.views.products.ProductDetailsActivity
import com.webxdevelopments.onemorecocoa.views.products.ProductListAdapter
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.webxdevelopments.onemorecocoa.common_utils.CommonUtils
import com.webxdevelopments.onemorecocoa.common_utils.Constants
import com.webxdevelopments.onemorecocoa.views.firebase_utils.FirebaseConstants
import com.webxdevelopments.onemorecocoa.views.firebase_utils.FirebaseProductsDB
import com.webxdevelopments.onemorecocoa.views.products.model.ProductsModel


class HomeFragment(val homeScreenClickCallback: DashboardActivity.HomeScreenClickCallback) : BaseBindingFragment<FragmentHomeBinding>() {

    private var TAG = HomeFragment::class.java.simpleName.toString()
    private var productsList = ArrayList<ProductsModel>()
    private lateinit var productListAdapter:ProductListAdapter
    private lateinit var imageList: List<Int>
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun getLayoutID(): Int {
        return R.layout.fragment_home
    }
    override fun initUI() {
        imageSlider()
        setHomeProductListAdapter()
        getProductsList()
    }
    private fun imageSlider() {
        imageList = ArrayList<Int>()
        imageList = imageList + R.drawable.coco1
        imageList = imageList + R.drawable.coco2
        imageList = imageList + R.drawable.coco3
        viewPagerAdapter = ViewPagerAdapter(requireActivity(), imageList)
        binding.idViewPager.adapter = viewPagerAdapter
    }

    private fun setHomeProductListAdapter() {
        binding.recyclerHomeProductList.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        productListAdapter = ProductListAdapter(requireActivity(), productsList,"HomeFragment", object :
            ProductListAdapter.ProductListCallback{
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
        binding.recyclerHomeProductList.adapter = productListAdapter

        binding.viewMoreText.setOnClickListener {
            homeScreenClickCallback.onViewMoreClick()
        }
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
                    CommonUtils.hideLoader()
                    if(productsModelList.size>0){
                        noProductsData(false)
                    }else{
                        noProductsData(true)
                    }
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
            binding.llHomeProducts.visibility = View.GONE
            binding.tvNoProductData.visibility = View.VISIBLE
        }else{
            binding.llHomeProducts.visibility = View.VISIBLE
            binding.tvNoProductData.visibility = View.GONE
        }
    }

}