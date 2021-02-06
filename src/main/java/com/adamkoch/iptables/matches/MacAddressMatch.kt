package com.adamkoch.iptables.matches

import com.adamkoch.iptables.objects.MacAddress
import com.adamkoch.iptables.matches.MacAddressMatch

/**
 * Match for MAC addresses.
 *
 * @author aakoch
 * @since 0.1.0
 */
class MacAddressMatch(private val macAddress: MacAddress) : Match {
    private var inverseFlag = false
    operator fun not(): Match {
        val newMatchingComponent = MacAddressMatch(macAddress)
        newMatchingComponent.inverseFlag = true
        return newMatchingComponent
    }

    override fun asString(): String {
        return "-m mac " + (if (inverseFlag) "! " else "") + "--mac-source " + macAddress
    }
}