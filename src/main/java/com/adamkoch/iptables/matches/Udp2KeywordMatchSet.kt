package com.adamkoch.iptables.matches

import com.adamkoch.annotations.Unstable
import java.util.*

/**
 * Test class that combines some [Match]es I've seen.
 *
 * @author aakoch
 * @since 0.1.0
 */
@Unstable
class Udp2KeywordMatchSet(keyword: String, ipAddress: String) {
    private val keyword: String
    private val ipAddress: String

    /**
     * @param keyword Keyword that will be used to REJECT packets
     * @param ipAddress IP Address-like
     */
    init {
        Objects.requireNonNull(keyword)
        Objects.requireNonNull(ipAddress)
        this.keyword = keyword
        this.ipAddress = ipAddress
    }

    fun getMatches(): List<Match> {
        return listOf(InInterfaceMatch("br0"),
            ProtocolMatch.UDP,
            UdpExtensionMatch().withDestinationPort(53),
            DestinationMatch(ipAddress),
            StringExtensionMatch(keyword).withAlgorithm(AlgorithmExtensionMatchOption.BOYER_MOORE_OPTION).withToOffset(65535).withIgnoreCase())
    }

    fun asString() : String {
        return getMatches().sorted().joinToString(" ", transform = Match::asString)
    }

    override fun toString(): String {
        return "Udp2KeywordMatch[$keyword]"
    }

}