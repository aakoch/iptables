package com.adamkoch.iptables.matches

/**
 * Combination of [Match]es.
 *
 * Example: {@code val aggregatedMatch = AggregatedMatch(listOf(dateTimeMatch, protocolMatch, ipDestMatch))}
 *
 * @author aakoch
 * @since 0.1.0
 */
open class AggregatedMatch(val matchList: List<Match>) : Match {
    override val weight: Int = 50

    override fun asString(): String {
        return matchList.sorted().joinToString(" ", transform = Match::asString)
    }
}