package com.webxdevelopments.onemorecocoa.views.dashboard

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webxdevelopments.onemorecocoa.R
import com.webxdevelopments.onemorecocoa.common_utils.CommonUtils
import com.webxdevelopments.onemorecocoa.databinding.ItemProductListBinding
import com.webxdevelopments.onemorecocoa.databinding.ItemSideMenuBinding
import com.webxdevelopments.onemorecocoa.views.dashboard.model.DashboardSideMenuModel
import com.google.android.material.card.MaterialCardView

/**
 * Created on 27/02/23.
 */
class DashboardSideMenuAdapter(var context: Activity,
                               var list: ArrayList<DashboardSideMenuModel>,
                               var dashboardSideMenuCallback:DashboardSideMenuCallback): RecyclerView.Adapter<DashboardSideMenuAdapter.ViewHolder>() {
    val TAG: String = DashboardSideMenuAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSideMenuBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_side_menu,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val currentItem = list[position]
        val title = currentItem.sideMenuTitle
        val isSelected = currentItem.isSelected
        setTextViewSelected(isSelected, holder, position, currentItem)
        holder.binding.tvItemSideMenuText.text  = title

        holder.itemView.setOnClickListener {
            dashboardSideMenuCallback.onItemClick(position)
        }
    }
    
    private fun setTextViewSelected(isSelected:Boolean, holder: ViewHolder, position:Int, currentItem:DashboardSideMenuModel){
        if(isSelected){
            holder.binding.cardItemSideMenu.setCardBackgroundColor(context.resources.getColor(R.color.bg_color_theme_alpha))
            com.webxdevelopments.onemorecocoa.common_utils.CommonUtils.setFontToTextView(activity = context, "montserrat_alternates_bold.ttf", holder.binding.tvItemSideMenuText)

        }else{
            holder.binding.cardItemSideMenu.setCardBackgroundColor(Color.TRANSPARENT)
            com.webxdevelopments.onemorecocoa.common_utils.CommonUtils.setFontToTextView(activity = context, "montserrat_alternates_medium.ttf", holder.binding.tvItemSideMenuText)
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(binding: ItemSideMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemSideMenuBinding

        init {
            this.binding = binding
        }

    }

    interface DashboardSideMenuCallback{
        fun onItemClick(position: Int)
    }
}