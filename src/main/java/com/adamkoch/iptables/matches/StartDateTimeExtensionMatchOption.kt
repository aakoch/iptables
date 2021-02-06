package com.adamkoch.iptables.matches

import java.time.temporal.Temporal
import com.adamkoch.iptables.matches.DateTimeExtensionMatchOption

class StartDateTimeExtensionMatchOption(startTemporal: Temporal) :
    DateTimeExtensionMatchOption(startTemporal) {
    override fun asString(): String {
        return super.asString(true)
    }
}