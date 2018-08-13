package com.happiness.ministry.moodtrackerk.Models

import java.util.*

/**
 * Created by Michaël SAGOT on 13/08/2018.
 */

class Mood (    var mMoodIndex  : Int       = 3,        // Mood index (0 - 4) of the "array_smileys" (res/values/arrays.xml)
                var mComment    : String    = "",       // Comment of the Mood
                var mDate       : Date      = Date())   // Backup date of the Mood ( current date )

