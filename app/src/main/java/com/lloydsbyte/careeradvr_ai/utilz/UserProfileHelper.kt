package com.lloydsbyte.careeradvr_ai.utilz

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.lloydsbyte.core.utilz.StoredPref
import com.lloydsbyte.core.utilz.UtilzDateHelper
import timber.log.Timber

//To be used with Firebase Auth and Firebase Database
class UserProfileHelper {
    companion object {

        val statusFree = "FREE"
        val statusPaid = "PAID"
        val statusSub = "SUB"
        val statusLife = "LIFE"

        val endpointStatus = "status"
        val endpointDateLimit = "dateLimit"
        val endpointUsageLimit = "usageLimit"

        fun createProfileReference(endpoint: String): DatabaseReference {
            return Firebase.database.getReference("users/${FirebaseAuth.getInstance().currentUser?.uid}/$endpoint")
        }

        fun updateUserStatus(status: String) {
            createProfileReference(endpointStatus).setValue(status)
        }

        fun updateDateLimit(date: Long) {
            createProfileReference(endpointDateLimit).setValue(date)
        }

        fun updateUsageLimit(usage: Int) {
            createProfileReference(endpointUsageLimit).setValue(usage)
        }

        fun getUserStatus(context: Context): String {
            return StoredPref(context).getProfileStatus()
        }

        fun canAskQuestion(context: Context): Boolean {
            val date = StoredPref(context).getUsageDate()
            val limit = StoredPref(context).getUsageLimit()
            var status = StoredPref(context).getProfileStatus()
            var helper = UtilzDateHelper(UtilzDateHelper.DF_CAL)
            Timber.d("JL_ data from stored pref: $date, $limit, $status")
            return when(status){
                statusFree -> {
                    !helper.compareTodayDate(helper.convertDateFromMillis(date))
                }
                statusPaid -> {
                    limit < 2 //|| !helper.compareTodayDate(helper.convertDateFromMillis(date))
                }
                statusSub -> {
                    limit < 3 //|| !helper.compareTodayDate(helper.convertDateFromMillis(date))
                }
                statusLife -> {
                    true
                }
                else -> { false }
            }
        }

        fun areWeRestricted(context: Context): Boolean {
            var status = StoredPref(context).getProfileStatus()
            return when(status){
                statusFree -> {
                    true
                }
                statusPaid -> {
                    true
                }
                statusSub -> {
                    true
                }
                statusLife -> {
                    false
                }
                else -> { false }
            }
        }

        fun getAdTimer(context: Context) : Long {
            //The shorter this value is the more ads the user will see
            var status = StoredPref(context).getProfileStatus()
            return when(status){
                statusFree -> {
                    180000
                }
                statusPaid -> {
                    300000
                }
                statusSub -> {
                    0
                }
                statusLife -> {
                    0
                }
                else -> { 0 }
            }
        }

    }


}