package com.adamkoch.iptables.matches

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

internal class DestinationPortMatchTest {
    @Test
    fun outOfBoundsLow() {
        assertThrows(IllegalArgumentException::class.java, {
            DestinationPortMatch(-1)
        })
    }

    @Test
    fun outOfBoundsHigh() {
        assertThrows(IllegalArgumentException::class.java, {
            DestinationPortMatch(555555)
        })
    }

    @Test
    fun asString() {
        assertEquals( "-dport 12345", DestinationPortMatch("12345").asString())
    }
}