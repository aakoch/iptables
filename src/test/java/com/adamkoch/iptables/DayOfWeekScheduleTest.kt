package com.adamkoch.iptables

import com.adamkoch.iptables.DayOfWeekSchedule.Companion.WEEKDAYS
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.DayOfWeek
import java.time.LocalTime

internal class DayOfWeekScheduleTest {


    @Test
    fun test() {
        val startTime = LocalTime.MIN
        val endTime = LocalTime.MAX
        val timeRange = TimeRange(startTime, endTime)
        val daySchedule = DayOfWeekSchedule(WEEKDAYS.toSet() as Set<DayOfWeek>, timeRange)
        assertEquals("MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY at 00:00-23:59", daySchedule.toString())
    }

    @Test
    fun noonToMidnight() {
        val startTime = LocalTime.NOON
        val endTime = LocalTime.MIDNIGHT
        val timeRange = TimeRange(startTime, endTime)
        val daySchedule = DayOfWeekSchedule(WEEKDAYS.toSet() as Set<DayOfWeek>, timeRange)
        assertEquals("MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY at 12:00-00:00", daySchedule.toString())
    }

    @Test
    fun midnightMinusOneNanosecond() {
        val startTime = LocalTime.MIN
        val endTime = LocalTime.MIDNIGHT.minusNanos(1)
        val timeRange = TimeRange(startTime, endTime)
        val daySchedule = DayOfWeekSchedule(WEEKDAYS.toSet() as Set<DayOfWeek>, timeRange)
        assertEquals("MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY at 00:00-23:59", daySchedule.toString())
    }
}