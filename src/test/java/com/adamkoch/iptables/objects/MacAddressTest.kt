package com.adamkoch.iptables.objects

import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.RandomUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException
import java.util.*

internal class MacAddressTest {

    @Test
    fun valid() {
        MacAddress(MacAddress.DUMMY.toString())
    }

    @Test
    fun invalid() {
        m("ab:cd:00:\u0110\u0100:11")
        m("\u0001\u0002")
        m("\u0127")
        m(RandomStringUtils.random(3000))
    }

    private fun m(str: String) {
        assertThrows(IllegalArgumentException::class.java, {
            MacAddress(str)
        })
    }
}