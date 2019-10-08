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
import com.example.marijah.outflow.popups.CalendarPopup
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_chart.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class StatsActivity : Activity(), AdapterView.OnItemSelectedListener {


    private lateinit var typefaceOswald: Typeface
    private lateinit var arrayListOfColors: Array<Int>
    private val arrayOfExpenses: ArrayList<ExpenseItem> = ArrayList()

    private val pieEntries: ArrayList<PieEntry> = ArrayList()

    private var currentlyPickedTime = TimeReference.THIS_DAY

    /**
     * Pocetni i finalni datum pri odabiru mesecnog ranga.
     */
    private var startDate = ""
    private var endDate = ""

    private enum class TimeReference {
        THIS_DAY, SOME_DAY, THIS_MONTH, SOME_RANGE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        // prvo stavke iz baze troskova smestamo u jednu array listu troskova
        getArrayListOfExpenses()

        // uzimamo niz boja koje cemo da koristimo pri prikazu grafikona
        getArrayListOfColors()

        val arrayOfTimeLapses = arrayOf("This day", "This month", "Pick day", "Pick range")

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
            0 -> {
                txtViewCustomDate.visibility = View.VISIBLE
                txtViewCustomDate.text = getTodaysDate()
                currentlyPickedTime = TimeReference.THIS_DAY
                setupPieChart()
            }
            1 -> {
                txtViewCustomDate.visibility = View.INVISIBLE
                currentlyPickedTime = TimeReference.THIS_MONTH
                setupPieChart()

            }
            2 -> {
                val calendarView = CalendarPopup(this, false)
                calendarView.show()

                calendarView.setOnDismissListener {
                    if (calendarView.userSetTheDate) {
                        txtViewCustomDate.visibility = View.VISIBLE
                        txtViewCustomDate.text = calendarView.finalStartDay

                        startDate = calendarView.finalStartDay
                        currentlyPickedTime = TimeReference.SOME_DAY

                        setupPieChart()
                    }
                }

            }
            3 -> {
                val calendarView = CalendarPopup(this, true)
                calendarView.show()

                calendarView.setOnDismissListener {
                    if (calendarView.userSetTheDate) {
                        txtViewCustomDate.visibility = View.VISIBLE
                        val dateString = "${calendarView.finalStartDay} - ${calendarView.finalEndDay}"
                        txtViewCustomDate.text = dateString


                        startDate = calendarView.finalStartDay
                        endDate = calendarView.finalEndDay
                        currentlyPickedTime = TimeReference.SOME_RANGE


                        setupPieChart()
                    }
                }
            }
        }


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


    private fun setupPieChart() {

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

    }


    private fun addPieData() {


        pieEntries.clear()

        for (item in arrayOfExpenses) {

            if (currentlyPickedTime == TimeReference.THIS_DAY && item.date == getTodaysDate()) {
                addPickedData(item)
            } else if (currentlyPickedTime == TimeReference.THIS_MONTH && (getThisMonthAndThisYear(item.date)) == (getThisMonthAndThisYear(getTodaysDate()))) {
                addPickedData(item)
            } else if (currentlyPickedTime == TimeReference.SOME_DAY && item.date == startDate)
                addPickedData(item)
            else if (currentlyPickedTime == TimeReference.SOME_RANGE && checkIfTheDateIsInTheBetweenOfTwoDates(item.date, startDate, endDate)) {
                // AKO JE DATUM JEDNAK ILI VECI OD START DATE I
                // JEDNAK ILI MANJI OD END DATE
                // ONDA GA UPISUJEMO
                addPickedData(item)
            }
        }

    }


    private fun addPickedData(item: ExpenseItem) {
        var itemExists = false
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

        if(pieEntries.size == 0)
        {

        }

    }

    /*private fun filterPieData() {

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

    }*/



/*    private fun setupBarChart(){


        val arrayListOfBarEntries : ArrayList<BarEntry> = ArrayList()

        arrayListOfBarEntries.add(BarEntry(945f, 0f))
        arrayListOfBarEntries.add(BarEntry(1040f, 1f))
        arrayListOfBarEntries.add(BarEntry(1133f, 2f))
        arrayListOfBarEntries.add(BarEntry(1240f, 3f))
        arrayListOfBarEntries.add(BarEntry(1369f, 4f))
        arrayListOfBarEntries.add(BarEntry(1487f, 5f))
        arrayListOfBarEntries.add(BarEntry(1501f, 6f))
        arrayListOfBarEntries.add(BarEntry(1645f, 7f))
        arrayListOfBarEntries.add(BarEntry(1578f, 8f))
        arrayListOfBarEntries.add(BarEntry(1695f, 9f))

        val arrayListOfBarLabels : ArrayList<String> = ArrayList()

        arrayListOfBarLabels.add("2008")
        arrayListOfBarLabels.add("2009")
        arrayListOfBarLabels.add("2010")
        arrayListOfBarLabels.add("2011")
        arrayListOfBarLabels.add("2012")
        arrayListOfBarLabels.add("2013")
        arrayListOfBarLabels.add("2014")
        arrayListOfBarLabels.add("2015")
        arrayListOfBarLabels.add("2016")
        arrayListOfBarLabels.add("2017")

        val barDataSet= BarDataSet(arrayListOfBarEntries, "No Of Employee")
        barChart.animateY(5000)
        val data = BarData(barDataSet)
        //data = object : IBarDataSet(){}
        barDataSet.setBarBorderWidth(10f);
        barDataSet.colors = arrayListOfColors.toMutableList()
        barChart.data = data
       // barChart.setFitBars(true);
       *//* val day = arrayOf("hah,agaha,haha", "jaja", "hahas")
        val xAxis = barChart.xAxis
        xAxis.valueFormatter = LabelFormatter(day) as ValueFormatter
        xAxis.setGranularity(1f)*//*


        //val xAxis = barChart.getXAxis();
        //xAxis.valueFormatter = IndexAxisValueFormatter(arrayOf("hah,agaha,haha", "jaja", "hahas"));

        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM;
        val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jasn", "Fesb", "Masr", "Apsr")
        val formatter = IndexAxisValueFormatter(months);
        xAxis.setGranularity(70f);
        xAxis.setValueFormatter(formatter);
        xAxis.textSize = 10f
        xAxis.textColor = Color.WHITE

        barChart.invalidate()
    }*/


    private fun setupBarChart() {

        val arrayListOfBarEntriesEntries: ArrayList<BarEntry> = ArrayList()
        arrayListOfBarEntriesEntries.add(BarEntry(1f, 50f))
        arrayListOfBarEntriesEntries.add(BarEntry(2f, 330f))
        arrayListOfBarEntriesEntries.add(BarEntry(3f, 320f))
        arrayListOfBarEntriesEntries.add(BarEntry(4f, 230f))
        arrayListOfBarEntriesEntries.add(BarEntry(5f, 430f))
        arrayListOfBarEntriesEntries.add(BarEntry(6f, 390f))


        val arrayListOfBarLabels: ArrayList<String> = ArrayList()
        arrayListOfBarLabels.add("day1")
        arrayListOfBarLabels.add("day2")
        arrayListOfBarLabels.add("day3")
        arrayListOfBarLabels.add("day4")
        arrayListOfBarLabels.add("day5")
        arrayListOfBarLabels.add("day6")


        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(true)
        barChart.description.isEnabled = false
        barChart.setPinchZoom(false)
        barChart.setBorderColor(Color.WHITE)
        barChart.setGridBackgroundColor(Color.WHITE)
        barChart.setDrawGridBackground(false)
        barChart.axisRight.isEnabled = false
        //barChart.axisLeft.isEnabled = false


        val yAxis = barChart.axisLeft
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        yAxis.granularity = 1f
        yAxis.isGranularityEnabled = true
        yAxis.textColor = Color.WHITE
        yAxis.gridColor = Color.WHITE


        val xAxis = barChart.xAxis
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(true)
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.valueFormatter = IndexAxisValueFormatter(arrayListOfBarLabels)
        xAxis.textColor = Color.WHITE
        xAxis.gridColor = Color.WHITE


        val yValues = ArrayList<BarEntry>()

        // for (i in 0 until entries.size) {
        yValues.addAll(arrayListOfBarEntriesEntries)
        // }


        val barDataSet: BarDataSet

        if (barChart.data != null && barChart.data.dataSetCount > 0) {
            barDataSet = barChart.data.getDataSetByIndex(0) as BarDataSet
            barDataSet.values = yValues
            barChart.data.notifyDataChanged()
            barChart.notifyDataSetChanged()
        } else {
            // create 2 datasets with different types
            barDataSet = BarDataSet(yValues, "SCORE")
            barDataSet.color = Color.rgb(255, 204, 0)
            barDataSet.color = arrayListOfColors.toMutableList()[0]

            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(barDataSet)

            val data = BarData(dataSets)
            barChart.data = data
        }

        barDataSet.valueTextColor = Color.WHITE
        //barDataSet.color = Color.WHITE


        barChart.setFitBars(true)

        val legendBarChart = barChart.legend
        legendBarChart.formSize = 12f // set the size of the legend forms/shapes
        legendBarChart.form = Legend.LegendForm.CIRCLE // set what type of form/shape should be used
        legendBarChart.orientation = Legend.LegendOrientation.HORIZONTAL
        legendBarChart.textSize = 10f
        legendBarChart.textColor = Color.WHITE
        legendBarChart.xEntrySpace = 5f // set the space between the legend entries on the x-axis
        legendBarChart.yEntrySpace = 5f // set the space between the legend entries on the y-axis


        barChart.invalidate()
        barChart.animateY(2000)
    }


   /* private fun setupBarChart() {

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


        //val xAxis = barChart.xAxis
        //xAxis.position = XAxisPosition.BOTTOM
        //xAxis.textSize = 10f
        //xAxis.textColor = Color.WHITE
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
         barChart.setBackgroundColor(Color.rgb(255, 255, 255))
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
    }*/


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


    private fun getThisMonthAndThisYear(dateString: String): String {
        return dateString.substring(3, dateString.length)
    }


    @SuppressLint("SimpleDateFormat")
    private fun checkIfTheDateIsInTheBetweenOfTwoDates(dateForChecking: String, startDate: String, endDate: String): Boolean {

        val formatter: DateFormat = SimpleDateFormat("dd.MM.yyyy")

        val dateToCheck = formatter.parse(dateForChecking) as Date
        val startDateToCheck = formatter.parse(startDate) as Date
        val endDateToCheck = formatter.parse(endDate) as Date

        return dateToCheck in startDateToCheck..endDateToCheck


/*
        val dateForCheckingDAY = dateForChecking.substring(0,2).toInt()
        val dateForCheckingMONTH = dateForChecking.substring(3,5).toInt()
        val dateForCheckingYEAR = dateForChecking.substring(6,10).toInt()

        val startDateDAY = startDate.substring(0,2).toInt()
        val startDateMONTH = startDate.substring(3,5).toInt()
        val startDateYEAR = startDate.substring(6,10).toInt()

        val endDateDAY = endDate.substring(0,2).toInt()
        val endDateMONTH = endDate.substring(3,5).toInt()
        val endDateYEAR = endDate.substring(6,10).toInt()


        if(dateForCheckingYEAR in startDateYEAR..endDateYEAR)
        {
            if(dateForCheckingMONTH in startDateMONTH..endDateMONTH) {
                if (dateForCheckingDAY in startDateDAY..endDateDAY) {
                    return true
                } else if (dateForCheckingDAY < startDateDAY)
                {
                    if(dateForCheckingMONTH == startDateMONTH)
                        return false
                    else if(dateForCheckingMONTH == endDateMONTH)
                        if(dateForCheckingDAY <= endDateDAY)
                            return true
                    else if (dateForCheckingMONTH < endDateMONTH)
                        {return true}
                }
                else if(dateForCheckingDAY > endDateDAY)
                {
                    if(dateForCheckingMONTH == endDateMONTH)
                        return false
                    else if (dateForCheckingMONTH < endDateMONTH)
                }

            }
            else
                return false
        }
        else
            return false

        Log.i("Marija", "$dateForCheckingDAY : $dateForCheckingMONTH : $dateForCheckingYEAR"  )

        return true*/
    }
}