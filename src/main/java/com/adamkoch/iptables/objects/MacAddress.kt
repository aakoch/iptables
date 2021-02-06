package com.adamkoch.iptables.objects

import com.adamkoch.iptables.objects.MacAddress
import java.util.Objects

/**
 *
 * @since 0.1.0
 * @author aakoch
 */
class MacAddress(addr: String) {
    private val addr: String
    override fun toString(): String {
        return addr
    }

    companion object {
        @JvmField
        val DUMMY = MacAddress("00:00:00:a1:2b:cc")
    }

    init {
        Objects.requireNonNull(addr)
        this.addr = addr
    }
}