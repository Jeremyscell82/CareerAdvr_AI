package com.lloydsbyte.careeradvr_ai.purchase_example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.billingclient.api.ProductDetails
import com.lloydsbyte.careeradvr_ai.MainActivity
import com.lloydsbyte.careeradvr_ai.R
import com.lloydsbyte.careeradvr_ai.databinding.FragmentPurchaseBinding
import com.lloydsbyte.careeradvr_ai.utilz.GptTokenController
import com.lloydsbyte.careeradvr_ai.utilz.Gpt_Helper
import com.lloydsbyte.careeradvr_ai.utilz.IAP_Helper

class PurchaseFragment: Fragment() {

    lateinit var binding: FragmentPurchaseBinding
    lateinit var purchaseAdapter: PurchaseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPurchaseBinding.inflate(inflater, container, false)
        purchaseAdapter = PurchaseAdapter()
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            // Handle the back button event
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            purchaseCloseFab.setOnClickListener {
                findNavController().popBackStack()
            }

            val subscriptionDetail = getSubscriptionDetails()

            purchaseSubscribeFab.setOnClickListener {
                if(subscriptionDetail != null)(requireActivity() as MainActivity).purchase(subscriptionDetail)
            }


            if (subscriptionDetail != null) {
                purchaseSubscribeFab.visibility = View.VISIBLE
            } else {
                Toast.makeText(requireActivity(), resources.getString(R.string.error_gone_wrong), Toast.LENGTH_LONG).show()
                purchaseSubscribeFab.visibility = View.GONE
            }

        }
    }

    private fun getSubscriptionDetails(): ProductDetails? {
        val purchaseOptions = (requireActivity() as MainActivity).productDetails
        val subscription = purchaseOptions.find { it.productId == IAP_Helper.sku_ad_free }
        return subscription
    }
}