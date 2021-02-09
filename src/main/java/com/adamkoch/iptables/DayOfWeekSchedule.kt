package com.adamkoch.iptables

import java.time.DayOfWeek
import java.util.*

/**
 * A representation of the range of times for a specified day of the week. For example,
 * 08:00 - 17:00 on Monday, Tuesday, Wednesday, Thursday, Friday
 *
 * @since 0.1.0
 * @author aakoch
 */
class DayOfWeekSchedule(val daysOfTheWeek: Set<DayOfWeek>, val timeRange: TimeRange) {
    /**
     * The output of this method is NOT suitable for usage in the iptables command.
     */
    override fun toString(): String {
        return daysOfTheWeek.joinToString(",") + " at " + timeRange.toString()
    }

    companion object {
        val WEEKENDS = arrayOf<DayOfWeek?>(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        val WEEKDAYS = arrayOf<DayOfWeek?>(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
        )
    }
}