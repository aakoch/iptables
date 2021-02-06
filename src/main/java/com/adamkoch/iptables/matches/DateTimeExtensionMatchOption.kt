package com.adamkoch.iptables.matches

import java.time.Instant
import java.time.temporal.ChronoField
import java.time.temporal.Temporal
import java.util.*

abstract class DateTimeExtensionMatchOption protected constructor(protected val temporal: Temporal) :
    GenericExtensionMatchOption(Arrays.asList("--date", "--time")) {
    private var useKernelTZ = true
    fun setUseKernelTZ(newValue: Boolean) {
        useKernelTZ = newValue
    }

    protected fun asString(startOrStop: Boolean): String {
        val type: String
        type = if (temporal.isSupported(ChronoField.YEAR) || temporal is Instant) {
            "date"
        } else {
            "time"
        }
        val temporalString = temporal.toString()
        val sb = StringBuilder()
        sb.append("--").append(type)
        if (startOrStop) {
            sb.append("start ")
        } else {
            sb.append("stop ")
        }
        sb.append(temporalString)
        if (temporalString.endsWith("Z") && useKernelTZ) {
            sb.deleteCharAt(sb.length - 1)
            sb.append(" --kerneltz")
        }
        return sb.toString()
    }
}