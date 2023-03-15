package com.webxdevelopments.onemorecocoa.views.firebase_utils

import android.app.Activity
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.webxdevelopments.onemorecocoa.views.carts.model.CartFullListModel
import com.webxdevelopments.onemorecocoa.views.carts.model.CartListModel
import com.webxdevelopments.onemorecocoa.views.products.model.ProductsModel

/**
 * Created on 13/03/23.
 */
class FirebaseCartDB {
    private var TAG = FirebaseCartDB::class.java.simpleName

    public fun addDataToCart(activity: Activity, userId:String, cartModel: CartListModel, addProductToCartCallback:AddProductToCartCallback){
        Firebase.database.reference.child(FirebaseConstants.DB_CARTS).child(userId).child(cartModel.cartProductId)
            .setValue(cartModel).addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.e(TAG, "signInWithEmail:success")
                    addProductToCartCallback.addProductToCartSuccess(cartModel)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e(TAG, "signInWithEmail:failure=="+ task.exception!!.message)
                    addProductToCartCallback.addProductToCartFailure(task.exception!!.message.toString())
                }
            }
    }

    public interface AddProductToCartCallback{
        public fun addProductToCartSuccess(cartModel: CartListModel)
        public fun addProductToCartFailure(msg:String)
    }


    public fun getCartList(activity: Activity, userId:String, getCartListCallback:GetCartListCallback){
        Firebase.database.reference.child(FirebaseConstants.DB_CARTS).child(userId).addValueEventListener(
            object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val cartList = ArrayList<CartListModel>()
                        val iterator: MutableIterator<DataSnapshot> = snapshot.children.iterator()
                        while (iterator.hasNext()) {
                            val dsp = iterator.next()
                            val cartId: String = dsp.value.toString()!!
                            val cartModel: CartListModel = dsp.getValue(CartListModel::class.java)!!
                            cartList.add(cartModel)
                            if (!iterator.hasNext()) {
                                //last DataSnapshot
                                getCartListCallback.getCartListSuccess(cartList)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    getCartListCallback.getCartListFailure( error.message)
                }
            })
    }

    public interface GetCartListCallback{
        public fun getCartListSuccess(cartList: ArrayList<CartListModel>)
        public fun getCartListFailure(msg:String)
    }

    public fun getProductsFromCart(activity: Activity, userId:String, getProductsFromCartCallback:GetProductsFromCartCallback){
        Firebase.database.reference.child(FirebaseConstants.DB_CARTS).child(userId).addValueEventListener(
            object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        val productsModelList = ArrayList<CartFullListModel>()
                        val iterator: MutableIterator<DataSnapshot> = snapshot.children.iterator()
                        while (iterator.hasNext()) {
                            val dsp = iterator.next()
                            val cartModel: CartListModel = dsp.getValue(CartListModel::class.java)!!
                            Log.e(TAG,"getProductsFromCartSuccess---1---productsModelList---2---cartModel000===${Gson().toJson(cartModel)}")
                            Firebase.database.reference.child(FirebaseConstants.DB_PRODUCTS).addValueEventListener(object: ValueEventListener {
                                override fun onDataChange(snapshotInner: DataSnapshot) {
                                    if(snapshotInner.exists()){
                                        for (postSnapshot in snapshotInner.children) {
                                            val productsModel: ProductsModel = postSnapshot.getValue(ProductsModel::class.java)!!
                                            if(productsModel.product_id == cartModel.cartProductId) {
                                                val cartFullListModel = CartFullListModel(cartModel, productsModel)
                                                productsModelList.add(cartFullListModel)
                                                Log.e(TAG,"getProductsFromCartSuccess---1---productsModel---2---${Gson().toJson(productsModel)}")

                                            }
                                        }
                                        if (!iterator.hasNext()) {
                                            //last DataSnapshot
                                            Log.e(TAG,"getProductsFromCartSuccess---1---productsModelList---${Gson().toJson(productsModelList)}")
                                            getProductsFromCartCallback.getProductsFromCartSuccess(productsModelList)
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }

                            })
                        }
                    }
                    else{
                        Log.e(TAG,"getProductsFromCartSuccess---1---getProductsFromCartFailure")
                        getProductsFromCartCallback.getProductsFromCartFailure("Cart is empty!!")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG,"getProductsFromCartSuccess---2---getProductsFromCartFailure")
                    getProductsFromCartCallback.getProductsFromCartFailure(error.message.toString())
                }

            })

    }

    public interface GetProductsFromCartCallback{
        public fun getProductsFromCartSuccess(productsList: ArrayList<CartFullListModel>)
        public fun getProductsFromCartFailure(msg:String)
    }

    //appleSnapshot.getRef().removeValue();
    public fun removeProductFromCart(activity: Activity, userId: String, cartId:String, removeProductsFromCartCallback:RemoveProductsFromCartCallback){
        Firebase.database.reference.child(FirebaseConstants.DB_CARTS).child(userId).child(cartId).addValueEventListener(
            object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        Firebase.database.reference.child(FirebaseConstants.DB_CARTS).child(userId).child(cartId).removeValue()
                    }else{
                        removeProductsFromCartCallback.onRemoveFromCartSuccess(false, "Cart product not found!!")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    removeProductsFromCartCallback.onRemoveFromCartSuccess(false, error.message)

                }
            })
    }


    public interface RemoveProductsFromCartCallback{
        public fun onRemoveFromCartSuccess(isSuccess:Boolean, msg:String)
    }


}