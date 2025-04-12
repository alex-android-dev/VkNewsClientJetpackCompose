package com.example.vknewsclient.data.mapper

import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

object MapTimestampToDate {

    fun mapTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp)
        return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }

}