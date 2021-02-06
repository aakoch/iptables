package com.adamkoch.iptables

import com.adamkoch.annotations.Unstable
import java.util.*

/**
 *
 * @since 0.1.0
 * @author aakoch
 */
@Unstable
class TimeRangeToDayMap(val timeRange: TimeRange) {
    private val days: MutableSet<String>
    fun addDay(day: String) {
        days.add(day)
    }

    fun getDays(): List<String> {
        val dayList: MutableList<String> = ArrayList()
        if (days.contains("Mon")) {
            dayList.add("Mon")
        }
        if (days.contains("Tue")) {
            dayList.add("Tue")
        }
        if (days.contains("Wed")) {
            dayList.add("Wed")
        }
        if (days.contains("Thu")) {
            dayList.add("Thu")
        }
        if (days.contains("Fri")) {
            dayList.add("Fri")
        }
        return dayList
    }

    val daysString: String
        get() = java.lang.String.join(",", getDays())

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as TimeRangeToDayMap
        return timeRange == that.timeRange && days == that.days
    }

    override fun hashCode(): Int {
        return Objects.hash(timeRange, days)
    }

    override fun toString(): String {
        return "TimeRangeMap{" +
                "timeRange=" + timeRange +
                ", days=" + daysString +
                '}'
    }

    init {
        days = HashSet()
    }
}