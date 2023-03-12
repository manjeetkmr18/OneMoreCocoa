package com.webxdevelopments.onemorecocoa.views.offers

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webxdevelopments.onemorecocoa.R
import com.webxdevelopments.onemorecocoa.databinding.ItemOffersListBinding

/**
 * Created on 27/02/23.
 */
class OfferListAdapter(var context: Context,
                       var list: ArrayList<String>,
                       var comingFrom:String,
                       var productListCallback:OfferListCallback): RecyclerView.Adapter<OfferListAdapter.ViewHolder>() {
    val TAG: String = OfferListAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemOffersListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_offers_list,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.itemView.setOnClickListener {
            productListCallback.onItemClick(position)
        }
    }


    override fun getItemCount(): Int {
        if(comingFrom == "HomeFragment"){
            return 4
        }else{
            return 20
        }
    }

    class ViewHolder(binding: ItemOffersListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemOffersListBinding

        init {
            this.binding = binding
        }

    }

    interface OfferListCallback{
        fun onItemClick(position: Int)
    }
}