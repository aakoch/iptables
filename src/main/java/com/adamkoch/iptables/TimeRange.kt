package com.adamkoch.iptables

import com.adamkoch.annotations.Unstable
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Two [TimeRange]s, used for the [TimeMatch]
 *
 * @since 0.1.0
 * @author aakoch
 */
@Unstable
open class TimeRange(val startTime: LocalTime, val endTime: LocalTime) {

    /**
     * Returns the range in "HH:mm-HH:mm" format. Note it uses a 24-hour clock with midnight as 00:00.
     */
    override fun toString(): String {
        return startTime.format(FORMATTER) + "-" + endTime.format(FORMATTER)
    }

    fun asString(): String {
        return toString()
    }

    companion object {
        val FORMATTER : DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    }
}