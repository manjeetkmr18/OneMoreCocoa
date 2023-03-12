package com.webxdevelopments.onemorecocoa.views.shop_by

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.webxdevelopments.onemorecocoa.R

/**
 * Created on 11/03/23.
 */
class ShopByCategorySpinnerAdapter(val context:Context, var arrayData:Array<String>) : BaseAdapter() {
    private val TAG: String = ShopByCategorySpinnerAdapter::class.java.simpleName
    var inflter: LayoutInflater? = LayoutInflater.from(context)

    override fun getCount(): Int {
        return arrayData!!.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = inflter!!.inflate(R.layout.item_shop_by_category_spinner, null)
        // MaterialCardView cardPropertyType = view.findViewById(R.id.cardPropertyType);
        val tvCategoryName = view.findViewById<TextView>(R.id.tv_category_name)
        val currentItem = arrayData!![position]
        // It is used the name to the TextView when the
        // current item is not null.
        tvCategoryName.text = currentItem
        return view
    }

}