package com.adamkoch.iptables.objects

import com.adamkoch.iptables.objects.MacAddress
import java.lang.IllegalArgumentException
import java.util.Objects

/**
 * A MAC address. Maybe someday with better validation.
 *
 * @since 0.1.0
 * @author aakoch
 */
class MacAddress(private val addr: String) {

    override fun toString(): String {
        return addr
    }

    companion object {
        @JvmField
        val DUMMY = MacAddress("00:00:00:A1:2B:CC")
    }

    init {
        Objects.requireNonNull(addr)
        if (!addr.chars().allMatch { it > 32 && it < 127 } || addr.length > 17 || addr.isBlank())
            throw IllegalArgumentException("Invalid MAC Address")
    }
}