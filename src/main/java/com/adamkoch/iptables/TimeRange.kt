package com.adamkoch.iptables

import com.adamkoch.annotations.Unstable
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 *
 * @since 0.1.0
 * @author aakoch
 */
@Unstable
class TimeRange(val startTime: LocalTime, val endTime: LocalTime) {
    fun getStartTimeString(): String {
        return startTime.format(FORMATTER)
    }

    fun getEndTimeString(): String {
        return endTime.format(FORMATTER)
    }

    override fun toString(): String {
        return getStartTimeString() + "-" + getEndTimeString()
    }

    companion object {
        val FORMATTER = DateTimeFormatter.ofPattern("HH:mm")
    }
}