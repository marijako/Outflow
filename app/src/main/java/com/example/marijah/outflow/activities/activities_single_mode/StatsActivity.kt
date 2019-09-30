package com.example.marijah.outflow.activities.activities_single_mode

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import com.example.marijah.outflow.R
import com.example.marijah.outflow.helpers.HelperManager
import com.example.marijah.outflow.models.AppManager
import com.example.marijah.outflow.models.ExpenseItem
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_chart.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class StatsActivity : Activity(), AdapterView.OnItemSelectedListener {


    private val arrayOfExpenses: ArrayList<ExpenseItem> = ArrayList()

    private lateinit var typefaceOswald: Typeface

    private lateinit var arrayListOfColors: Array<Int>

    val pieEntries: ArrayList<PieEntry> = ArrayList()

    private var currentlyPickedTime = TimeReference.THIS_DAY


    private enum class TimeReference {
        THIS_DAY, THIS_WEEK, THIS_MONTH, THIS_YEAR
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        // prvo stavke iz baze troskova smestamo u jednu array listu troskova
        getArrayListOfExpenses()

        // uzimamo niz boja koje cemo da koristimo pri prikazu grafikona
        getArrayListOfColors()

        val arrayOfTimeLapses = arrayOf("This day", "This week", "This month", "This year")

        // Declaring an Adapter and initializing it to the data pump
        val adapter = ArrayAdapter(applicationContext, R.layout.spinner_item, arrayOfTimeLapses)
        // Setting Adapter to the Spinner
        spinner.adapter = adapter
        // Setting OnItemClickListener to the Spinner
        spinner.onItemSelectedListener = this


        typefaceOswald = Typeface.createFromAsset(assets, "oswald_regular.ttf")
        setLayoutsAndListeners()
    }

    private fun getArrayListOfColors() {
        arrayListOfColors = arrayOf(
                ColorTemplate.COLORFUL_COLORS[0],
                ColorTemplate.COLORFUL_COLORS[1],
                ColorTemplate.COLORFUL_COLORS[2],
                ColorTemplate.COLORFUL_COLORS[3],
                ColorTemplate.COLORFUL_COLORS[4],
                ColorTemplate.JOYFUL_COLORS[0],
                ColorTemplate.JOYFUL_COLORS[1],
                ColorTemplate.JOYFUL_COLORS[2],
                ColorTemplate.JOYFUL_COLORS[3],
                ColorTemplate.JOYFUL_COLORS[4],
                ColorTemplate.VORDIPLOM_COLORS[0],
                ColorTemplate.VORDIPLOM_COLORS[1],
                ColorTemplate.VORDIPLOM_COLORS[2],
                ColorTemplate.VORDIPLOM_COLORS[3],
                ColorTemplate.VORDIPLOM_COLORS[4],
                ColorTemplate.MATERIAL_COLORS[0],
                ColorTemplate.MATERIAL_COLORS[1],
                ColorTemplate.MATERIAL_COLORS[2],
                ColorTemplate.MATERIAL_COLORS[3],
                ColorTemplate.LIBERTY_COLORS[0],
                ColorTemplate.LIBERTY_COLORS[1],
                ColorTemplate.LIBERTY_COLORS[2],
                ColorTemplate.LIBERTY_COLORS[3],
                ColorTemplate.LIBERTY_COLORS[4],
                Color.parseColor("#a32f80"),
                Color.parseColor("#341677"),
                Color.parseColor("#7f78d2"),
                Color.parseColor("#d2d0fe"))


        //val arrayListOfColors1 : ArrayList<Int> =  ArrayList()

        //val list = ArrayList<String>(Arrays.asList(ColorTemplate.COLORFUL_COLORS))
        //arrayListOfColors1.addAll(list)


    }


    // Defining the Callback methods here
    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        //showToast(this, spinner.getItemAtPosition(pos).toString())

        Log.i("Marijaaaa", "${spinner.getItemAtPosition(pos)} i ovaj drugi : {${parent.getItemAtPosition(pos)}}")
        when (pos) {
            0 -> currentlyPickedTime = TimeReference.THIS_DAY
            1 -> currentlyPickedTime = TimeReference.THIS_WEEK
            2 -> currentlyPickedTime = TimeReference.THIS_MONTH
            3 -> currentlyPickedTime = TimeReference.THIS_YEAR

        }

        setupPieChart()

    }

    // Defining the Callback methods here
    override fun onNothingSelected(arg0: AdapterView<*>) {}


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

            Toast.makeText(this, "OMG", Toast.LENGTH_SHORT).show()
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

    private var xData: Array<String>? = null
    private var arrayListOfExpensesByPercentForPie: Array<Float>? = null

    private fun setupPieChart() {

        /*arrayListOfExpensesByPercentForPie = arrayOf(25.3f, 10.6f, 66.76f, 44.32f, 46.01f, 16.89f, 23.9f)
        xData = arrayOf("Mitch", "Jessica", "Mohammad", "Kelsey", "Sam", "Robert", "Ashley")

        // pie chart
        val description = Description()
        description.text = "Expences for this day"
        description.textColor = ContextCompat.getColor(this, R.color.white_color)
        description.typeface = typefaceOswald
        pieChart.description = description


        pieChart.isRotationEnabled = true
        //pieChart.setUsePercentValues(true)
        pieChart.setHoleColor(ContextCompat.getColor(this, R.color.blue_dark_color))
        pieChart.setCenterTextColor(ContextCompat.getColor(this, R.color.white_color))
        pieChart.holeRadius = 60f
        pieChart.setTransparentCircleAlpha(0)
        //pieChart.centerText = "Bravo Marijo"
        pieChart.setCenterTextTypeface(typefaceOswald)
        pieChart.setCenterTextSize(10f)
        //pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelTextSize(20f)
        pieChart.setDrawEntryLabels(true)
        pieChart.setEntryLabelColor(ContextCompat.getColor(this, R.color.white_color))
        pieChart.setEntryLabelTypeface(typefaceOswald)
        //More options just check out the documentation!


        pieChart.setOnClickListener {

            Toast.makeText(this@StatsActivity, "wtfff", Toast.LENGTH_LONG).show()

        }


        pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener{
            override fun onNothingSelected() {
                Toast.makeText(this@StatsActivity, "nista", Toast.LENGTH_LONG).show()

            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                Toast.makeText(this@StatsActivity, "Employee " + e!!.x + "\n" + "Sales: $" + e.y + "K", Toast.LENGTH_LONG).show()
            }
        })

        addPieDataSet()
*/

        /*val pieEntries: ArrayList<PieEntry> = ArrayList()
        pieEntries.add(PieEntry(945f, "Maxi"))
        pieEntries.add(PieEntry(1030f, "Bitches"))
        pieEntries.add(PieEntry(1143f, "Okej"))
        pieEntries.add(PieEntry(250f, "Wtf"))
        pieEntries.add(PieEntry(3444f, "Lebtemazo"))
        pieEntries.add(PieEntry(987f, "Pozdrav"))
        pieEntries.add(PieEntry(5555f, "Hello"))

        pieEntries.add(PieEntry(250f, "Wtf"))
        pieEntries.add(PieEntry(3444f, "Lebtemazo"))
        pieEntries.add(PieEntry(987f, "Pozdrav"))
        pieEntries.add(PieEntry(5555f, "Hello"))

        pieEntries.add(PieEntry(250f, "Wtf"))
        pieEntries.add(PieEntry(3444f, "Lebtemazo"))
        pieEntries.add(PieEntry(987f, "Pozdrav"))
        pieEntries.add(PieEntry(5555f, "Hello"))*/


        addPieData()

        val pieDataSet = PieDataSet(pieEntries, "")
        //val arrayOfColors = arrayOf(Color.WHITE, Color.MAGENTA, Color.GREEN, Color.BLUE, Color.CYAN, Color.RED, Color.YELLOW, Color.BLACK, Color.GRAY)
        Log.i("Marija", "${Color.BLACK}")
        pieDataSet.colors = arrayListOfColors.toMutableList()


        val legend = pieChart.legend
        legend.textColor = Color.WHITE
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.form = Legend.LegendForm.CIRCLE
        legend.xEntrySpace = 15f
        legend.yEntrySpace = 5f
        legend.yOffset = 0f
        legend.isWordWrapEnabled = true
        legend.textSize = 10f
        legend.typeface = typefaceOswald
        legend.setDrawInside(false)


        pieChart.setHoleColor(ContextCompat.getColor(this, R.color.blue_dark_color))
        pieChart.holeRadius = 50f

        pieChart.setDrawEntryLabels(false)
        pieChart.setEntryLabelColor(ContextCompat.getColor(this, R.color.blue_dark_color))
        pieChart.setCenterTextTypeface(typefaceOswald)
        pieChart.setCenterTextSize(13f)
        pieChart.setCenterTextColor(ContextCompat.getColor(this, R.color.blue_dark_color))
        pieChart.setTransparentCircleAlpha(0)
        //pieChart.setUsePercentValues(true)
        pieChart.valuesToHighlight()
        pieChart.setDrawSliceText(false)
        pieChart.setDrawCenterText(false)

        val description = Description()
        description.text = "Expences for this day"
        description.textColor = ContextCompat.getColor(this, R.color.white_color)
        description.typeface = typefaceOswald
        pieChart.description = description

        val pieData = PieData(pieDataSet)
        pieChart.data = pieData
        pieChart.animateXY(3000, 3000)
        pieChart.invalidate()

        //Log.i("Marija", "${arrayOfExpenses[0]}")

    }


    /*pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
        override fun onValueSelected(e: Entry, h: Highlight) {
            *//* Log.d(TAG, "onValueSelected: Value select from chart.")
                 Log.d(TAG, "onValueSelected: $e")
                 Log.d(TAG, "onValueSelected: " + h.toString())*//*

                var pos1 = e.toString().indexOf("(sum): ")
                val sales = e.toString().substring(pos1 + 7)
                //  val salesFloat = sales.toFloat()

                *//* for (i in 0 until yData.size) {
                     if (yData[i] == salesFloat) {
                         pos1 = i
                         break
                     }
                 }*//*
                val employee = xData!![pos1 + 1]
                Toast.makeText(applicationContext, "Employee " + employee + "\n" + "Sales: $" + sales + "K", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected() {

            }
        })*/


    private fun addPieData() {
        var itemExists = false


        pieEntries.clear()

        for (item in arrayOfExpenses) {

            if (currentlyPickedTime == TimeReference.THIS_DAY && item.date == getTodaysDate()) {
                for (i in 0 until pieEntries.size) {
                    if (pieEntries[i].label == item.category) {
                        val totalValue = pieEntries[i].value + item.price.toFloat()
                        pieEntries[i] = PieEntry(totalValue, item.category)
                        itemExists = true
                        break
                    }
                }

                if (!itemExists) {
                    pieEntries.add(PieEntry(item.price.toFloat(), item.category))
                } else
                    itemExists = false

            }
        }

    }

    private fun filterPieData() {

        var itemExists = false


        pieEntries.clear()

        for (item in arrayOfExpenses) {

            //if(item.date == currentlyPickedTime)


            for (i in 0 until pieEntries.size) {
                if (pieEntries[i].label == item.category) {
                    val totalValue = pieEntries[i].value + item.price.toFloat()
                    pieEntries[i] = PieEntry(totalValue, item.category)
                    itemExists = true
                    break
                }
            }

            if (!itemExists) {
                pieEntries.add(PieEntry(item.price.toFloat(), item.category))
            } else
                itemExists = false
        }

    }


    private fun addPieDataSet() {


/*        val yEntrys: ArrayList<PieEntry> = ArrayList()
        val xEntrys: ArrayList<String> = ArrayList()

        for (i in 0 until arrayListOfExpensesByPercentForPie!!.size) {
            yEntrys.add(PieEntry(arrayListOfExpensesByPercentForPie!![i], i))
        }

        for (i in 1 until xData!!.size) {
            xEntrys.add(xData!![i])
        }

        //create the data set
        val pieDataSet = PieDataSet(yEntrys, "Okk")


        pieDataSet.color = ContextCompat.getColor(this, R.color.white_color)
        pieDataSet.valueTextColor = ContextCompat.getColor(this, R.color.blue_dark_color)
        pieDataSet.sliceSpace = 2f
        pieDataSet.valueTextSize = 12f
        pieDataSet.valueTypeface = typefaceOswald


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
        *//*  val legend = pieChart.legend
          legend.form = Legend.LegendForm.DEFAULT
          legend.position = Legend.LegendPosition.LEFT_OF_CHART
          legend.typeface= typefaceOswald
          legend.textColor = ContextCompat.getColor(this, R.color.white_color)*//*


        val legend = pieChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.form = Legend.LegendForm.DEFAULT
        legend.xEntrySpace = 15f
        legend.yEntrySpace = 15f
        legend.yOffset = 0f
        legend.isWordWrapEnabled = true
        legend.setDrawInside(false)
        legend.calculatedLineSizes

        // l.entries = (yEntrys.)


        //create pie data object
        val pieData = PieData(pieDataSet)
        pieChart.data = pieData
        pieChart.invalidate()*/
    }


    private fun setupBarChart() {

        val desc = barChart.description
        desc.text = "" // this is the weirdest way to clear something!!

        val legend = barChart.legend
        legend.form = Legend.LegendForm.CIRCLE

        //legend.isEnabled = false


        val leftAxis = barChart.axisLeft
        leftAxis.textSize = 10f
        leftAxis.textColor = Color.WHITE


        val rightAxis = barChart.axisRight
        rightAxis.setDrawAxisLine(false)
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawLabels(false)


        val xAxis = barChart.xAxis
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.textSize = 10f
        xAxis.textColor = Color.WHITE
        //xAxis.setDrawAxisLine(true)
        //xAxis.setDrawGridLines(false)
//
//

        //leftAxis.setDrawAxisLine(true)
        //leftAxis.setDrawGridLines(false)
//
        //rightAxis.setDrawAxisLine(false)
        //rightAxis.setDrawGridLines(false)
        //rightAxis.setDrawLabels(false)

        val data = BarData(setBarData())
        data.barWidth = 0.8f // set custom bar width
        barChart.data = data

        barChart.setFitBars(true) // make the x-axis fit exactly all bars
        // refresh
        barChart.setBorderColor(R.color.yellow_color)
        barChart.setScaleEnabled(true)
        //barChart.isDoubleTapToZoomEnabled = true
        /* barChart.setBackgroundColor(Color.rgb(255, 255, 255))*/
        barChart.animateXY(2000, 2000)
        barChart.setDrawBorders(false)
        barChart.description = desc
        barChart.setDrawValueAboveBar(true)

        barChart.invalidate()

    }


    private fun setBarData(): BarDataSet {

        val entries: ArrayList<BarEntry> = ArrayList()

        entries.add(BarEntry(0f, 20f))
        entries.add(BarEntry(1f, 80f))
        entries.add(BarEntry(2f, 60f))
        entries.add(BarEntry(3f, 50f))
        entries.add(BarEntry(4f, 70f))
        entries.add(BarEntry(5f, 60f))
        entries.add(BarEntry(2.5f, 20f))
        //entries.add(BarEntry(1.5f, 80f))
        //entries.add(BarEntry(0.3f, 60f))
        entries.add(BarEntry(8.3f, 50f))
        entries.add(BarEntry(2.4f, 70f))
        entries.add(BarEntry(7.1f, 60f))


        val set = BarDataSet(entries, "")
        set.color = Color.YELLOW
        set.colors = arrayListOfColors.toMutableList()
        set.valueTextColor = ContextCompat.getColor(this, R.color.blue_light_color)
        return set
    }


    /**
     * Funkcija za preuzimanje troskova i smestanje u arrej listu troskova
     */
    private fun getArrayListOfExpenses() {

        val database = FirebaseDatabase.getInstance()
        val myReferenceToExpenses = database.reference.child(AppManager.getInstance(this).currentlyLookedTableName)

        val childEventListenerForExpenses = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                val expenseItem = dataSnapshot.getValue(ExpenseItem::class.java)
                // dodajemo u listu troskova
                if (expenseItem != null)
                    arrayOfExpenses.add(expenseItem)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}

            override fun onCancelled(databaseError: DatabaseError) {}
        }

        myReferenceToExpenses.addChildEventListener(childEventListenerForExpenses)
    }


    @SuppressLint("SimpleDateFormat")
    private fun getTodaysDate(): String {

        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd.MM.yyyy.")
        return df.format(c)
    }

}