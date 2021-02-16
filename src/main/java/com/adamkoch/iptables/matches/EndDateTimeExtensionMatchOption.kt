package com.adamkoch.iptables.matches

import java.time.temporal.Temporal
import com.adamkoch.iptables.matches.DateTimeExtensionMatchOption

class EndDateTimeExtensionMatchOption(endTemporal: Temporal) :
    DateTimeExtensionMatchOption(endTemporal) {
    override fun asString(): String {
        return super.asString(false)
    }

    override fun toString(): String {
        return "end[${temporal}]"
    }
}