package com.lloydsbyte.careeradvr_ai.utilz

import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.lloydsbyte.core.ErrorController

open class IAP_Helper: AppCompatActivity() {


    /** IN APP PURCHASE **/
    var productDetails: List<ProductDetails> = emptyList()
    var consumableProduct: ProductDetails? = null
    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            // To be implemented in a later section.
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    ErrorController.logStatus("Purchased this: ${purchase.products.first()}")
                    consume(purchase.purchaseToken)
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.

            } else {
                // Handle any other error codes.

                ErrorController.logStatus("Error this: ${purchases?.first()?.products?.first()}")
            }
        }

    val billingClient: BillingClient by lazy {
        BillingClient.newBuilder(this)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
    }

    fun initializeBilling() {
        //This must be called prior to loading the SKU's as their may be a few second delay
        //Launch in onCreate of the MainActivity to ensure IAP is ready when you need it
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    queryForSkus()
                }
            }
            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    private fun queryForSkus() {
        val productList = ArrayList<QueryProductDetailsParams.Product>()
        productList.addAll(listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("NA")
                .setProductType(BillingClient.ProductType.INAPP)
                .build(),
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("NA")
                .setProductType(BillingClient.ProductType.INAPP)
                .build(),
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("NA")
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        ))
        val params = QueryProductDetailsParams.newBuilder()
        params.setProductList(productList)

        billingClient.queryProductDetailsAsync(params.build()) {
                billingResult,
                productDetailsList ->
            // check billingResult
            // process returned productDetailsList
            productDetails = productDetailsList.toList()
            ErrorController.logStatus("Results were: ${productDetailsList.size} and first item was ${productDetailsList.first().title}")
        }
    }

    fun purchase(product: ProductDetails) {
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                .setProductDetails(product)
                // to get an offer token, call ProductDetails.subscriptionOfferDetails()
                // for a list of offers that are available to the user
                .setOfferToken("")
                .build()
        )

        consumableProduct = product
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

// Launch the billing flow
        billingClient.launchBillingFlow(this, billingFlowParams)

    }

    private fun consume(purchaseToken: String) {
        val params = ConsumeParams.newBuilder()
            .setPurchaseToken(purchaseToken)
            .build()

        billingClient.consumeAsync(
            params
        ) { billingResult, token ->
            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    //Place an interface or use rx to send the result to another section of the app.
                    // Easiest method would be to use ROOM DB to save a value, this could be read live through out the app ;)
                    ErrorController.logStatus("SUCCESS it worked, and the user consumed the product")
                }
                else -> {
                    ErrorController.logStatus("BillingClient Failed to consume purchase $billingResult")
                }
            }
        }
    }


}