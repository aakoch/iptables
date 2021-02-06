package com.adamkoch.iptables.objects

import com.adamkoch.iptables.objects.MacAddress

/**
 * Represents a network-enabled device such as a computer, TV, gaming system, etc.
 *
 * @author aakoch
 * @since 0.1.0
 */
class Device(val name: String, val macAddress: MacAddress) {
    override fun toString(): String {
        return "$name ($macAddress)"
    }
}