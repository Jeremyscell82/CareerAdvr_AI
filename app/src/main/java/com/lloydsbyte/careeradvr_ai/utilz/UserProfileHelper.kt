package com.lloydsbyte.careeradvr_ai.utilz

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.lloydsbyte.careeradvr_ai.R
import com.lloydsbyte.core.utilz.StoredPref
import com.lloydsbyte.core.utilz.UtilzDateHelper

//To be used with Firebase Auth and Firebase Database
class UserProfileHelper {
    companion object {

        val statusFree = "FREE"
        val statusSub = "SUB"
        val statusLife = "LIFE"
        val statusDebug = "DEBUG"

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

        fun isUserSubscribed(context: Context): Boolean {
            val membershipStatus = StoredPref(context).getMembershipStatus()
            return membershipStatus == statusSub
        }

        fun getUserMembershipName(context: Context): String {
            val membershipCode: String = StoredPref(context).getMembershipStatus()
            val options = context.resources.getStringArray(R.array.settings_profile_memberships)
            return when(membershipCode) {
                statusDebug -> options[2]
                statusSub -> options[1]
                else -> options[0]
            }

        }

        fun canAskQuestion(context: Context): Boolean {
            val date = StoredPref(context).getUsageDate()
            val limit = StoredPref(context).getUsageLimit()
            var status = StoredPref(context).getMembershipStatus()
            var helper = UtilzDateHelper(UtilzDateHelper.DF_CAL)
            return when(status){
                statusFree -> {
                    !helper.compareTodayDate(helper.convertDateFromMillis(date))
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
            var status = StoredPref(context).getMembershipStatus()
            return when(status){
                statusFree -> {
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
            var status = StoredPref(context).getMembershipStatus()
            return when(status){
                statusFree -> {
                    180000
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