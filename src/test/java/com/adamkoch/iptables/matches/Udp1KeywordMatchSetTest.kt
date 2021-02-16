package com.adamkoch.iptables.matches

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Udp1KeywordMatchSetTest {
    @Test
    fun test() {
        val match = Udp1KeywordMatchSet("discord").getMatches().joinToString(" ", transform = Match::asString)
        assertEquals("-i br0 -p udp -m udp --dport 53 -m string --string \"discord\" --algo bm --to 65535 --icase", match)
    }
}