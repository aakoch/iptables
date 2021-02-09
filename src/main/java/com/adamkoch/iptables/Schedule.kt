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
class Schedule {
    private val dayOfWeekSchedules: MutableList<DayOfWeekSchedule>
    fun add(dayOfWeekSchedule: DayOfWeekSchedule) {
        dayOfWeekSchedules.add(dayOfWeekSchedule)
    }

    fun getDaySchedules(): List<DayOfWeekSchedule> {
        return dayOfWeekSchedules
    }

    override fun toString(): String {
//        return daySchedules.joinToString()
        val sj = StringJoiner(", ")
        dayOfWeekSchedules.forEach(Consumer { dayOfWeekSchedule: DayOfWeekSchedule -> sj.add(dayOfWeekSchedule.toString()) })
        return sj.toString()
    }

    init {
        dayOfWeekSchedules = ArrayList()
    }
}