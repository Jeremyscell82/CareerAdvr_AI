package com.lloydsbyte.careeradvr_ai.utilz

import android.content.Context
import com.lloydsbyte.careeradvr_ai.analytics.Analytix
import com.lloydsbyte.core.utilz.StoredPref
import com.lloydsbyte.core.utilz.UtilzDateHelper

/**
 * This class
 */
class GptTokenController {

    private var adsShown: Int = 0
    private val costPerAd: Int = 400
    private val dailyLimit: Int = 8000
    private val subscribedDailyLimit: Int = 15000

    fun adsShown(): Int {
        return adsShown
    }

    fun shouldShowAds(convoCost: Int): Boolean {
        val adsShownSoFar = convoCost / costPerAd
        val showAd = adsShown == adsShownSoFar
        if (showAd) adsShown += 1
        return showAd
    }
    
    fun hasReachedMax(context: Context, convoCost: Int, isSubscribed: Boolean): Boolean {
        //Get stored tokens
        val tokensUsedToday = StoredPref(context).getTokenCount()
        val limit: Int = if (isSubscribed) subscribedDailyLimit else dailyLimit
        val totalTokens = convoCost+tokensUsedToday
        return totalTokens > limit
    }

    //Token controller to prevent token abuse
    fun addToTotalCount(context: Context, tokenCount: Int) {
        val usedTokens = StoredPref(context).getTokenCount()
        val newTotal = usedTokens+ tokenCount
        StoredPref(context).updateTokenCount(newTotal)
    }

    //Used at the start of the launch to ensure the user does not go over the count
    fun shouldResetTokens(context: Context): Boolean {
        val storedPref = StoredPref(context)
        val currentDate = UtilzDateHelper(UtilzDateHelper.DF_CAL).buildStrDate()
        val lastUsedDate = storedPref.getTokenLastUsedDate()
         val reset = currentDate != lastUsedDate
        if (reset){
            Analytix().reportTokenCount(context, storedPref.getTokenCount())
            storedPref.updateTokenLastUsedDate(UtilzDateHelper(UtilzDateHelper.DF_CAL).buildStrDate())
            storedPref.updateTokenCount(0)
        }
        return reset
    }

}