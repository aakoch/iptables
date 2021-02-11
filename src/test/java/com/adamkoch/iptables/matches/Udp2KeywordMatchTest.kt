package com.adamkoch.iptables.matches

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Udp2KeywordMatchTest {
    @Test
    fun test() {
        val match = Udp2KeywordMatch("discord", "127.0.0.1")
        assertEquals("-d 127.0.0.1 -i br0 -p udp -m udp --dport 53 -m string --string \"discord\" --algo bm --to 65535 --icase", match.asString())
    }
}