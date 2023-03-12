package com.webxdevelopments.onemorecocoa.views.products

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webxdevelopments.onemorecocoa.R
import com.webxdevelopments.onemorecocoa.databinding.ItemProductListBinding
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.webxdevelopments.onemorecocoa.common_utils.CommonUtils
import com.webxdevelopments.onemorecocoa.views.products.model.ProductsModel

/**
 * Created on 27/02/23.
 */
class ProductListAdapter(var context: Context,
                         var list: ArrayList<ProductsModel>,
                         var comingFrom:String,
                         var productListCallback:ProductListCallback): RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {
    val TAG: String = ProductListAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemProductListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_product_list,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val currentItem = list[position]
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
    }


    override fun getItemCount(): Int {
        if(comingFrom == "HomeFragment"){
            if(list.size>4) {
                return 4
            }else{
                return list.size
            }
        }else{
            return list.size
        }
    }

    class ViewHolder(binding: ItemProductListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemProductListBinding

        init {
            this.binding = binding
        }

    }

    interface ProductListCallback{
        fun onItemClick(position: Int, cardView:MaterialCardView)
    }
}