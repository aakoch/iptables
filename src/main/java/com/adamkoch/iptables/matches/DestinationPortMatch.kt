package com.adamkoch.iptables.matches

import com.adamkoch.iptables.matches.GenericMatch
import java.lang.IllegalArgumentException
import com.adamkoch.iptables.matches.DestinationPortMatch

/**
 * Matches against a destination port.
 *
 * @author aakoch
 * @since 0.1.0
 */
class DestinationPortMatch(value: Int) : GenericMatch("--dport", Integer.toString(value)) {
    constructor(value: String) : this(value.toInt()) {}

    companion object {
        private fun requireWithinPortRange(value: Int) {
            require(!(value < 0 || value > 65535)) { "port \"$value\" is outside the allowed port range of 0 - 65535" }
        }
    }

    init {
        requireWithinPortRange(value)
    }
}