package com.happiness.ministry.moodtrackerk.Controllers.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.gson.Gson
import com.happiness.ministry.moodtrackerk.R
import com.happiness.ministry.moodtrackerk.Models.MoodPreferences
import java.util.ArrayList

class CamembertActivity : AppCompatActivity() {

    // Defined String Preferences
    private var mPreferences: String? = null

    // Defined a MoodPreferences Object
    private var mMoodPreferences: MoodPreferences? = null

    internal lateinit var pieChart: PieChart

    // Create the key Preferences
    val KEY_PREFERENCES : String = "KEY_PREFERENCES"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camembert)

        // Get PieChart from layout
        pieChart = findViewById(R.id.activity_camembert_piechart)

        // Get back Intent send to parameter by the MainActivity
        val intent = intent
        mPreferences = intent.getStringExtra(KEY_PREFERENCES)

        // Restoring the preferences with a Gson Object
        val gson = Gson()
        mMoodPreferences = gson.fromJson<MoodPreferences>(mPreferences, MoodPreferences::class.java)

        // Initialize the camembert and show it
        this.init()
    }

    private fun init() {

        // Data definition
        val entries = ArrayList<PieEntry>()

        val sortMood = intArrayOf(0, 0, 0, 0, 0)

        for (i in 0 until mMoodPreferences!!.mMoodHistory.size - 1) {
            sortMood[mMoodPreferences!!.mMoodHistory.get(i).mMoodIndex] += 1
        }

        val colors = ArrayList<Int>()
        for (i in sortMood.indices) {
            if (sortMood[i] > 0) {
                entries.add(PieEntry(sortMood[i].toFloat(), resources.getStringArray(R.array.array_mood_text)[i]))
                colors.add(resources.obtainTypedArray(R.array.colorPagesViewPager).getColor(i, 0))
            }
        }
        // Camembert Appearances
        camembertConfig(entries, colors)
    }

    //Method which configures the appearance of the camembert
    private fun camembertConfig(entries: List<PieEntry>, colors: ArrayList<Int>) {

        // Appearance of the Camembert
        val pieDescription = Description()
        pieDescription.text = ""
        pieChart.description = pieDescription
        pieChart.centerText = "Moods"
        pieChart.setCenterTextSize(15f)
        pieChart.setTransparentCircleAlpha(0)
        pieChart.holeRadius = 25f
        pieChart.setDrawEntryLabels(true)
        val pieDataSet = PieDataSet(entries, "")
        pieDataSet.sliceSpace = 2f
        pieDataSet.valueTextSize = 0f
        pieDataSet.colors = colors
        val pieData = PieData(pieDataSet)
        pieChart.data = pieData
        pieChart.invalidate() // refresh

        // Legend Configuration
        val legend = pieChart.legend
        legend.form = Legend.LegendForm.CIRCLE // set what type of form/shape should be used
        legend.xEntrySpace = 10f // set the space between the legend entries on the y-axis
        legend.textSize = 12f
    }

    companion object {

        private val TAG = "MainActivity"
    }
}