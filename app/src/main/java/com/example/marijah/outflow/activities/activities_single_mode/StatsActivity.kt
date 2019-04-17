package com.example.marijah.outflow.activities.activities_single_mode

import android.app.Activity
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.marijah.outflow.R
import com.example.marijah.outflow.helpers.HelperManager
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_chart.*


class StatsActivity : Activity() {

    // pie chart
    private val yData = floatArrayOf(25.3f, 10.6f, 66.76f, 44.32f, 46.01f, 16.89f, 23.9f)
    private val xData = arrayOf("Mitch", "Jessica", "Mohammad", "Kelsey", "Sam", "Robert", "Ashley")

    private lateinit var typefaceOswald: Typeface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        typefaceOswald = Typeface.createFromAsset(assets, "oswald_regular.ttf")
        setLayoutsAndListeners()
    }

    private fun setLayoutsAndListeners() {
        HelperManager.setTypefaceRegular(assets, txtViewName)


        setupLineChart()


        imgViewLineChart.setOnClickListener {
            setThePickedOneYellowAndTheOthersBlue(imgViewLineChart)
            setupLineChart()
            lineChart.visibility = View.VISIBLE
            pieChart.visibility = View.INVISIBLE
            barChart.visibility = View.INVISIBLE

        }

        imgViewPieChart.setOnClickListener {
            setThePickedOneYellowAndTheOthersBlue(imgViewPieChart)
            setupPieChart()
            lineChart.visibility = View.INVISIBLE
            pieChart.visibility = View.VISIBLE
            barChart.visibility = View.INVISIBLE
        }

        imgViewBarChart.setOnClickListener {
            setThePickedOneYellowAndTheOthersBlue(imgViewBarChart)
            setupBarChart()
            lineChart.visibility = View.INVISIBLE
            pieChart.visibility = View.INVISIBLE
            barChart.visibility = View.VISIBLE
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


    private fun setupLineChart() {
        val entries = ArrayList<Entry>()

        // for (data in dataObjects) {

        // turn your data into Entry objects
        entries.add(Entry(200f, 150f))
        entries.add(Entry(220f, 120f))
        entries.add(Entry(230f, 160f))


        val dataSet = LineDataSet(entries, "Label") // add entries to data set
        dataSet.color = Color.RED
        dataSet.setDrawFilled(true)
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.fillColor = Color.CYAN
        dataSet.color = Color.CYAN
        dataSet.fillAlpha = 255
        dataSet.setDrawCircles(false)

        dataSet.valueTextColor = Color.YELLOW

        val lineData = LineData(dataSet)
        lineChart.data = lineData
        lineChart.invalidate()

    }

    private fun setupPieChart() {


        //pieChart.setDescription("Sales by employee (In Thousands $) ");
        val description = Description()
        description.text = "Random data chart"
        description.textColor = ContextCompat.getColor(this, R.color.white_color)
        description.typeface = typefaceOswald
        pieChart.description = description
        pieChart.isRotationEnabled = true;
        //pieChart.setUsePercentValues(true);
        pieChart.setHoleColor(ContextCompat.getColor(this, R.color.blue_dark_color))
        pieChart.setCenterTextColor(ContextCompat.getColor(this, R.color.white_color))
        pieChart.holeRadius = 60f
        pieChart.setTransparentCircleAlpha(0)
        pieChart.centerText = "Bravo Marijo"
        pieChart.setCenterTextTypeface(typefaceOswald)
        pieChart.setCenterTextSize(10f);
        //pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelTextSize(20f);
        pieChart.setDrawEntryLabels(true)
        pieChart.setEntryLabelColor(ContextCompat.getColor(this, R.color.white_color))
        pieChart.setEntryLabelTypeface(typefaceOswald)
        //More options just check out the documentation!

        addDataSet()

        pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry, h: Highlight) {
                /* Log.d(TAG, "onValueSelected: Value select from chart.")
                 Log.d(TAG, "onValueSelected: $e")
                 Log.d(TAG, "onValueSelected: " + h.toString())*/

                var pos1 = e.toString().indexOf("(sum): ")
                val sales = e.toString().substring(pos1 + 7)
                //  val salesFloat = sales.toFloat()

                /* for (i in 0 until yData.size) {
                     if (yData[i] == salesFloat) {
                         pos1 = i
                         break
                     }
                 }*/
                val employee = xData[pos1 + 1]
                Toast.makeText(applicationContext, "Employee " + employee + "\n" + "Sales: $" + sales + "K", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected() {

            }
        })
    }


    private fun addDataSet() {

        val yEntrys: ArrayList<PieEntry> = ArrayList()
        val xEntrys: ArrayList<String> = ArrayList()

        for (i in 0 until yData.size) {
            yEntrys.add(PieEntry(yData[i], i))
        }

        for (i in 1 until xData.size) {
            xEntrys.add(xData[i])
        }

        //create the data set
        val pieDataSet = PieDataSet(yEntrys, "Okk")


        pieDataSet.color = ContextCompat.getColor(this, R.color.white_color)
        pieDataSet.valueTextColor = ContextCompat.getColor(this, R.color.blue_dark_color)
        pieDataSet.sliceSpace = 2f
        pieDataSet.valueTextSize = 12f
        pieDataSet.valueTypeface = typefaceOswald

        //add colors to dataset
        /*val colors: ArrayList<Int> = ArrayList()
        colors.add(ContextCompat.getColor(this, R.color.chart_color_1))
        colors.add(ContextCompat.getColor(this, R.color.chart_color_2))
        colors.add(ContextCompat.getColor(this, R.color.chart_color_3))
        colors.add(ContextCompat.getColor(this, R.color.chart_color_4))
        colors.add(ContextCompat.getColor(this, R.color.chart_color_5))
        colors.add(ContextCompat.getColor(this, R.color.chart_color_6))
        colors.add(ContextCompat.getColor(this, R.color.chart_color_8))*/

        val ok = ColorTemplate.JOYFUL_COLORS
        pieDataSet.colors = ok.toMutableList()

        val pieEntryLabels: ArrayList<String> = ArrayList()

        pieEntryLabels.add("January")
        pieEntryLabels.add("February")
        pieEntryLabels.add("March")
        pieEntryLabels.add("April")
        pieEntryLabels.add("May")
        pieEntryLabels.add("June")

        //add legend to chart
        /*  val legend = pieChart.legend
          legend.form = Legend.LegendForm.DEFAULT
          legend.position = Legend.LegendPosition.LEFT_OF_CHART
          legend.typeface= typefaceOswald
          legend.textColor = ContextCompat.getColor(this, R.color.white_color)*/


        val l = pieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.form = Legend.LegendForm.CIRCLE
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f
        l.isWordWrapEnabled = true
        l.setDrawInside(false)
        l.calculatedLineSizes

        // l.entries = (yEntrys.)


        //create pie data object
        val pieData = PieData(pieDataSet)
        pieChart.data = pieData
        pieChart.invalidate()
    }


    private fun setupBarChart() {

        /* val barEntryLabels: ArrayList<IBarDataSet> = ArrayList()
         val barEntry : ArrayList<BarEntry> = ArrayList()


         barEntry.add(BarEntry(2f, 0f))
         barEntry.add(BarEntry(4f, 1f))
         barEntry.add(BarEntry(6f, 2f))
         barEntry.add(BarEntry(8f, 3f))
         barEntry.add(BarEntry(7f, 4f))
         barEntry.add(BarEntry(3f, 5f))


         barEntryLabels.add(IBarDataSet(BarEntry(2f, 0f)))
         barEntryLabels.add("February")
         barEntryLabels.add("March")
         barEntryLabels.add("April")
         barEntryLabels.add("May")
         barEntryLabels.add("June")

         val barDataSet: BarDataSet = BarDataSet(barEntry, "Projects")
         barDataSet.colors = ColorTemplate.COLORFUL_COLORS

         Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

         val barData: BarData = BarData(barEntryLabels, barDataSet)


         barChart.data = barData

         barChart.animateY(3000)*/


        val desc = barChart.description
        val legend = barChart.legend

        desc.text = "Hello" // this is the weirdest way to clear something!!
        legend.isEnabled = false


        val leftAxis = barChart.axisLeft
        val rightAxis = barChart.axisRight
        val xAxis = barChart.xAxis

        xAxis.position = XAxisPosition.BOTTOM
        xAxis.textSize = 10f
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawGridLines(false)


        leftAxis.textSize = 10f
        leftAxis.setDrawLabels(false)
        leftAxis.setDrawAxisLine(true)
        leftAxis.setDrawGridLines(false)

        rightAxis.setDrawAxisLine(false)
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawLabels(false)

        val data = BarData(setBarData())


        data.barWidth = 0.9f // set custom bar width
        barChart.data = data

        barChart.setFitBars(true) // make the x-axis fit exactly all bars
        barChart.invalidate() // refresh
        barChart.setBorderColor(R.color.yellow_color)
        barChart.setScaleEnabled(true)
        barChart.isDoubleTapToZoomEnabled = true
        /* barChart.setBackgroundColor(Color.rgb(255, 255, 255))*/
        barChart.animateXY(2000, 2000)
        barChart.setDrawBorders(false)
        barChart.description = desc
        barChart.setDrawValueAboveBar(true)


    }


    private fun setBarData(): BarDataSet {

        val entries: ArrayList<BarEntry> = ArrayList()

        entries.add(BarEntry(0f, 30f))
        entries.add(BarEntry(1f, 80f))
        entries.add(BarEntry(2f, 60f))
        entries.add(BarEntry(3f, 50f))
        entries.add(BarEntry(4f, 70f))
        entries.add(BarEntry(5f, 60f))


        val set = BarDataSet(entries, "")
        set.color = Color.YELLOW
        val ok = ColorTemplate.VORDIPLOM_COLORS
        set.colors = ok.toMutableList()
        set.valueTextColor = ContextCompat.getColor(this, R.color.blue_light_color)

        return set
    }

}


