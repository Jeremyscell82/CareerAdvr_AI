package com.lloydsbyte.careeradvr_ai.utilz

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.lloydsbyte.careeradvr_ai.R
import com.lloydsbyte.core.ErrorController
import com.lloydsbyte.core.utilz.StoredPref
import timber.log.Timber

open class IAP_Helper: AppCompatActivity() {

    private val sku_ad_free = "sku_ad_free_subscription"
    private val sku_debug_sub = "sku_debug_subscription"

    /** IN APP PURCHASE **/
    var productDetails: List<ProductDetails> = emptyList()
    var isSubAdFree: Boolean = false
    var consumableProduct: ProductDetails? = null
    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            // To be implemented in a later section.
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    ErrorController.logStatus("Purchased this: ${purchase.products.first()}")

                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        // Acknowledge the purchase if it hasn't already been acknowledged.
                        if (!purchase.isAcknowledged) {
                            val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.purchaseToken)
                                .build()
                            billingClient.acknowledgePurchase(acknowledgePurchaseParams) { ackBillingResult ->
                                if (ackBillingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                                    // Purchase acknowledged.
                                    // Grant access to the subscription features.
                                    Toast.makeText(this, "User made a purchase!!", Toast.LENGTH_LONG).show()
                                    Timber.d("JL_ purchase was made")
                                    StoredPref(this).setMembershipStatus("SUB")
                                    isSubAdFree = true
                                } else {
                                    Toast.makeText(this, resources.getString(R.string.error_gone_wrong), Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
//                    consume(purchase.purchaseToken)
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
                Toast.makeText(this, resources.getString(R.string.error_changed_mind), Toast.LENGTH_LONG).show()
            } else {
                // Handle any other error codes.
                Toast.makeText(this, resources.getString(R.string.error_gone_wrong), Toast.LENGTH_LONG).show()
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
                    // The BillingClient is ready. You can query now
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
                .setProductId(sku_ad_free)
                .setProductType(BillingClient.ProductType.SUBS)
                .build(),
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(sku_debug_sub)
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
            checkSubscriptionActive()
        }
    }


    //Check Subscription Status
    fun checkSubscriptionActive() {

        billingClient.queryPurchasesAsync(BillingClient.ProductType.SUBS) { billingResult, purchasesList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                for (purchase in purchasesList) {
                    purchase.products.map {
                        if (it == sku_ad_free) {
                            StoredPref(this).setMembershipStatus("Subscribed")
                            isSubAdFree = true
                        } else if (it == sku_debug_sub) {
                            StoredPref(this).setMembershipStatus("Debug")
                        }
                    }
                }
            } else {
                // Handle error
            }
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

    fun openSubscriptionsPage() {
        val packageName = packageName
        try {
            // Intent to open the subscription page for your app in the Google Play Store
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/account/subscriptions?package=$packageName")))
        } catch (e: ActivityNotFoundException) {
            // Fallback: Try to open the Google Play Store app at your app's main page if the subscriptions URL fails
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
            } catch (fallbackException: ActivityNotFoundException) {
                // If Google Play Store is not installed, open in web browser
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
            }
        }
    }

}