package com.happiness.ministry.moodtrackerk.Controllers.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.happiness.ministry.moodtrackerk.R
import com.happiness.ministry.moodtrackerk.Models.Mood
import com.happiness.ministry.moodtrackerk.Utilities.DateUtilities.getDaysBetweenTwoDates
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * History Fragment
 */
class HistoryFragment : Fragment(), View.OnClickListener {

    private var mMoodIndex  : Int = 0             // Defined a Mood index
    private var mDate       : Date? = null        // Defined a Date
    private var mComment    : String? = null      // Defined a comment String

    // Define the widgets variables of the Fragment
    private lateinit var fragmentHistoryLinear: LinearLayout
    private lateinit var fragmentHistoryText: TextView
    private lateinit var fragmentHistoryButton: Button
    private lateinit var fragmentHistoryBlank: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get data from Bundle (created in method newInstance)
        mMoodIndex = arguments!!.getInt(KEY_MOOD_INDEX, -1)
        mComment = arguments!!.getString(KEY_COMMENT, "")
        // Restoring the Date with a Gson Object
        val gson = Gson()
        mDate = gson.fromJson(arguments!!.getString(KEY_DATE, ""), Date::class.java)

        Log.i("MOOD", "KEY_MOOD_INDEX = $mMoodIndex")
        Log.i("MOOD", "KEY_COMMENT    = $mComment")

        // Inflate the layout for this fragment
        val fragmentHistoryLayout = inflater.inflate(R.layout.fragment_history, container, false)

        // Get widgets from layout
        fragmentHistoryLinear = fragmentHistoryLayout.findViewById(R.id.fragment_history_linear)
        fragmentHistoryText = fragmentHistoryLayout.findViewById(R.id.fragment_history_text)
        fragmentHistoryButton = fragmentHistoryLayout.findViewById(R.id.fragment_history_btn)
        fragmentHistoryBlank = fragmentHistoryLayout.findViewById(R.id.fragment_history_blank)

        // set the listening of buttons "comment" on HistoryFragment
        fragmentHistoryButton.setOnClickListener(this)

        // Initialization of the fragment
        try {
            initHistoryFragment()
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return fragmentHistoryLayout
    }

    // Initialization of the fragment
    @Throws(ParseException::class)
    private fun initHistoryFragment() {
        Log.i("MOOD", "_____initHistoryFragment_____")

        // Set color of the Fragment
        fragmentHistoryLinear.setBackgroundColor(resources
                .obtainTypedArray(R.array.colorPagesViewPager)
                .getColor(mMoodIndex, 1))

        // Adjust the size of the Fragment
        val linearParams = fragmentHistoryBlank.layoutParams as LinearLayout.LayoutParams
        linearParams.weight = (mMoodIndex + 1).toFloat()
        val frameParams = fragmentHistoryLinear.layoutParams as LinearLayout.LayoutParams
        frameParams.weight = (4 - mMoodIndex).toFloat()


        // If no comment, hidden the button comment
        if (mComment == "") {
            Log.i("MOOD", "HIDDEN BUTTON COMMENT")
            fragmentHistoryButton.visibility = View.GONE
        }

        // Puts back to zero the hour of the date saved
        val formatString = "%1\$tY%1\$tm%1\$td"
        val dateStringSSAAMMJ = String.format(formatString, mDate)
        val dateNoTime = SimpleDateFormat("yyyyMMdd").parse(dateStringSSAAMMJ)

        // Calculate the difference between the current date and the save date in number of Days
        val deltaDate = getDaysBetweenTwoDates(Date(), dateNoTime)

        when (deltaDate) {
            1, 2, 3, 4, 5, 6, 7 -> {
                // Get back the history comment which must be put on the fragment
                val historyComment = resources.getStringArray(R.array.array_history_comment)[deltaDate]
                // Put the history comment on the fragment
                fragmentHistoryText.text = historyComment
            }
            else -> {
                // More than a week
                if (deltaDate > 7 && deltaDate < 30) {
                    // Get back the history comment which must be put on the fragment
                    // and put it on the fragment
                    fragmentHistoryText.text = resources.getStringArray(R.array.array_history_comment)[8]
                }
                // More than a month
                if (deltaDate >= 30) {
                    // Get back the history comment which must be put on the fragment
                    // and put it on the fragment
                    fragmentHistoryText.text = resources.getStringArray(R.array.array_history_comment)[9]
                }
            }
        }
    }

    override fun onClick(view: View) {

        // Get the layout inflater of the HistoryFragment
        val inflater = this.layoutInflater

        // Inflate the layout of the toast comment
        val toastCommentView = inflater.inflate(R.layout.toast_comment, null)

        // Retrieves the text field of the toast_comment_text
        var text: TextView? = toastCommentView.findViewById(R.id.toast_comment_text)

        // Set the Text to show in TextView
        text?.setText(mComment)

        // Display the Toast comment
        val toast = Toast(activity)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = toastCommentView
        toast.show()
    }

    companion object {

        // Create the key Mood
        val KEY_MOOD_INDEX = "KEY_MOOD_INDEX"
        val KEY_COMMENT = "KEY_COMMENT"
        val KEY_DATE = "KEY_DATE"

        // Method that will create a new instance of PageFragment, and add data to its bundle.
        fun newInstance(mood: Mood): HistoryFragment {

            // Create new fragment
            val frag = HistoryFragment()

            // Create bundle and add it some data
            val args = Bundle()
            args.putInt(KEY_MOOD_INDEX, mood.mMoodIndex)
            val gson = GsonBuilder()
                    .serializeNulls()
                    .disableHtmlEscaping()
                    .create()
            val json = gson.toJson(mood.mDate)
            args.putString(KEY_DATE, json)
            args.putString(KEY_COMMENT, mood.mComment)

            frag.arguments = args

            return frag
        }
    }
}// Required empty public constructor

