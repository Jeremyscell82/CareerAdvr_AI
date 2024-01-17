package com.lloydsbyte.core.utilz

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.play.core.ktx.BuildConfig
import com.lloydsbyte.core.R
import java.util.UUID

class UtilzSendItHelper {
    companion object {
        val contactEmail: String = "jeremylloyd@lloydsbyte.com"
        val errorString: String = "No email found, please email jeremylloyd@lloydsbyte.com"
        val appUrl: String = "https://play.google.com/store/apps/details?id=com.lloydsbyte.base_chap"
        val profileUrl: String = "https://play.google.com/store/apps/dev?id=8668340116169178413"
    }


    fun reportBug(context: Context, version: String) {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "message/rfc822"
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf(contactEmail))
        i.putExtra(Intent.EXTRA_SUBJECT, "CareerAdvr AI Reporting bug")
        i.putExtra(Intent.EXTRA_TEXT, "I am on version $version and found a bug...")
        try {
            context.startActivity(Intent.createChooser(i, "Send email..."))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                context,
                errorString,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun contactDeveloper(context: Context, version: String) {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "message/rfc822"
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf(contactEmail))
        i.putExtra(Intent.EXTRA_SUBJECT, "Contact Request - CareerAdvr AI :$version")
        try {
            context.startActivity(Intent.createChooser(i, "Send email..."))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                context,
                errorString,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun contactDeveloperWithUID(context: Context, version: String, UID: String) {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "message/rfc822"
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf(contactEmail))
        i.putExtra(Intent.EXTRA_SUBJECT, "Support Request with UID\nCareerAdvr AI :$version\nUUID:${UID}")
        try {
            context.startActivity(Intent.createChooser(i, "Send email..."))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                context,
                errorString,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    //To figure out which one needs this
    fun sendChat(context: Context, body: String) {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "message/rfc822"
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf(""))
        i.putExtra(Intent.EXTRA_SUBJECT, "Alyssa told me...")
        i.putExtra(Intent.EXTRA_TEXT, body)
        try {
            context.startActivity(Intent.createChooser(i, "Send email..."))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                context,
                errorString,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun shareApp(context: Context) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, appUrl)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    fun shareUrl(context: Context, url: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }
}