package com.adamkoch.iptables.matches

import com.adamkoch.iptables.objects.MacAddress
import com.adamkoch.iptables.matches.MacAddressMatch

/**
 * Mac address match
 *
 * @property macAddress
 * @constructor Create empty Mac address match
 */
class MacAddressMatch(private val macAddress: MacAddress) : Match {
    override val weight: Int = MatchWeight.MAC_ADDRESS.weight
    private var inverseFlag = false

    /**
     * Not
     *
     * @return
     */
    operator fun not(): Match {
        val newMatchingComponent = MacAddressMatch(macAddress)
        newMatchingComponent.inverseFlag = true
        return newMatchingComponent
    }

    override fun asString(): String {
        return "-m mac " + (if (inverseFlag) "! " else "") + "--mac-source " + macAddress
    }

    override fun toString(): String {
        return "MacAddress[$macAddress]"
    }
}