package com.lloydsbyte.core.utilz

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.core.content.pm.PackageInfoCompat
import com.afollestad.materialdialogs.BuildConfig
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lloydsbyte.core.ErrorController
import com.lloydsbyte.network.ConfigModel
import java.lang.reflect.Type
import kotlin.random.Random


class Utilz {
    companion object {

        /** CONFIG FILE HELPER FUNCTIONS **/
        fun convertConfigToString(configFile: ConfigModel.BaseStructure): String {
            return Gson().toJson(configFile)
        }

        fun convertConfigToModel(configInStr: String): ConfigModel.BaseStructure {
//            val jsonFile = Gson().toJson(configInStr)
            val type: Type = object : TypeToken<ConfigModel.BaseStructure?>() {}.type
            return Gson().fromJson(configInStr, type)
        }

        fun isInDebugMode(context: Context): Boolean {
            return 0 != context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
        }

        fun getBuildNumber(context: Context): Int {
            var version: Long = 0L
            try {
                version = PackageInfoCompat.getLongVersionCode(context.packageManager.getPackageInfo(context.packageName, 0))
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return version.toInt()
        }

        fun getVersionName(context: Context): String {
            var versionName: String = ""
            try {
                versionName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return versionName
        }

        //Get random 4 digit number
        fun getRandomDigits(): String {
            return String.format("%04d", Random.nextInt(10000))
        }

        fun openInBrowser(context: Context, url: String) {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(url)
            context.startActivity(openURL)
        }


        fun rateApp(activity: Activity) {
            val manager =
                if (BuildConfig.DEBUG) FakeReviewManager(activity) else ReviewManagerFactory.create(
                    activity
                )
            val request = manager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // We got the ReviewInfo object
                    val reviewInfo = task.result
                    val flow = manager.launchReviewFlow(activity, reviewInfo)
                    flow.addOnCompleteListener { _ ->
                        // The flow has finished. The API does not indicate whether the user
                        // reviewed or not, or even whether the review dialog was shown. Thus, no
                        // matter the result, we continue our app flow and thank the user :) .
                        Toast.makeText(
                            activity,
                            "Thank you for your review",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    // There was some problem, log or handle the error code.
                    ErrorController.logError("Was not able to rate app")
                }
            }
        }

        fun copyToClipboard(context: Context, copy: String) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // Creates a new text clip to put on the clipboard.
            val clip: ClipData = ClipData.newPlainText("simple text", copy)
            // Set the clipboard's primary clip.
            clipboard.setPrimaryClip(clip)
        }

        //End of Companion Object
    }
}