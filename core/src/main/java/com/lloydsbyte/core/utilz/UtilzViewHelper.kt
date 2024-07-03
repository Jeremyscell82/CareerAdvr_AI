package com.lloydsbyte.core.utilz

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.lloydsbyte.core.custombottomsheet.ErrorBottomsheet

class UtilzViewHelper {
    companion object {
        inline fun View.fadeIn(durationMillis: Long = 400) {
            this.visibility = View.VISIBLE
            this.startAnimation(AlphaAnimation(0F, 1F).apply {
                duration = durationMillis
                fillAfter = false
            })
        }

        inline fun View.fadeOut(durationMillis: Long = 400) {
            this.visibility = View.GONE
            this.startAnimation(AlphaAnimation(1F, 0F).apply {
                duration = durationMillis
                fillAfter = false
            })
        }

        fun showSoftKeyboard(context: Context, view: View) {
            if (view.requestFocus()) {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        fun hideSoftKeyboard(context: Context, view: View) {
            if (view.requestFocus()){
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        fun requestFocusOnView(context: Context, view: View) {
            val inputMethodManager: InputMethodManager =
                context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }

        fun circularRevealView(myView: View) {
            // Get the center for the clipping circle.
            val cx = myView.width / 2
            val cy = myView.height / 2

            // Get the final radius for the clipping circle.
            val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

            // Create the animator for this view. The start radius is 0.
            val anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0f, finalRadius)
            // Make the view visible and start the animation.
            myView.visibility = View.VISIBLE
            anim.start()
        }

        fun dismissCircularRevealView(myView: View) {
            // Get the center for the clipping circle.
            val cx = myView.width / 2
            val cy = myView.height / 2

            // Get the final radius for the clipping circle.
            val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

            // Create the animator for this view. The start radius is 0.
            val anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0f, finalRadius)
            // Make the view visible and start the animation.
            myView.visibility = View.VISIBLE
            anim.start()
        }

    }
}