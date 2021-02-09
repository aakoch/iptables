package com.adamkoch.iptables

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class UtilTest {
    @Test
    fun test() {
        assertEquals("Joels_Test", Util.sanitize("Joel's Test"))
    }

    @Test
    fun doubleIt() {
        assertEquals("Joels_Test", Util.sanitize(Util.sanitize("Joel's Test")))
    }
}