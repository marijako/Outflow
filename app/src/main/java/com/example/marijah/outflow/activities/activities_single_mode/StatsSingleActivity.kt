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
import com.example.marijah.outflow.room_database.Expense
import com.example.marijah.outflow.room_database.ExpenseDatabase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
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


class StatsSingleActivity : Activity(), AdapterView.OnItemSelectedListener {


    private lateinit var typefaceOswald: Typeface
    private lateinit var arrayListOfColors: Array<Int>
    private val arrayOfExpenses: ArrayList<Expense> = ArrayList()

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
                setupBarChart()
            }
            1 -> {
                txtViewCustomDate.visibility = View.INVISIBLE
                currentlyPickedTime = TimeReference.THIS_MONTH
                setupPieChart()
                setupBarChart()

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
                        setupBarChart()

                        if(pieChart.visibility == View.VISIBLE)
                            barChart.visibility = View.INVISIBLE

                        if(barChart.visibility == View.VISIBLE)
                            pieChart.visibility = View.INVISIBLE

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
                        setupBarChart()


                        if(pieChart.visibility == View.VISIBLE)
                            barChart.visibility = View.INVISIBLE

                        if(barChart.visibility == View.VISIBLE)
                            pieChart.visibility = View.INVISIBLE
                    }
                }
            }

        }
        if(pieChart.visibility == View.VISIBLE)
            barChart.visibility = View.INVISIBLE

        if(barChart.visibility == View.VISIBLE)
            pieChart.visibility = View.INVISIBLE


    }

    // Defining the Callback methods here
    override fun onNothingSelected(arg0: AdapterView<*>) {}


    private fun setLayoutsAndListeners() {
        HelperManager.setTypefaceRegular(assets, txtViewName)
        //setupPieChart()


        // inicijalno podesavanje
        //txtViewCustomDate.visibility = View.VISIBLE
        //txtViewCustomDate.text = getTodaysDate()
        //currentlyPickedTime = TimeReference.THIS_DAY
        //setupPieChart()
        //setupBarChart()
        pieChart.visibility = View.VISIBLE
        barChart.visibility = View.INVISIBLE

        spinner.setSelection(0)


        setThePickedOneYellowAndTheOthersBlue(imgViewPieChart)
        setupPieChart()
        lineChart.visibility = View.INVISIBLE
        pieChart.visibility = View.VISIBLE
        barChart.visibility = View.INVISIBLE

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

        setThePickedOneYellowAndTheOthersBlue(imgViewPieChart)


        imgViewBack.setOnClickListener {
            finish()
        }

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
        dataSet.setDrawFilled(false)
        dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        dataSet.fillColor = Color.CYAN
        dataSet.color = Color.CYAN
        dataSet.fillAlpha = 255
        dataSet.setDrawCircles(true)

        dataSet.valueTextColor = Color.YELLOW

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        lineChart.axisRight.isEnabled = false

        val xAxis = lineChart.xAxis
        xAxis.textColor = Color.WHITE
        xAxis.typeface = typefaceOswald


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


    private fun addPickedData(item: Expense) {
        txtViewNoEntries.visibility = View.INVISIBLE

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

        if (pieEntries.size == 0) {
            txtViewNoEntries.visibility = View.VISIBLE
        }

    }


    private fun setupBarChart() {

        val arrayListOfBarEntriesEntries: ArrayList<BarEntry> = ArrayList()
        val arrayListOfBarLabels: ArrayList<String> = ArrayList()

        addPieData()

        for (i in 0 until pieEntries.size) {
            arrayListOfBarLabels.add(i, pieEntries[i].label)
            arrayListOfBarEntriesEntries.add(BarEntry(i.toFloat(), pieEntries[i].value))
        }

        // ako nema podataka
        if (arrayListOfBarEntriesEntries.isEmpty()) {
            barChart.visibility = View.INVISIBLE
            txtViewNoEntries.visibility = View.VISIBLE
            //return
        } else {
            barChart.visibility = View.VISIBLE
            txtViewNoEntries.visibility = View.INVISIBLE
        }


        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(true)
        barChart.setNoDataTextTypeface(typefaceOswald)
        barChart.description.isEnabled = false
        barChart.setPinchZoom(false)
        barChart.setFitBars(true)
        barChart.setBorderColor(Color.WHITE)
        barChart.setDrawGridBackground(false)
        barChart.axisRight.isEnabled = false


        val yAxis = barChart.axisLeft
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        yAxis.granularity = 1f
        yAxis.setStartAtZero(true)
        yAxis.isGranularityEnabled = true
        yAxis.textColor = Color.WHITE
        yAxis.gridColor = Color.parseColor("#858585")
        yAxis.typeface = typefaceOswald

        val xAxis = barChart.xAxis
        xAxis.isEnabled = false
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = IndexAxisValueFormatter(arrayListOfBarLabels)
        xAxis.textColor = Color.WHITE
        xAxis.gridColor = Color.WHITE
        /* xAxis.granularity = 0.5f
         xAxis.isGranularityEnabled = true
         xAxis.setCenterAxisLabels(true)
         xAxis.setDrawGridLines(false)
         xAxis.position = XAxis.XAxisPosition.TOP
         xAxis.granularity = 0.5f
         xAxis.isGranularityEnabled = true
         */

        val yValues = ArrayList<BarEntry>()
        yValues.addAll(arrayListOfBarEntriesEntries)


        val dataSets: ArrayList<IBarDataSet> = ArrayList<IBarDataSet>()
        for ((i, barEntry) in yValues.withIndex()) {
            val arrayListOfBarEntry = arrayListOf(barEntry)
            val barDataSet = BarDataSet(arrayListOfBarEntry, arrayListOfBarLabels[i])
            barDataSet.color = arrayListOfColors.toMutableList()[i]
            barDataSet.valueTextColor = Color.CYAN
            barDataSet.valueTypeface = typefaceOswald
            barDataSet.valueTextSize = 10f
            dataSets.add(barDataSet)
        }
        val data = BarData(dataSets)
        barChart.data = data


        val legendBarChart = barChart.legend
        //legendBarChart.formSize = 12f // set the size of the legend forms/shapes
        legendBarChart.form = Legend.LegendForm.CIRCLE // set what type of form/shape should be used
        legendBarChart.orientation = Legend.LegendOrientation.HORIZONTAL
        legendBarChart.textSize = 10f
        legendBarChart.typeface = typefaceOswald
        legendBarChart.textColor = Color.WHITE
        legendBarChart.isWordWrapEnabled = true

        barChart.invalidate()
        barChart.animateY(2000)
    }


    /**
     * Funkcija za preuzimanje troskova iz firebase realtime database baze i smestanje u array listu troskova.
     */
    private fun getArrayListOfExpenses() {

        val expenseDatabase = ExpenseDatabase.getInstance(this)
        for (expense in expenseDatabase!!.expenseDao().expenseList) {
            arrayOfExpenses.add(expense)
        }
    }


    /**
     * Funkcija za dobijanje danasnjeg dana.
     */
    @SuppressLint("SimpleDateFormat")
    private fun getTodaysDate(): String {

        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd.MM.yyyy.")
        return df.format(c)
    }


    /**
     * Funkcija za dobijanje danasnjeg dana i godine.
     */
    private fun getThisMonthAndThisYear(dateString: String): String {
        return dateString.substring(3, dateString.length)
    }


    /**
     * Funkcija za proveravanje da li je jedan datum izmedju dva datuma.
     */
    @SuppressLint("SimpleDateFormat")
    private fun checkIfTheDateIsInTheBetweenOfTwoDates(dateForChecking: String, startDate: String, endDate: String): Boolean {

        val formatter: DateFormat = SimpleDateFormat("dd.MM.yyyy")

        val dateToCheck = formatter.parse(dateForChecking) as Date
        val startDateToCheck = formatter.parse(startDate) as Date
        val endDateToCheck = formatter.parse(endDate) as Date

        return dateToCheck in startDateToCheck..endDateToCheck
    }
}