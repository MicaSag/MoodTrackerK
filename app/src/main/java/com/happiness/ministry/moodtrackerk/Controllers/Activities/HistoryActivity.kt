package com.happiness.ministry.moodtrackerk.Controllers.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.gson.Gson
import com.happiness.ministry.moodtrackerk.R
import com.happiness.ministry.moodtrackerk.Controllers.Fragments.HistoryFragment
import com.happiness.ministry.moodtrackerk.Models.MoodPreferences
import com.happiness.ministry.moodtrackerk.Utilities.DateUtilities

class HistoryActivity : AppCompatActivity() {

    // Defined String Preferences
    private var mPreferences: String? = null

    // Defined a MoodPreferences Object
    private var mMoodPreferences: MoodPreferences? = null

    companion object {

        // Create the key Preferences
        val KEY_PREFERENCES = "KEY_PREFERENCES"
    }
    // --------------
    // FRAGMENTS
    // --------------

    // Declare history fragment reference
    private var historyFragment: HistoryFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // Get back Intent send to parameter by the MainActivity
        val intent = intent
        mPreferences = intent.getStringExtra(KEY_PREFERENCES)

        // Restoring the preferences with a Gson Object
        val gson = Gson()
        mMoodPreferences = gson.fromJson<MoodPreferences>(mPreferences, MoodPreferences::class.java)

        Log.i("MOOD", """____HISTORY____
                |Index        = ${mMoodPreferences?.mLastSavedMoodIndex}
                |Mood Index   = ${mMoodPreferences?.getLastMood()?.mMoodIndex}
                |Comment      = ${mMoodPreferences?.getLastMood()?.mComment}
                |mMoodHistory = ${mMoodPreferences?.mMoodHistory?.size}
                |Date         = ${DateUtilities.getIntDateSSAAMMJJ(mMoodPreferences?.getLastMood()?.mDate!!)}""".trimMargin())

        // If at least the mood precedent is present then Configure and show history fragments
        if (mMoodPreferences?.mMoodHistory?.size!! > 1) this.configureAndShowMainFragment()
    }

    // Method which configures and show history fragments
    private fun configureAndShowMainFragment() {
        Log.i("MOOD", "____configureAndShowMainFragment()____")


        // Index of the previous Mood in the arrayList MoodHistorical
        val previousMoodIndex = mMoodPreferences!!.mMoodHistory.size - 2
        // Index of the "array_history" resource "arrays.xml"
        var displayIndex = 6
        // Identifier of the FrameLayout to be update
        var resourceLayout: Int

        // Explore the previously registered list of moods
        for (i in previousMoodIndex downTo 0) {

            // Get back the identifier of the FrameLayout to be update
            resourceLayout = resources.obtainTypedArray(R.array.array_history)
                    .getResourceId(displayIndex, 1)

            // Get back the identifier of the HistoryFragment
            // The FragmentManager (Support)
            historyFragment = supportFragmentManager.findFragmentById(resourceLayout) as HistoryFragment?
            // If no created HistoryFragment
            if (historyFragment == null) {
                // Create new history fragment and send Mood in parameter
                historyFragment = HistoryFragment.newInstance(mMoodPreferences!!.mMoodHistory.get(i))
                // Add it to FrameLayout container
                supportFragmentManager.beginTransaction()
                        .add(resourceLayout, historyFragment)
                        .commit()

                // Next Mood
                displayIndex--
            }
        }
    }
}