package com.adamkoch.iptables.matches

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Udp2KeywordMatchSetTest {
    @Test
    fun test() {
        val match = Udp2KeywordMatchSet("discord", "127.0.0.1")
        assertEquals("-i br0 -p udp -m udp --dport 53 -d 127.0.0.1 -m string --string \"discord\" --algo bm --to 65535 --icase", match.asString())
    }
}