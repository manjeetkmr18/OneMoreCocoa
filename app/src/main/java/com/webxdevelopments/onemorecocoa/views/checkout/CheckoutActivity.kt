package com.webxdevelopments.onemorecocoa.views.checkout

import android.content.Intent
import android.text.TextUtils
import android.util.Log
import com.paypal.android.sdk.payments.*
import com.webxdevelopments.onemorecocoa.R
import com.webxdevelopments.onemorecocoa.common_utils.BaseBindingActivity
import com.webxdevelopments.onemorecocoa.common_utils.CommonUtils
import com.webxdevelopments.onemorecocoa.databinding.ActivityCheckoutBinding
import com.webxdevelopments.onemorecocoa.views.dashboard.DashboardActivity
import org.json.JSONException
import java.math.BigDecimal

class CheckoutActivity : BaseBindingActivity<ActivityCheckoutBinding>() {
    private var TAG = CheckoutActivity::class.java.simpleName.toString()
    val clientKey =
        "AYiFwF-5zXtr4NBzozgYcxeCCHXv5fNFvgbojcE5VlOUmXWvKG-Po6tQ4L1I6Pz4vlNyBNIRBs6GYTZH"
    val PAYPAL_REQUEST_CODE = 123

    // Paypal Configuration Object
    private val config = PayPalConfiguration() // Start with mock environment.  When ready,
        // switch to sandbox (ENVIRONMENT_SANDBOX)
        // or live (ENVIRONMENT_PRODUCTION)
        .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // on below line we are passing a client id.
        .clientId(clientKey)

    override fun getLayoutID(): Int {
        return R.layout.activity_checkout
    }

    override fun initUI() {
        CommonUtils.showTopBack(binding.includeLayoutBack, true)
        CommonUtils.showTopTitle(binding.includeLayoutBack, true, "Checkout")
        binding.includeLayoutBack.cardBack.setOnClickListener {
            finish()
        }

        binding.btnCheckOut.setOnClickListener {
            val address: String = binding.fullAddress.text.toString().trim()
            val city: String = binding.etCity.text.toString().trim()
            val country: String = binding.etCountry.text.toString().trim()
            if (TextUtils.isEmpty(address)) {
                binding.fullAddress.setError("Please enter house no")
            } else if (TextUtils.isEmpty(city)) {
                binding.etCity.setError("Please enter city name")
            } else if (TextUtils.isEmpty(country)) {
                binding.etCountry.setError("Please enter country name")

            }else{
                getPayment()
            }
        }
    }

    private fun getPayment() {

        // Getting the amount from editText
        val amount: String = "40000"

        // Creating a paypal payment on below line.
        val payment = PayPalPayment(
            BigDecimal(amount), "USD", "Total Amount",
            PayPalPayment.PAYMENT_INTENT_SALE
        )

        // Creating Paypal Payment activity intent
        val intent = Intent(this, PaymentActivity::class.java)

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)

        // Putting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)

        // Starting the intent activity for result
        // the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == RESULT_OK) {
                //Getting the payment confirmation
                val confirm =
                    data?.getParcelableExtra<PaymentConfirmation>(PaymentActivity.EXTRA_RESULT_CONFIRMATION)

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        val paymentDetails = confirm.toJSONObject().toString(4)
                        Log.i("paymentExample", paymentDetails)

                        //Starting a new activity for the payment details and also putting the payment details with intent
                        /*           startActivity(
                                       Intent(this, ConfirmationActivity::class.java)
                                           .putExtra("PaymentDetails", paymentDetails)
                                           .putExtra("PaymentAmount", paymentAmount)
                                   )*/

                        val intent =
                            Intent(this, DashboardActivity::class.java)


                        startActivity(intent)
                    } catch (e: JSONException) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e)
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.")
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                    "paymentExample",
                    "An invalid Payment or PayPalConfiguration was submitted. Please see the docs."
                )
            }
        }
    }
}