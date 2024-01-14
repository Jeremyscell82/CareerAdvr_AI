package com.lloydsbyte.core.permissions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog

/**
 * Permission Check
 * Ensures user has accepted the permission in question easier than implemented by the creators
 */
open class PermissionCheck {
    companion object {
        val PERMISSION_CALLBACK_CODE = 1001


        //Check if location permission has been granted
        fun isLocationGranted(context: Context): Boolean {
            return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        }

        //Check if camera permission has been granted
        fun isCameraGranted(context: Context): Boolean {
            return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        }


        fun requestPermission(activity: Activity, permission: String, permissionDenied: Boolean, deniedTitle: String = "Permission required", deniedMessage: String = "This Permission is required to use this feature.") {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    permission
                )
            ) {
                //If we are able to request permission, request it
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(permission),
                    PERMISSION_CALLBACK_CODE
                )
            } else {
                //We may be blocked from requesting permission....
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(permission),
                    PERMISSION_CALLBACK_CODE
                )
                //If the user has denied it already, display a dialog stating to enable it from the settings
                if (permissionDenied) showSendToSettingsDialog(activity, deniedTitle, deniedMessage)
            }
        }

        //Easily send the user to the settings of your app to enable permissions
        fun showSendToSettingsDialog(activity: Activity, title: String, message: String) {
            MaterialDialog(activity).show {
                title(text = title)
                message(text = message)
                positiveButton(text = "OK") { dialog ->
                    sendToAppSettings(activity)
                    dialog.dismiss()
                }
            }
        }
        private fun sendToAppSettings(activity: Activity){
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", activity.packageName, null)
            intent.data = uri
            activity.startActivityForResult(intent, PERMISSION_CALLBACK_CODE)
        }
    }
}