package com.adamkoch.iptables.matches

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

internal class DestinationMatchTest {
    @Test
    fun asString() {
        assertEquals( "-d 127.0.0.1/32", DestinationMatch("127.0.0.1/32").asString())
    }
}