package com.example.marijah.outflow.activities.activities_single_mode

import android.app.Activity
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import com.example.marijah.outflow.R
import com.example.marijah.outflow.helpers.HelperManager
import kotlinx.android.synthetic.main.activity_chart.*

class StatsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        setLayoutsAndListeners()
    }

    private fun setLayoutsAndListeners() {
        HelperManager.setTypefaceRegular(assets, txtViewName)


        imgViewLineChart.setOnClickListener {
            setThePickedOneYellowAndTheOthersBlue(imgViewLineChart)
        }

        imgViewPieChart.setOnClickListener {
            setThePickedOneYellowAndTheOthersBlue(imgViewPieChart)
        }

        imgViewBarChart.setOnClickListener {
            setThePickedOneYellowAndTheOthersBlue(imgViewBarChart)
        }

        setThePickedOneYellowAndTheOthersBlue(imgViewLineChart)

    }

    /**
     * Funkcija koja menja boju kliknutom imageView-u kako bi odala utisak selekcije
     */
    private fun setThePickedOneYellowAndTheOthersBlue(pickedImageView: ImageView) {
        val colorBlue = ContextCompat.getColor(this, R.color.blue_light_color)
        imgViewLineChart.setColorFilter(colorBlue, PorterDuff.Mode.SRC_ATOP)
        imgViewPieChart.setColorFilter(colorBlue, PorterDuff.Mode.SRC_ATOP)
        imgViewBarChart.setColorFilter(colorBlue, PorterDuff.Mode.SRC_ATOP)
        pickedImageView.setColorFilter(ContextCompat.getColor(this, R.color.yellow_color), PorterDuff.Mode.SRC_ATOP)

    }

}
