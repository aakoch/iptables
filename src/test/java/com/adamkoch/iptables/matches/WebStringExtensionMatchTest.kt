package com.adamkoch.iptables.matches

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class WebStringExtensionMatchTest {
    @Test
    fun cannotUseSpaces() {
        Assertions.assertThrows(
            IllegalArgumentException::class.java
        ) { WebStringExtensionMatch("adam is cool") }
    }

    @Test
    fun basicUse() {
        Assertions.assertEquals(
            "-m webstr --url pornhub",
            WebStringExtensionMatch("pornhub").asString()
        )
    }
}