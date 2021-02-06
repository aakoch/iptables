package com.adamkoch.iptables

import java.util.*

/**
 *
 * @since 0.1.0
 * @author aakoch
 */
class DaySchedule(val day: String) {
    private val timeRanges: MutableList<TimeRange>
    private val marked = false
    fun add(timeRange: TimeRange) {
        timeRanges.add(timeRange)
    }

    fun getTimeRanges(): List<TimeRange> {
        timeRanges.sortWith({ range1: TimeRange, range2: TimeRange ->
            range2.endTime.compareTo(
                range1.startTime
            )
        })
        return timeRanges
    }

    override fun toString(): String {
        return day + timeRanges
    }

    init {
        timeRanges = ArrayList()
    }
}