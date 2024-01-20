package com.lloydsbyte.careeradvr_ai.utilz

import android.content.Context
import com.lloydsbyte.core.utilz.StoredPref

/**
 * This class
 */
class GptTokenController {

    private var showDoubleAds: Boolean = true
    private var adsShown: Int = 0
    private val costPerAd: Int = 400
    private val maxCostLimit: Int = 10000

    fun adsShown(): Int {
        return adsShown
    }

    fun shouldShowAds(convoCost: Int): Boolean {
        val adsShownSoFar = convoCost / costPerAd
        val showAd = adsShown == adsShownSoFar
        if (showAd) adsShown += 1
        return showAd
    }
    
    fun hasReachedMax(convoCost: Int): Boolean {
        return convoCost > maxCostLimit
    }
    
    fun showDoubleAd(): Boolean {
        return showDoubleAds
    }

    //Token controller to prevent token abuse
    fun addToTotalCount(context: Context, tokenCount: Int) {
        val usedTokens = StoredPref(context).getTokenCount()
        val newTotal = usedTokens+ tokenCount
        StoredPref(context).updateTokenCount(newTotal)
    }


}