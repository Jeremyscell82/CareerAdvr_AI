package com.lloydsbyte.careeradvr_ai.purchase_example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lloydsbyte.careeradvr_ai.MainActivity
import com.lloydsbyte.careeradvr_ai.databinding.FragmentPurchaseBinding

class PurchaseFragment: Fragment() {

    lateinit var binding: FragmentPurchaseBinding
    var tokenCount = 0
    lateinit var purchaseAdapter: PurchaseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPurchaseBinding.inflate(inflater, container, false)
        purchaseAdapter = PurchaseAdapter()
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
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
            purchaseRecyclerview.apply {
                adapter = purchaseAdapter
                purchaseAdapter.initAdapter((requireActivity() as MainActivity).productDetails)
                purchaseAdapter.itemClicked = {
                    (requireActivity() as MainActivity).purchase(it)
                }
            }

        }
    }

}