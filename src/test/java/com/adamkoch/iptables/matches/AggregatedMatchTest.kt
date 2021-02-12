package com.adamkoch.iptables.matches

import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

internal class AggregatedMatchTest {

    /**
     * Heaviest weight should go to the left
     */
    @Test
    fun weight() {
        val negativeMatch : Match = object : Match {
            override val weight: Int = -10
            override fun asString(): String {
                return "match1"
            }
        }
        val positiveMatch : Match = object : Match {
            override val weight: Int = 10
            override fun asString(): String {
                return "match2"
            }
        }
        val zeroMatch : Match = object : Match {
            override val weight: Int = 0
            override fun asString(): String {
                return "match3"
            }
        }
        val aggregatedMatch = AggregatedMatch(listOf(negativeMatch, positiveMatch, zeroMatch))
        assertEquals("match2 match3 match1", aggregatedMatch.asString())
    }
}