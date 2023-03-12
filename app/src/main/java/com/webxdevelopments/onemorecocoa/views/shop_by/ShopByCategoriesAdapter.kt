package com.webxdevelopments.onemorecocoa.views.shop_by

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webxdevelopments.onemorecocoa.R
import com.webxdevelopments.onemorecocoa.databinding.ItemProductListBinding
import com.google.android.material.card.MaterialCardView

/**
 * Created on 27/02/23.
 */
class ShopByCategoriesAdapter(var context: Context,
                              var list: ArrayList<String>,
                              var productListCallback:ProductListCallback): RecyclerView.Adapter<ShopByCategoriesAdapter.ViewHolder>() {
    val TAG: String = ShopByCategoriesAdapter::class.java.simpleName

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
        holder.binding.cardItemProduct.setOnClickListener {
            productListCallback.onItemClick(position, holder.binding.cardItemProduct)
        }
    }


    override fun getItemCount(): Int {
        return 20
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