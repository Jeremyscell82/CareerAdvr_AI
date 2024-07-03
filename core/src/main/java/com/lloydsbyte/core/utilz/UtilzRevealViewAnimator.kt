package com.lloydsbyte.core.utilz

import android.animation.Animator
import android.view.View
import android.view.ViewAnimationUtils
import kotlin.math.hypot
import kotlin.math.max

/**
 * This class will turn any regular view into a circular animated one... a little tricky to get working and may be out dated....
 */
class UtilzRevealViewAnimator {

    companion object {

        fun showMenu(view: View) {
            revealView(view, view.width/2, view.height/2, 400, null)
        }

        fun hideMenu(view: View) {
            hideView(view, view.width, view.height, 400, null)
        }

        fun revealView(
            toBeDisplayed: View,
            startPointX: Int,
            startPointY: Int,
            animDuration: Long,
            doOnComplete: Runnable?
        ) {
            toBeDisplayed.visibility = View.INVISIBLE
            val startRadius = 0f
            val endRadius =
                hypot(toBeDisplayed.width.toDouble(), toBeDisplayed.height.toDouble()).toFloat()

            val anim: Animator = ViewAnimationUtils.createCircularReveal(
                toBeDisplayed,
                startPointX,
                startPointY,
                startRadius,
                endRadius
            )

            toBeDisplayed.visibility = View.VISIBLE
            anim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                }

                override fun onAnimationEnd(animation: Animator) {
                    doOnComplete?.run()
                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationRepeat(animation: Animator) {
                }

            })
            anim.duration = animDuration
            anim.start()

        }

        //Still pretty current
        fun hideView(
            toBeHidden: View,
            endPointX: Int,
            endPointY: Int,
            animDuration: Long,
            runnable: Runnable?
        ) {

            val startRadius: Int = max(toBeHidden.width, toBeHidden.height)
            val endRadius = 0

            val anim = ViewAnimationUtils.createCircularReveal(
                toBeHidden,
                endPointX,
                endPointY,
                startRadius.toFloat(),
                endRadius.toFloat()
            )
            anim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {
                }

                override fun onAnimationEnd(animator: Animator) {
                    toBeHidden.visibility = View.GONE
                    runnable?.run()
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })
            anim.duration = animDuration
            anim.start()
        }

    }
}