package com.happiness.ministry.moodtrackerk.Utilities

import android.util.Log
import java.util.*


/**
 * Created by Michaël SAGOT on 18/03/2018.
 *
 * Utilitarian library for the processing of dates
 */

object DateUtilities {

    // Method allowing the conversion of an object Dates in the format int SSAAMMJJ
    // example : in => Sun Mar 18 16:52:01 GMT+00:00 2018
    //          out => 20180318
    fun getIntDateSSAAMMJJ(date: Date): Int {

        // Utilities formatted as : %1$tY = SSAA , %1$tm = MM , %1$td = JJ
        val formatString = "%1\$tY%1\$tm%1\$td"

        // Date in Format String SSAAMMJJ
        val dateStringSSAAMMJ = String.format(formatString, date)

        // Day of year in Format int SSAAMMJJ

        return Integer.parseInt(dateStringSSAAMMJ)
    }

    // Calculate the difference between two dates in number of Days
    fun getDaysBetweenTwoDates(date1: Date, date2: Date): Int {

        val period1hour = (1000 * 60 * 60).toLong()     // 1000 milliseconds * 60 seconds * 60 minutes = 1 hour
        val period1day = period1hour * 24 // 1 hour * 24 = 1 day

        // Calculate the difference between two dates in milliseconds
        //long difference = Math.abs(date1.getTime()-date2.getTime());
        val difference = date1.getTime() - date2.getTime()

        Log.i("MOOD", "difference    = $difference")
        Log.i("MOOD", "period1day    = $period1day")

        // Return the number of days between two dates
        return (difference / period1day).toInt()
    }
}