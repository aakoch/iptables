package com.adamkoch.iptables.matches

import java.util.stream.Collectors

/**
 * Combination of [Match]es
 *
 * @author aakoch
 * @since 0.1.0
 */
open class AggregatedMatch(val matchList: List<Match>) : Match {

    override fun asString(): String {
        return matchList.stream().map { obj: Match -> obj.asString() }
            .collect(Collectors.joining(" "))
    }

//    init {
//        this.matches = ArrayList(matches)
//    }
}