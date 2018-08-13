package com.happiness.ministry.moodtrackerk.Views

import android.support.v4.view.ViewPager
import android.view.View

/**
 * Created by Michaël SAGOT on 17/03/2018.
 */

class VerticalPageTransformer : ViewPager.PageTransformer {

    override fun transformPage(view: View, position: Float) {

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.alpha = 0f

        } else if (position <= 1) { // [-1,1]
            view.alpha = 1f

            // Counteract the default slide transition
            view.translationX = view.width * -position

            //set Y position to swipe in from top
            val yPosition = position * view.height
            view.translationY = yPosition

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.alpha = 0f
        }
    }
}