package com.adamkoch.iptables.matches

import com.adamkoch.annotations.Unstable

/**
 * Test class that combines some [Match]es I've seen.
 *
 * @author aakoch
 * @since 0.1.0
 * @param keyword Keyword that will be used to REJECT packets
 */
@Unstable
class Udp1KeywordMatchSet(private val keyword: String)  {

    fun getMatches(): List<Match> {
        return listOf(InInterfaceMatch("br0"),
            ProtocolMatch.UDP,
            UdpExtensionMatch().withDestinationPort(53),
            StringExtensionMatch(keyword).withAlgorithm(AlgorithmExtensionMatchOption.BOYER_MOORE_OPTION).withToOffset(65535).withIgnoreCase())

    }

    override fun toString(): String {
        return "Udp1KeywordMatch[$keyword]"
    }
}