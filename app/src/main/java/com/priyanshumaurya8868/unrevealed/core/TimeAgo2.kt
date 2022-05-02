package com.priyanshumaurya8868.unrevealed.core

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


fun String.covertToPostTimeText(): String? {
    var convTime: String? = null
    val prefix = ""
    val suffix = "Ago"
    try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val pasTime: Date = dateFormat.parse(this)
        val nowTime = Date()
        val dateDiff: Long = nowTime.time - pasTime.time
        val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
        val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
        val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
        val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
        when {
            second < 60 -> {
                convTime = "$second Seconds $suffix"
            }
            minute < 60 -> {
                convTime = "$minute Minutes $suffix"
            }
            hour < 24 -> {
                convTime = "$hour Hours $suffix"
            }
            day >= 7 -> {
                convTime = when {
                    day > 360 -> {
                        (day / 360).toString() + " Years " + suffix
                    }
                    day > 30 -> {
                        (day / 30).toString() + " Months " + suffix
                    }
                    else -> {
                        (day / 7).toString() + " Week " + suffix
                    }
                }
            }
            day < 7 -> {
                convTime = "$day Days $suffix"
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("ConvTimeE", e.message ?: "")
    }
    return convTime
}


fun String.covertToCommentTimeText(): String? {
    var convTime: String? = null
    try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val pasTime: Date = dateFormat.parse(this)
        val nowTime = Date()
        val dateDiff: Long = nowTime.time - pasTime.time
        val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
        val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
        val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
        val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
        when {
            second < 60 -> {
                convTime = "${second}s"
            }
            minute < 60 -> {
                convTime = "${minute}m"
            }
            hour < 24 -> {
                convTime = "$hour"+"h"
            }
            day >= 7 -> {
                convTime = when {
                    day > 360 -> {
                        (day / 360).toString() + "yr"
                    }
                    day > 30 -> {
                        (day / 30).toString() +"m"
                    }
                    else -> {
                        (day / 7).toString() + "w"
                    }
                }
            }
            day < 7 -> {
                convTime = "$day"+"d"
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("ConvTimeE", e.message ?: "")
    }
    return convTime
}
