package com.webxdevelopments.onemorecocoa.views.carts.model

import com.webxdevelopments.onemorecocoa.views.products.model.ProductsModel

/**
 * Created on 13/03/23.
 */
data class CartFullListModel(
    val cartModel:CartListModel = CartListModel(),
    val productModel:ProductsModel = ProductsModel(),
)