package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext

import java.text.*
import java.util.*
import java.util.concurrent.*

var fileSizeUnits = arrayOf("bytes", "KB", "MB", "GB")

fun Long.formatSizeTwoDecimal(): String {
    var sizeToReturn = ""
    var bytes = this.toDouble()
    var index = 0
    while (index < fileSizeUnits.size) {
        if (bytes < 1024) {
            break
        }
        bytes /= 1024
        index++
    }
    sizeToReturn = "%.2f".format(bytes) + " " + fileSizeUnits.get(index)
    return sizeToReturn
}

fun Long.fromMilliToDate(): String {
    val dateFormat = "MMM dd, yyyy hh:mm a"
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    val calendar = Calendar.getInstance()

    calendar.timeInMillis = this
    return formatter.format(calendar.time)
}

fun Long.getTimeAgo(): String {
    val now = System.currentTimeMillis()
    val diff = now - this
    val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val days = TimeUnit.MILLISECONDS.toDays(diff)

    return when {
        seconds < 60 -> "just now"
        minutes < 60 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
        hours < 24 -> "$hours hour${if (hours > 1) "s" else ""} ago"
        days < 7 -> "$days day${if (days > 1) "s" else ""} ago"
        days < 30 -> "${days / 7} week${if (days / 7 > 1) "s" else ""} ago"
        days < 365 -> "${days / 30} month${if (days / 30 > 1) "s" else ""} ago"
        else -> "${days / 365} year${if (days / 365 > 1) "s" else ""} ago"
    }
}