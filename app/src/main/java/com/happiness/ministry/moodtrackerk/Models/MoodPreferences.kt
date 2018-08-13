package com.happiness.ministry.moodtrackerk.Models

/**
 * Created by Michaël SAGOT on 13/08/2018.
 *
 * This Object which will saved as preferences
 */

class MoodPreferences(var mLastSavedMoodIndex : Int = 0,                                    // Index of the list corresponding to the last Mood saved
                      var mMoodHistory        : MutableList<Mood> = mutableListOf<Mood>())  // List containing 7 last Mood saved + Mood of the Day
{
    init {
        mMoodHistory.add(Mood())    // Put Default Mood in the List : post (0)
    }

    /**
     * Constructor by default
     */
    constructor() : this(0, mutableListOf<Mood>())

    /**
     * Method which turns the last saved Mood
     */
    fun getLastMood(): Mood {
        return this.mMoodHistory[this.mLastSavedMoodIndex]
    }

    /**
     * Method which adds a new mood to the historical list
     */
    fun addMoodToPreferences (mood : Mood) {

        // Adds a new Mood in the history
        mMoodHistory.add(mood)

        // Save index of the last Mood added
        mLastSavedMoodIndex = mMoodHistory.size - 1
    }
}