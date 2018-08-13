package com.happiness.ministry.moodtrackerk.Controllers.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.happiness.ministry.moodtrackerk.Adapters.PageAdapter
import com.happiness.ministry.moodtrackerk.Controllers.Fragments.PageFragment
import com.happiness.ministry.moodtrackerk.R
import com.happiness.ministry.moodtrackerk.Models.Mood
import com.happiness.ministry.moodtrackerk.Models.MoodPreferences
import com.happiness.ministry.moodtrackerk.Utilities.DateUtilities
import java.util.*


class MainActivity : AppCompatActivity(), PageFragment.OnButtonClickedListener {

    // Defined Preferences of the application
    lateinit var mPreferences: SharedPreferences

    // Defined a MoodPreferences Object
    lateinit var mMoodPreferences: MoodPreferences

    // Defined the ViewPager use by this activity
    lateinit var pager: ViewPager

    // Create the key saving of the preferences of the application
    val PREF_KEY_MOOD : String = "PREF_KEY_MOOD"

    companion object {

        // Create the key Preferences
        val KEY_PREFERENCES = "KEY_PREFERENCES"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        // Initialize the application
        this.init()

        // Configure ViewPager
        this.configureViewPager()
    }

    // Method allowing to restore the Preferences
    // and save the mood of the previous day if it's a new day
    private fun init() {

        // Read Preferences
        mPreferences = getPreferences(Context.MODE_PRIVATE)
        // TEST == >>> Allows to erase all the preferences ( Useful for the test phase )
        //Log.i("MOOD","CLEAR COMMIT PREFERENCES");
        //mPreferences.edit().clear().commit();

        // Recovered PREF_KEY_MOOD in a String Object
        var moodPreferences = mPreferences.getString(PREF_KEY_MOOD, null)

        // if preferences PREF_KEY_MOOD is present
        if (moodPreferences != null) {
            Log.i("MOOD", "PREFERENCES")

            // Restoring the preferences with a Gson Object
            val gson = Gson()
            mMoodPreferences = gson.fromJson(moodPreferences, MoodPreferences::class.java)

            Log.i("MOOD", """____START____
                |Index      = ${mMoodPreferences.mLastSavedMoodIndex}
                |Mood Index = ${mMoodPreferences.getLastMood().mMoodIndex}
                |Comment    = ${mMoodPreferences.getLastMood().mComment}
                |Date       = ${DateUtilities.getIntDateSSAAMMJJ(mMoodPreferences.getLastMood().mDate)}""".trimMargin())

            // If the backup date of the last mood is smaller than the current date
            // Then it is a new day
            if (DateUtilities.getIntDateSSAAMMJJ(mMoodPreferences.getLastMood().mDate) < DateUtilities.getIntDateSSAAMMJJ(Date())) {

                Log.i("MOOD", "NEW DAY")
                // If the list is already full, then we remove the first element of the list
                // And we add a new Mood by default
                if (mMoodPreferences.mMoodHistory.size == 8) {
                    mMoodPreferences.mMoodHistory.removeAt(0)
                }

                // Create a new Mood
                val newMood = Mood()

                // Adds the new Mood in the moodPreferences
                mMoodPreferences.addMoodToPreferences(newMood)
            }
            // If not preference
            // It's the first time when the application is opened because the preferences are absent
        } else {
            Log.i("MOOD", "NO PREFERENCES")
            // Creation of a new MoodPreferences Object with one Mood by Default
            mMoodPreferences = MoodPreferences()
        }
    }

    // Method allowing to configure the viewPager
    private fun configureViewPager() {

        // Get ViewPager from layout
        pager = findViewById<ViewPager>(R.id.activity_main_viewpager)

        // Set Adapter PageAdapter and glue it together

        pager.setAdapter(object : PageAdapter(supportFragmentManager) {})

        // Positioning the front page on Mood by default : smiley happy
        pager.currentItem = mMoodPreferences.getLastMood().mMoodIndex
    }


    override fun onButtonClicked(view: View) {
        // Here, thanks to the callback implemented in the PageFragment
        // We are going to manage the click on the various buttons of PageFragment
        when (view.id) {

            // History Button Clicked
            R.id.fragment_page_history_btn,
                // Camembert Button Clicked
            R.id.fragment_page_camembert_btn ->

                // If a history is present
                if (mMoodPreferences.mMoodHistory.size > 1) {
                    Log.i("MOOD", "SIZE > 1")
                    // Create à PREF_KEY_MOOD String with a Gson Object
                    val gson = GsonBuilder()
                            .serializeNulls()
                            .disableHtmlEscaping()
                            .create()
                    val json = gson.toJson(mMoodPreferences)

                    var intentActivity: Intent? = null

                    // Call activity HistoryActivity
                    Log.i("MOOD", "Click = history")
                    if (view.id == R.id.fragment_page_history_btn) {
                        intentActivity = Intent(this@MainActivity, HistoryActivity::class.java)
                    }

                    // Call activity CamembertActivity
                    Log.i("MOOD", "Click = camembert")
                    if (view.id == R.id.fragment_page_camembert_btn) {
                        intentActivity = Intent(this@MainActivity, CamembertActivity::class.java)
                    }
                    intentActivity!!.putExtra(KEY_PREFERENCES, json)
                    startActivity(intentActivity)
                } else {
                    val toast = Toast.makeText(this, "Not still of present history", Toast.LENGTH_SHORT)
                    toast.show()
                }// Not present history

            // Comment Button Clicked : call comment dialog Box
            R.id.fragment_page_comment_btn -> {
                Log.i("MOOD", "Click = comment")
                // Create, call and manage the Comment Dialog
                manageCommentDialog()
            }
        }
    }

    // Create and manage the Comment Dialog
    private fun manageCommentDialog() {

        val builder = AlertDialog.Builder(this)
        val titleText = "Commentaire"
        // Initialize a new spannable string builder instance
        val ssBuilder = SpannableStringBuilder(titleText)

        // Initialize a new relative size span instance
        // Divide the text size oby 2 on this span
        val largeSizeText = RelativeSizeSpan(0.7f)

        // Apply the relative size span
        ssBuilder.setSpan(
                largeSizeText, // Span to add
                0, // Start of the span
                titleText.length, // End of the span
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extent the span when text add later
        )
        builder.setTitle(ssBuilder)

        // Get the layout inflater of the MainActivity
        val inflater = this.layoutInflater

        // Inflate the layout of the dialog comment
        val dialogCommentView = inflater.inflate(R.layout.dialog_comment, null)

        // Retrieves the input field of the dialog_comment_input
        val input = dialogCommentView.findViewById<EditText>(R.id.dialog_comment_input)

        // Link the AlertDialog at the view dialogCommentView
        builder.setView(dialogCommentView)

        // Set up the buttons
        // Validate Button
        builder.setPositiveButton("VALIDER") { dialog, which ->
            dialog.dismiss()
            // Save comment into mood preferences
            mMoodPreferences.getLastMood().mComment = input.getText().toString()
        }
        // Cancel Button
        builder.setNegativeButton("ANNULER") { dialog, which -> dialog.cancel() }
        builder.show()
    }

    override fun onPause() {
        super.onPause()
        Log.i("MOOD", "status MainActivity = paused")

        // Change the day Mood in MoodPreference
        mMoodPreferences.getLastMood().mMoodIndex = pager.currentItem

        // Create à PREF_KEY_MOOD String with a Gson Object
        val gson = GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .create()
        val json = gson.toJson(mMoodPreferences)

        // Add the new Mood in Preferences
        mPreferences.edit().putString(PREF_KEY_MOOD, json).apply()
    }

}
