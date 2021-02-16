package com.adamkoch.iptables.matches

import com.adamkoch.iptables.objects.Protocol

/**
 * Not the same as the ProtocolMatch. I might need to combine them.
 */
class UdpExtensionMatch() : ExtensionMatch(Protocol.UDP.name.toLowerCase()) {

    override val weight: Int = MatchWeight.PROTOCOL.weight - 1

    fun withDestinationPort(port: Int): UdpExtensionMatch {
        val newStringExtensionMatch = copy(port)
        newStringExtensionMatch.with(DestinationPortOption(port))
        return newStringExtensionMatch
    }

    private fun copy(port: Int): UdpExtensionMatch {
        val newUdpExtensionMatch = UdpExtensionMatch()
        newUdpExtensionMatch.extensionMatchOptions = super.extensionMatchOptions
        return newUdpExtensionMatch
    }

    override fun asString(): String {
        return "-m udp " + super.extensionMatchOptions?.joinToString(" ", transform = ExtensionMatchOption::asString)
    }
}