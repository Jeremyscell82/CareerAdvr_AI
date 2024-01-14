package com.lloydsbyte.careeradvr_ai.utilz

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.firebase.ui.auth.AuthUI
import com.lloydsbyte.core.customdialog.CustomDialogs

/**
 * This class holds all the functions that operate the tokens
 */
class GAccountHelper {
    companion object {

        /** ========== Sign In Functions ========== **/
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        fun createSignInIntent(signInLauncher: ActivityResultLauncher<Intent>) {
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
            signInLauncher.launch(signInIntent)
        }

        fun signOutUser(context: Context, runnable: Runnable) {
            AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener {
                    // ...User has signed out
                    Toast.makeText(context, "Signed out successfully", Toast.LENGTH_SHORT).show()
                    Handler().postDelayed(runnable, 800)
                }
        }

        fun deleteMyAccount(view: View, context: Context, runnable: Runnable) {
            AuthUI.getInstance()
                .delete(context)
                .addOnCompleteListener {
                     //Show toast that account was deleted
                    CustomDialogs.snackbar(view, "Account was deleted")
                    signOutUser(context, runnable)
                }
        }

    }
}