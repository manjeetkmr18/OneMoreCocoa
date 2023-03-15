package com.webxdevelopments.onemorecocoa.views.carts

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.webxdevelopments.onemorecocoa.R
import com.webxdevelopments.onemorecocoa.common_utils.CommonUtils
import com.webxdevelopments.onemorecocoa.databinding.ItemCartListBinding
import com.webxdevelopments.onemorecocoa.views.carts.model.CartFullListModel

/**
 * Created on 27/02/23.
 */
class CartListAdapter(var context: Context,
                      var list: ArrayList<CartFullListModel>,
                      var productListCallback:ProductListCallback): RecyclerView.Adapter<CartListAdapter.ViewHolder>() {
    val TAG: String = CartListAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCartListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_cart_list,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val currentItem = list[position].productModel
        val cartModel = list[position].cartModel
        val quantity = cartModel.quantity

        holder.binding.tvQuantityCount.text = "$quantity"

        var image = currentItem.photo.toString()
        //val image = "https://firebasestorage.googleapis.com/v0/b/chocolate-app-a6ee0.appspot.com/o/GHIRARDELLI%20Caramel%20Milk%20Chocolate%20Squares.png?alt=media&token=c05aa0c3-65eb-4a8f-bdca-205f43b114ca"
        val productName = currentItem.name.toString()
        val productPrice = currentItem.price.toString()

        if(CommonUtils.isNullOrEmpty(productName)){
            holder.binding.tvProductTitle.text = ""
        }else{
            holder.binding.tvProductTitle.text = productName
        }

        if(CommonUtils.isNullOrEmpty(productPrice)){
            holder.binding.tvProductPrice.text = ""
        }else{
            holder.binding.tvProductPrice.text = "$$productPrice"
        }

        if(CommonUtils.isNullOrEmpty(image)){
            holder.binding.ivProductImg.setImageResource(R.drawable.image_placeholder)
        }
        else {
            Picasso.with(context)
                .load(image)
                .into(holder.binding.ivProductImg, object : Callback {
                    override fun onSuccess() {
                        Log.e(TAG, "imageUrl---onSuccess")
                    }

                    override fun onError() {
                        Log.e(TAG, "imageUrl---onError")
                        holder.binding.ivProductImg.setImageResource(R.drawable.image_placeholder)
                    }
                })
        }

        holder.binding.cardItemProduct.setOnClickListener {
            productListCallback.onItemClick(position, holder.binding.cardItemProduct)
        }

        holder.binding.tvQuantityAdd.setOnClickListener {
            val quantityCount = quantity + 1
            productListCallback.onCartQuantityChange(position, quantityCount)
        }

        holder.binding.tvQuantityMinus.setOnClickListener {
            if(quantity>1) {
                val quantityCount = quantity - 1
                productListCallback.onCartQuantityChange(position, quantityCount)
            }
        }

        holder.binding.cardDelete.setOnClickListener {
            productListCallback.onCartDelete(position)
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(binding: ItemCartListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemCartListBinding

        init {
            this.binding = binding
        }

    }

    interface ProductListCallback{
        fun onItemClick(position: Int, cardView:MaterialCardView)
        fun onCartQuantityChange(position: Int, count:Int)
        fun onCartDelete(position: Int)
    }
}