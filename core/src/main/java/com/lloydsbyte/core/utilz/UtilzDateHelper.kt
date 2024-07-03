package com.lloydsbyte.core.utilz

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar

/**
 * Example of format 'MM/dd/yyyy'
 */
class UtilzDateHelper(val format: String) {
    companion object {
        //Pass these values in as the class format when you want to use it's format, add more if you need to!
        val DF_CAL = "MM/dd/yyyy"
        val DF_TIMEDATE = "h:mm a MM/dd/yyyy"

        //Time in Milliseconds to use with compareMillisDates.. in case you need them
        val TIMEINMILLIS_HOUR = 3600000L
        val TIMEINMILLIS_DAY = 86400000L
        val TIMEINMILLIS_WEEK = 604800000L
    }

    //The format passed into the class is used here to set the global variable for all formatting
    @SuppressLint("SimpleDateFormat")
    private val sdf = SimpleDateFormat(format)

    fun buildStrDate(): String {
        //Create date in string format using the passed in format
        val today  = Calendar.getInstance()
        return sdf.format(today.time)
    }

    fun buildMillisDate(): Long {
        //Simply create a time in millis using the systems time
        return System.currentTimeMillis()
    }


    @SuppressLint("SimpleDateFormat")
    fun convertDateFromMillis(timeInMillis: Long): String {
        //Convert a time from millis into a human readable using the format passed in
        val dateString = sdf.format(timeInMillis)
        return String.format( dateString)
    }



    fun compareMillisDates(firstDate: Long, distanceAccepted: Long): Boolean {
        //Returns true if time between days are longer than accepted
        return (buildMillisDate() - firstDate) < distanceAccepted
    }


    fun compareTodayDate(dateInStr: String): Boolean {
        //Returns true if date matches today's date
        return dateInStr == buildStrDate()
    }


}