package com.example.marijah.outflow.popups

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.applikeysolutions.cosmocalendar.selection.RangeSelectionManager
import com.applikeysolutions.cosmocalendar.utils.SelectionType
import com.example.marijah.outflow.R
import kotlinx.android.synthetic.main.popup_calendar.*


class CalendarPopup(private var activity: Activity, private val isRangePicked: Boolean) : DimmedPopupDialog(activity, R.layout.popup_calendar) {


    var userSetTheDate = false
    var finalStartDay = ""
    var finalEndDay = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        calendarView.calendarOrientation = 1
        if (isRangePicked)
            calendarView.selectionType = SelectionType.RANGE
        else {
            calendarView.selectionType = SelectionType.SINGLE


        }

        /* calendarView.selectionManager = object : RangeSelectionManager() {
             override fun toggleDay(@NonNull day: Day) {
                 Log.i("Ok", "Hello $day")
             }

             override fun isDaySelected(@NonNull day: Day): Boolean {
                 return false
             }

             override fun clearSelections() {

             }
         }
 */

        imgViewSetDate.setOnClickListener {

            if (isRangePicked) {
                if (calendarView.selectionManager is RangeSelectionManager) {
                    val rangeSelectionManager = calendarView.selectionManager as RangeSelectionManager
                    if (rangeSelectionManager.days != null) {

                        // uzimamo pocetni datum
                        val startDate = rangeSelectionManager.days.first!!

                        // kastujemo ga u nama zgodan prikaz
                        val startDay: String = if (startDate.dayNumber > 9)
                            startDate.dayNumber.toString()
                        else
                            "0${startDate.dayNumber}"
                        val startMonth: String = getMonthNumber(startDate.toString().substring(12, 15))
                        val startYear: String = startDate.toString().substring(startDate.toString().length - 5, startDate.toString().length - 1)
                        finalStartDay = "$startDay.$startMonth.$startYear."


                        // uzimamo krajnji datum
                        val endDate = rangeSelectionManager.days.second!!
                        // kastujemo ga u nama zgodan prikaz
                        val endDay: String = if (endDate.dayNumber > 9)
                            endDate.dayNumber.toString()
                        else
                            "0${endDate.dayNumber}"
                        val endMonth: String = getMonthNumber(endDate.toString().substring(12, 15))
                        val endYear: String = endDate.toString().substring(endDate.toString().length - 5, endDate.toString().length - 1)
                        finalEndDay = "$endDay.$endMonth.$endYear."



                        Log.i("Marija",
                                "\n finalStartDay $finalStartDay  " +
                                "\n finalEndDay $finalEndDay \n")


                        userSetTheDate = true
                        dismiss()
                    } else {

                        userSetTheDate = false
                        Toast.makeText(activity.baseContext, "Invalid selection.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                val selectedDaysList = calendarView.selectedDays
                if (selectedDaysList != null && selectedDaysList.size > 0) {
                    Log.i("Marija", "startDate: ${selectedDaysList[0]} i endDate")

                    // uzimamo datum
                    val startDate = selectedDaysList[0]

                    // kastujemo ga u nama zgodan prikaz
                    val startDay: String = if (startDate.dayNumber > 9)
                        startDate.dayNumber.toString()
                    else
                        "0${startDate.dayNumber}"
                    val startMonth: String = getMonthNumber(startDate.toString().substring(12, 15))
                    val startYear: String = startDate.toString().substring(startDate.toString().length - 5, startDate.toString().length - 1)
                    finalStartDay = "$startDay.$startMonth.$startYear."


                    Log.i("Marija", "finalStartDay: $finalStartDay")

                    userSetTheDate = true
                    dismiss()
                } else {
                    Toast.makeText(activity.baseContext, "Invalid selection.", Toast.LENGTH_SHORT).show()

                }
            }

        }


        imgViewCancel.setOnClickListener {
            userSetTheDate = false
            dismiss() }

    }


    @SuppressLint("DefaultLocale")
    private fun getMonthNumber(month: String): String {

        return when (month.toLowerCase()) {
            "jan" -> {
                "01"
            }
            "feb" -> "02"
            "mar" -> "03"
            "apr" -> "04"
            "may" -> "05"
            "jun" -> "06"
            "jul" -> "07"
            "aug" -> "08"
            "sep" -> "09"
            "oct" -> "10"
            "nov" -> "11"
            "dec" -> "12"
            else -> "00"

        }
    }

}
