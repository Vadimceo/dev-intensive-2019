package ru.skillbranch.devintensive.extensions

import java.lang.IllegalStateException
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern:String="HH:mm:ss dd:MM:yy"):String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value:Int, units: TimeUnits = TimeUnits.SECOND) : Date {
    var time = this.time

    time +=when(units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }

    this.time = time
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val diffTime = this.time - date.time

    return when {
        abs(diffTime / SECOND) in 0..1 -> "только что"
        abs(diffTime / SECOND) in 1..45 -> "несколько секунд назад"
        abs(diffTime / SECOND) in 45..75 -> "минуту назад"
        abs(diffTime / MINUTE) in 45..75 -> "час назад"
        abs(diffTime / HOUR)   in 22..26 -> "день назад"
        diffTime / DAY > 360  -> "более чем через год"
        diffTime / DAY < -360 -> "более года назад"
        else -> getComplexMesage(diffTime)
    }
}
fun getComplexMesage(diffTime: Long): String {
    val answer = when {
        abs(diffTime / HOUR) in 26 .. 360*24 -> "${abs(diffTime / DAY)} ${getUnits(diffTime / DAY, TimeUnits.DAY)}"
        abs(diffTime / MINUTE) in 75 .. 22*60 -> "${abs(diffTime / HOUR)} ${getUnits(diffTime / HOUR, TimeUnits.HOUR)}"
        abs(diffTime / SECOND) in 75 .. (45*60) -> "${abs(diffTime / MINUTE)} ${getUnits(diffTime / MINUTE, TimeUnits.MINUTE)}"
        else -> "${abs(diffTime / SECOND)} ${getUnits(diffTime / SECOND, TimeUnits.SECOND)}"
    }

    return when {
        diffTime > 0L -> "через $answer"
        diffTime < 0L -> "$answer назад"
        else -> "непонятно"
    }
}

fun getUnits(l: Long, unit: TimeUnits): String {
    return when (abs(l % 10).toInt()) {
        1 -> if (unit == TimeUnits.DAY) "день" else if (unit == TimeUnits.HOUR) "час" else if (unit == TimeUnits.MINUTE) "минуту" else if (unit == TimeUnits.SECOND) "секунду" else ""
        2, 3, 4 -> if (unit == TimeUnits.DAY) "дня" else if (unit == TimeUnits.HOUR) "часа" else if (unit == TimeUnits.MINUTE) "минуты" else if (unit == TimeUnits.SECOND) "секунды" else ""
        5, 6, 7, 8, 9, 0 -> if (unit == TimeUnits.DAY) "дней" else if (unit == TimeUnits.HOUR) "часов" else if (unit == TimeUnits.MINUTE) "минут" else if (unit == TimeUnits.SECOND) "секунд" else ""
        else -> ""
    }
}

enum class TimeUnits{
    SECOND,
    MINUTE,
    HOUR,
    DAY
}