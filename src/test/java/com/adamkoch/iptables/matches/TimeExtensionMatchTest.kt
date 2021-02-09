package com.adamkoch.iptables.matches

import com.adamkoch.iptables.DayOfWeekSchedule
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.*

internal class TimeExtensionMatchTest {
    @Test
    fun asString() {
        val match = TimeExtensionMatch()
        Assertions.assertEquals("-m time", match.asString())
    }

    @Test
    fun withStartDate() {
        val match = TimeExtensionMatch()
        match.setStart(LocalDate.of(2020, 4, 4))
        Assertions.assertEquals("-m time --datestart 2020-04-04", match.asString())
    }

    @Test
    fun withEndDate() {
        val match = TimeExtensionMatch()
        match.setEnd(LocalDate.of(2020, 4, 4))
        Assertions.assertEquals("-m time --datestop 2020-04-04", match.asString())
    }

    @Test
    fun withStartAndEndDate() {
        val match = TimeExtensionMatch()
        match.setStart(LocalDate.of(2020, 4, 4))
        match.setEnd(LocalDate.of(2020, 4, 4))
        Assertions.assertEquals(
            "-m time --datestart 2020-04-04 --datestop 2020-04-04",
            match.asString()
        )
    }

    @Test
    fun withStartTime() {
        val match = TimeExtensionMatch()
        match.setStart(LocalTime.of(4, 4, 4))
        Assertions.assertEquals("-m time --timestart 04:04:04", match.asString())
    }

    @Test
    fun withEndTime() {
        val match = TimeExtensionMatch()
        match.setEnd(LocalTime.of(4, 4, 4))
        Assertions.assertEquals("-m time --timestop 04:04:04", match.asString())
    }

    @Test
    fun withStartAndEndTime() {
        val match = TimeExtensionMatch()
        match.setStart(LocalTime.of(5, 5, 5))
        match.setEnd(LocalTime.of(4, 4, 4))
        Assertions.assertEquals(
            "-m time --timestart 05:05:05 --timestop 04:04:04",
            match.asString()
        )
    }

    @Test
    fun withContiguous() {
        val match = TimeExtensionMatch()
        match.setStart(LocalTime.of(5, 5, 5))
        match.setEnd(LocalTime.of(4, 4, 4))
        match.setContiguous()
        Assertions.assertEquals(
            "-m time --timestart 05:05:05 --timestop 04:04:04 --contiguous",
            match.asString()
        )
    }

    @Test
    fun withStartDateTime() {
        val match = TimeExtensionMatch()
        match.setStart(LocalDateTime.of(2020, 5, 6, 7, 8, 9))
        Assertions.assertEquals("-m time --datestart 2020-05-06T07:08:09", match.asString())
    }

    @Test
    fun withWeekdaysTime() {
        val match = TimeExtensionMatch()
        match.days =
            arrayOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)
        Assertions.assertEquals("-m time --weekdays Mon,Tue,Thu,Fri", match.asString())
    }

    @Test
    fun withWeekDays() {
        val match = TimeExtensionMatch()
        match.days = DayOfWeekSchedule.WEEKDAYS
        Assertions.assertEquals("-m time --weekdays Mon,Tue,Wed,Thu,Fri", match.asString())
    }

    @Test
    fun withWeekends() {
        val match = TimeExtensionMatch()
        match.days = DayOfWeekSchedule.WEEKENDS
        Assertions.assertEquals("-m time --weekdays Sat,Sun", match.asString())
    }

    @Test
    fun withTimezone() {
        val match = TimeExtensionMatch()
        match.setStart(Instant.EPOCH)
        Assertions.assertEquals(
            "-m time --datestart 1970-01-01T00:00:00 --kerneltz",
            match.asString()
        )
    }
}