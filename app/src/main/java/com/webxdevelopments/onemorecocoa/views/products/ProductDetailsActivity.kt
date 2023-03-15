package com.webxdevelopments.onemorecocoa.views.products

import android.content.Intent
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.paypal.android.sdk.payments.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.webxdevelopments.onemorecocoa.R
import com.webxdevelopments.onemorecocoa.common_utils.BaseBindingActivity
import com.webxdevelopments.onemorecocoa.common_utils.CommonUtils
import com.webxdevelopments.onemorecocoa.common_utils.Constants
import com.webxdevelopments.onemorecocoa.databinding.ActivityProductDetailsBinding
import com.webxdevelopments.onemorecocoa.views.carts.CartListActivity
import com.webxdevelopments.onemorecocoa.views.carts.model.CartListModel
import com.webxdevelopments.onemorecocoa.views.firebase_utils.FirebaseCartDB
import com.webxdevelopments.onemorecocoa.views.products.model.ProductsModel
import org.json.JSONException
import java.math.BigDecimal


class ProductDetailsActivity : BaseBindingActivity<ActivityProductDetailsBinding>() {
    private var TAG = ProductDetailsActivity::class.java.simpleName.toString()

    private lateinit var productsModel: ProductsModel
    private var isProductAlreadyPresentInCart = false
    private var fullCartList = ArrayList<CartListModel>()
    private var cartId = ""



    override fun getLayoutID(): Int {
        return R.layout.activity_product_details
    }

    override fun initUI() {
        getIntentData()
        CommonUtils.showTopBack(binding.includeLayoutBack, true)
        CommonUtils.showTopTitle(binding.includeLayoutBack, true, "Product Details")
        binding.includeLayoutBack.cardBack.setOnClickListener {
            finish()
        }

        binding.tvProductDetailsDesc.isClickable = true
        binding.tvProductDetailsDesc.movementMethod = LinkMovementMethod.getInstance()
        binding.tvProductDetailsDesc.text = Html.fromHtml(resources.getString(R.string.product_details_desc))


        binding.tvQuantityMinus.setOnClickListener {
            var count = binding.tvQuantityCount.text.toString().toInt()
            if(count !=1){
                count--
            }
            binding.tvQuantityCount.text = count.toString()
        }
        binding.tvQuantityAdd.setOnClickListener {
            var count = binding.tvQuantityCount.text.toString().toInt()
            count++
            binding.tvQuantityCount.text = count.toString()
        }

        binding.btnAddToCart.setOnClickListener {

            if (CommonUtils.isConnectingToInternet(this) == true){
                CommonUtils.showLoader(this)
                val quantity = binding.tvQuantityCount.text.toString().toInt()
                val userId = Firebase.auth.currentUser!!.uid
                val cartModel = CartListModel(productsModel.product_id.toString(), quantity)
                FirebaseCartDB().addDataToCart(
                    this,
                    userId,
                    cartModel,
                    object : FirebaseCartDB.AddProductToCartCallback {
                        override fun addProductToCartSuccess(cartModel: CartListModel) {
                            CommonUtils.hideLoader()
                            Log.e(TAG, "addProductToCartSuccess---cartModel")
                            addToCart(false)
                        }

                        override fun addProductToCartFailure(msg: String) {
                            CommonUtils.hideLoader()
                            Log.e(TAG, "addProductToCartFailure---cartModel")
                        }

                    })
            }
            else{
                CommonUtils.showToast(this, resources.getString(R.string.no_internet_connection))
            }
        }

        binding.btnDeleteCart.setOnClickListener {
            if(isProductAlreadyPresentInCart) {
                if (CommonUtils.isConnectingToInternet(this@ProductDetailsActivity) == true) {
                    CommonUtils.showLoader(this)
                    val userId = Firebase.auth.currentUser!!.uid
                    FirebaseCartDB().removeProductFromCart(
                        activity = this,
                        userId = userId,
                        cartId,
                        object : FirebaseCartDB.RemoveProductsFromCartCallback {
                            override fun onRemoveFromCartSuccess(isSuccess: Boolean, msg: String) {
                                CommonUtils.hideLoader()
                                if (isSuccess) {
                                    addToCart(true)
                                } else {
                                    CommonUtils.showToast(this@ProductDetailsActivity, msg)
                                }
                            }

                        })
                }
                else{
                    CommonUtils.showToast(this, resources.getString(R.string.no_internet_connection))
                }
            }

        }

        isProductAlreadyPresentInCart = getProductAlreadyPresent()

        if(isProductAlreadyPresentInCart){
            addToCart(false)
        }else{
            addToCart(true)
        }

        binding.btnBuyNow.setOnClickListener {
            showCart()

        }
    }
    private fun showCart() {
        startActivity(Intent(this, CartListActivity::class.java))
    }
    private fun addToCart(isVisi:Boolean){
        if(isVisi){
            binding.btnAddToCart.visibility = View.VISIBLE
            binding.btnDeleteCart.visibility = View.GONE
        }
        else{
            binding.btnAddToCart.visibility = View.GONE
            binding.btnDeleteCart.visibility = View.VISIBLE
        }
    }

    private fun getIntentData() {
        val bundle = intent.extras
        if(bundle != null){
            if(bundle.containsKey(Constants.INTENT_PRODUCT_DETAIL)){
                val productsModelStr = bundle.getString(Constants.INTENT_PRODUCT_DETAIL)
                productsModel = Gson().fromJson(productsModelStr, ProductsModel::class.java)
                setProductData(productsModel)
            }else{
                setDummyData()
            }
        }else{
            setDummyData()
        }
    }

    private fun setProductData(productsModel: ProductsModel) {
        val image = productsModel.photo.toString()
        Log.e(TAG, "imageUrl---$image")
        if(CommonUtils.isNullOrEmpty(image)){
            binding.ivProductImage.setImageResource(R.drawable.image_placeholder)
        }
        else {
            Picasso.with(this)
                .load(image)
                .into(binding.ivProductImage, object : Callback {
                    override fun onSuccess() {
                        Log.e(TAG, "imageUrl---onSuccess")
                    }

                    override fun onError() {
                        Log.e(TAG, "imageUrl---onError")
                        binding.ivProductImage.setImageResource(R.drawable.image_placeholder)
                    }
                })
        }

        val title = productsModel.name.toString()
        if(CommonUtils.isNullOrEmpty(title)){
            binding.tvProductDetailsTitle.text = ""
        }else {
            binding.tvProductDetailsTitle.text = title
        }

        val price = productsModel.price.toString()
        if(CommonUtils.isNullOrEmpty(price)){
            binding.tvProductDetailsPrice.text = ""
        }else {
            binding.tvProductDetailsPrice.text = "$$price"
        }

        val desc = productsModel.description.toString()
        if(CommonUtils.isNullOrEmpty(desc)){
            binding.tvProductDetailsDesc.text = ""
        }else {
            binding.tvProductDetailsDesc.text = desc
        }
    }
    private fun setDummyData() {
        binding.ivProductImage.setImageResource(R.mipmap.img_product)
        binding.tvProductDetailsTitle.text = "70% DARK CHOCOLATE"
        binding.tvProductDetailsPrice.text = "$9.99"
        binding.tvProductDetailsDesc.text = resources.getString(R.string.product_details_desc)
    }


    private fun getProductAlreadyPresent():Boolean{
        var isProductAlreadyPresentInCart = false
        val userId = Firebase.auth.currentUser!!.uid
        FirebaseCartDB().getCartList(this, userId, object : FirebaseCartDB.GetCartListCallback{
            override fun getCartListSuccess(cartList: ArrayList<CartListModel>) {
                fullCartList.clear()
                fullCartList.addAll(cartList)
                for(i in 0 until fullCartList.size){
                    if(fullCartList[i].cartProductId == productsModel.product_id){
                        isProductAlreadyPresentInCart = true
                        cartId = fullCartList[i].cartProductId
                        break
                    }
                }
                Log.e(TAG,"getCartListSuccess--fullCartList--${Gson().toJson(fullCartList)}")
            }

            override fun getCartListFailure(msg: String) {
                Log.e(TAG,"getCartListFailure--msg--$msg")
            }

        })
        return isProductAlreadyPresentInCart
    }




}