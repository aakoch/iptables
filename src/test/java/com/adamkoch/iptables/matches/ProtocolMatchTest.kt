package com.adamkoch.iptables.matches

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ProtocolMatchTest {
    @Test
    fun test() {
        val protocolMatch = ProtocolMatch.Companion.TCP
        assertEquals("-p tcp", protocolMatch.asString())
    }
}