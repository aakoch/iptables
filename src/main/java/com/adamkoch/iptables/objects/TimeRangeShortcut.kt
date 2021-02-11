package com.adamkoch.iptables.objects

import com.adamkoch.iptables.TimeRange
import java.time.LocalTime

class TimeRangeShortcut(
    startHour: Int,
    startMinute: Int,
    endHour: Int,
    endMinute: Int
) : TimeRange(
    LocalTime.of(startHour, startMinute),
    LocalTime.of(endHour, endMinute)) {

    }