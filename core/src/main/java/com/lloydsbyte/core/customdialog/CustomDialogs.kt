package com.lloydsbyte.core.customdialog

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar


/**
 * This class will display a custom made full screen easily modifiable dialog, it also has some toast and snack bar functions to launch those.
 * Extremely useful for when developing an app...
 */
class CustomDialogs {
    companion object {

        //* New Custom Dialogs How to Use Example**/
        fun launchDialog(sfm: FragmentManager, title: String, message: String, posText: String, negText: String?, okRun: Runnable?, cancelRun: Runnable?, cancelable: Boolean) {
            val dialogFragment = CustomDialogFragment(CustomDialogModel(title = title, message = message, okBtn = posText, cancelBtn = negText, okRunnable = okRun, cancelRunnable = cancelRun, cancelable = cancelable))
            dialogFragment.show(sfm, dialogFragment.tag)
        }


        //Toasts good because they show over everything where snackbars need a root view to attach to... issues with bottomsheets for example
        fun toast(context: Context, text: String) = Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        //Show a snackbar at the bottom of the view you pass into it. Configure the length by changing the LENGTH_LONG into LENGTH_SHORT
        fun snackbar(rootView: View, text: String) = Snackbar.make(rootView, text, Snackbar.LENGTH_LONG).show()
        fun snackbar(rootView: View, anchorView: View, text: String) = Snackbar.make(rootView, text, Snackbar.LENGTH_LONG).setAnchorView(anchorView).show()
        fun snackbar(rootView: View, text: String, actionText: String, runnable: Runnable) = Snackbar.make(rootView, text, Snackbar.LENGTH_INDEFINITE).setAction(actionText) { runnable.run() }.show()
    }
}