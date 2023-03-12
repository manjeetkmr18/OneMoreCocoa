package com.webxdevelopments.onemorecocoa.views.firebase_utils

import android.app.Activity
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.webxdevelopments.onemorecocoa.views.products.model.ProductsModel

/**
 * Created on 12/03/23.
 */
class FirebaseProductsDB {
    private var TAG = FirebaseProductsDB::class.java.simpleName.toString()

    public fun getProductsList(activity: Activity, getProductsListFromDbCallback:GetProductsListFromDbCallback){
       Firebase.database.reference.child(FirebaseConstants.DB_PRODUCTS).addValueEventListener(
           object: ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot) {
                   if(snapshot.exists()) {
                       val productsModelList = ArrayList<ProductsModel>()
                       val iterator: MutableIterator<DataSnapshot> = snapshot.children.iterator()
                       while (iterator.hasNext()) {
                           val dsp = iterator.next()
                           val productKey: String = dsp.key.toString()
                           val productsModel: ProductsModel = dsp.getValue(ProductsModel::class.java)!!
                           productsModelList.add(productsModel)
                           if (!iterator.hasNext()) {
                               //last DataSnapshot
                               getProductsListFromDbCallback.getProductsListDbSuccess(productsModelList)
                           }
                       }
                   }else{
                       getProductsListFromDbCallback.getProductsListDbFailure("Data not found!!")
                   }
               }

               override fun onCancelled(error: DatabaseError) {
                   getProductsListFromDbCallback.getProductsListDbFailure(error.message.toString())
               }

           })
    }

    public interface GetProductsListFromDbCallback{
        public fun getProductsListDbSuccess(productsModelList: ArrayList<ProductsModel>)
        public fun getProductsListDbFailure(msg:String)
    }

}