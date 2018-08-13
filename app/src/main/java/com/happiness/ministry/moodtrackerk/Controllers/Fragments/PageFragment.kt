package com.happiness.ministry.moodtrackerk.Controllers.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.happiness.ministry.moodtrackerk.R


/**
 * PageFragment class
 */
class PageFragment : Fragment(), View.OnClickListener {

    // ===>> CALL BACK <====
    //Declare a callback
    private var mCallback: OnButtonClickedListener? = null

    //Declare our interface that will be implemented by any container activity
    interface OnButtonClickedListener {
        fun onButtonClicked(view: View)
    }

    override// Called as soon as a fragment asks to be shown
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                     savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val fragmentPageLayout = inflater.inflate(R.layout.fragment_page, container, false)

        // Get widgets from layout
        val rootView = fragmentPageLayout.findViewById<FrameLayout>(R.id.fragment_page_rootview)
        val imageView = fragmentPageLayout.findViewById<ImageView>(R.id.fragment_page_smiley_img)

        // Get data from Bundle (created in method newInstance)
        val position = arguments!!.getInt(KEY_POSITION, -1)

        // Update widgets with it
        // Set Background Color of the Mood
        rootView.background = resources.obtainTypedArray(R.array.colorPagesViewPager).getDrawable(position)
        // Set Image of the Mood
        imageView.setImageDrawable(resources.obtainTypedArray(R.array.array_smileys).getDrawable(position))

        // set the listening of buttons "comment", "history" and "camembert" on the PageFragment
        fragmentPageLayout.findViewById<View>(R.id.fragment_page_comment_btn).setOnClickListener(this)
        fragmentPageLayout.findViewById<View>(R.id.fragment_page_history_btn).setOnClickListener(this)
        fragmentPageLayout.findViewById<View>(R.id.fragment_page_camembert_btn).setOnClickListener(this)


        return fragmentPageLayout
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // Creating callback after being attached to parent activity
        try {
            //Parent activity will automatically subscribe to callback
            mCallback = activity as OnButtonClickedListener
        } catch (e: ClassCastException) {
            throw ClassCastException(e.toString() + " must implement OnButtonClickedListener")
        }

    }

    override fun onClick(view: View) {

        // Spreads the click to the parent activity
        mCallback!!.onButtonClicked(view)
    }

    companion object {

        // Create keys for the Bundle of the fragment
        private val KEY_POSITION = "position"

        // Method that will create a new instance of PageFragment, and add data to its bundle.
        fun newInstance(position: Int): PageFragment {

            // Create new fragment
            val frag = PageFragment()

            // Create bundle and add it some data
            val args = Bundle()
            args.putInt(KEY_POSITION, position)

            frag.arguments = args

            return frag
        }
    }
}// Required empty public constructor