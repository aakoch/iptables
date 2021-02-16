package com.adamkoch.iptables.objects

import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

internal class MacAddressTest {
    private companion object {
        @JvmStatic
        fun validAddresses() = arrayOf(
            MacAddress.DUMMY.toString(),
            "3D-F2-C9-A6-B3:4F",
            "3DF-2C9-A6B-34F",
            "3D.F2.C9.A6.B3.4F"
        )

        @JvmStatic
        fun invalidAddresses() = arrayOf(
            "\u0001\u0002",
            "\u0127",
            "",
            RandomStringUtils.random(30)
        )
    }

    @ParameterizedTest(name = "\"{0}\" is a valid MAC address and should not throw an exception")
    @MethodSource("validAddresses")
    fun parameterizedValid(macAddress: String) {
        MacAddress(macAddress)
    }

    @ParameterizedTest(name = "\"{0}\" is an invalid MAC address and should throw an exception")
    @MethodSource("invalidAddresses")
    fun parameterizedInvalid(macAddress: String) {
        val exception = assertThrows<IllegalArgumentException>("\"$macAddress\" is invalid and should throw an IllegalArgumentException") {
            MacAddress(macAddress)
        }
        assertEquals("Invalid MAC Address", exception.message)
    }
}