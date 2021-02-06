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
    private val daySchedules: MutableList<DaySchedule>
    fun add(daySchedule: DaySchedule) {
        daySchedules.add(daySchedule)
    }

    fun getDaySchedules(): List<DaySchedule> {
        return daySchedules
    }

    override fun toString(): String {
        val sj = StringJoiner(", ")
        daySchedules.forEach(Consumer { daySchedule: DaySchedule -> sj.add(daySchedule.toString()) })
        return sj.toString()
    }

    init {
        daySchedules = ArrayList()
    }
}