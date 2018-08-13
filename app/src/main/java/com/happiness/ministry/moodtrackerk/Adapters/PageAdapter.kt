package com.happiness.ministry.moodtrackerk.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.happiness.ministry.moodtrackerk.Controllers.Fragments.PageFragment

/**
 * Created by Michaël SAGOT on 18/03/2018.
 */

open class PageAdapter// Default Constructor
(mgr: FragmentManager) : FragmentPagerAdapter(mgr) {

    override fun getCount(): Int {
        return 5 // Number of page to show
    }

    override fun getItem(position: Int): Fragment {
        // Page to return
        return PageFragment.newInstance(position)
    }
}
