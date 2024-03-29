package com.lloydsbyte.careeradvr_ai.purchase_example

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.ProductDetails
import com.lloydsbyte.careeradvr_ai.databinding.ItemPurchaseBinding

class PurchaseAdapter: RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder>() {

    var adapteritems: List<ProductDetails> = emptyList()
    var itemClicked: ((ProductDetails)-> Unit)? = null
    fun initAdapter(items: List<ProductDetails>) {
        adapteritems = items
        notifyDataSetChanged()
    }

    inner class PurchaseViewHolder(val view: ItemPurchaseBinding): RecyclerView.ViewHolder(view.root) {
        fun bind(productDetails: ProductDetails) {
            view.apply {
                itemTextview.text = productDetails.title
                itemCardview.setOnClickListener {
                    itemClicked?.invoke(productDetails)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseAdapter.PurchaseViewHolder {
        return PurchaseViewHolder(ItemPurchaseBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int = adapteritems.size

    override fun onBindViewHolder(holder: PurchaseViewHolder, position: Int) {
        holder.bind(adapteritems[position])
    }
}