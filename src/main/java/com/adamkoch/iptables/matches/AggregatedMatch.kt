package com.adamkoch.iptables.matches

import java.util.stream.Collectors

/**
 * Combination of [Match]es
 *
 * @author aakoch
 * @since 0.1.0
 */
open class AggregatedMatch(val matchList: List<Match>) : Match {
    override val rank: Int = 1

    override fun asString(): String {

        val comparator = Comparator { match1: Match, match2: Match -> match2.rank - match1.rank }

        return matchList.sortedWith(comparator).joinToString (" ", transform = Match::asString )

    }

//    init {
//        this.matches = ArrayList(matches)
//    }
}