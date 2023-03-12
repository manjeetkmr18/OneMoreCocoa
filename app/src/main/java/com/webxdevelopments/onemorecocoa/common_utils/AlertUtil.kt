package com.webxdevelopments.onemorecocoa.common_utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color

/**
 * Created on 12/03/23.
 */
object AlertUtil {
    fun showAlert(
        context: Context,
        title: String,
        msg: String,
        isTwoButtonShow: Boolean,
        positiveBtnText: String?,
        negativeBtnText: String?,
        alertDialogBtnClick: AlertDialogBtnClick
    ): AlertDialog? {
        val builder = AlertDialog.Builder(context)
        if(isTwoButtonShow){
            builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(positiveBtnText) { dialog, id ->
                    dialog.dismiss()
                    alertDialogBtnClick.onPositiveBtnClick()
                }
                .setNegativeButton(
                    negativeBtnText
                )
                { dialog, id -> //  Action for 'NO' Button
                    dialog.dismiss()
                    alertDialogBtnClick.onNegativeBtnClick()
                }
        }else{
            builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(positiveBtnText) { dialog, id ->
                    dialog.dismiss()
                    alertDialogBtnClick.onPositiveBtnClick()
                }
        }
        //Creating dialog box
        val alert = builder.create()
        //Setting the title manually
        if (!CommonUtils.isNullOrEmpty(title)) {
            alert.setTitle(title)
        }
        alert.show()
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        return alert
    }

    public interface AlertDialogBtnClick{
        fun onPositiveBtnClick()
        fun onNegativeBtnClick()
    }
}