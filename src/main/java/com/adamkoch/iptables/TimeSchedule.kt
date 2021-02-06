package com.adamkoch.iptables

import com.adamkoch.annotations.Unstable
import java.util.*
import java.util.function.Consumer

/**
 *
 * @since 0.1.0
 * @author aakoch
 */
@Unstable
class TimeSchedule {
    private val timeRanges: MutableList<TimeRange>
    fun add(daySchedule: TimeRange) {
        timeRanges.add(daySchedule)
    }

    fun getTimeRanges(): List<TimeRange> {
        return timeRanges
    }

    override fun toString(): String {
        val sj = StringJoiner(", ")
        timeRanges.forEach(Consumer { daySchedule: TimeRange -> sj.add(daySchedule.toString()) })
        return sj.toString()
    }

    init {
        timeRanges = ArrayList()
    }
}