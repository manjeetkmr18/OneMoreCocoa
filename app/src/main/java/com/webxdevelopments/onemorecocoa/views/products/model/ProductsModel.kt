package com.webxdevelopments.onemorecocoa.views.products.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ProductsModel(
	var flavour: String? = "",
	var price: String? = "",
	var name: String? = "",
	var description: String? = "",
	var photo: String? = "",
	var category: String? = ""
) : Parcelable
