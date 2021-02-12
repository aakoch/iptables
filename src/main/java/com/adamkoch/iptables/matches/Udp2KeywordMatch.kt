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
class Udp2KeywordMatch(keyword: String, ipAddress: String) : Match {
    override val weight: Int = 2
    private val keyword: String
    private val ipAddress: String
    override fun asString(): String {
        //return "-p tcp -m webstr --url " + keyword;
        //return super.toString() + String.format("-i br0 -m udp --dport 53 -m string --string \"%s\" --algo bm --to 65535 --icase", keyword);
        // -d destinationIP/mask
        val stringJoiner = StringJoiner(" ")
        stringJoiner.add(DestinationMatch(ipAddress).asString()) // mask of 32 does ... what?
        stringJoiner.add(InInterfaceMatch("br0").asString()) // still don't know what "br0" is
        stringJoiner.add(ProtocolMatch.UDP.asString())
        stringJoiner.add(ExtensionMatch("udp").asString())
        stringJoiner.add(DestinationPortMatch("53").asString())

        // The following rules were not found at https://www.frozentux.net/iptables-tutorial/iptables-tutorial.html
        // But I did find it at http://ipset.netfilter.org/iptables-extensions.man.html#lbCE
        stringJoiner.add(
            StringExtensionMatch(keyword)
                .withAlgorithm(AlgorithmExtensionMatchOption.BOYER_MOORE)
                .withToOffset(65535)
                .withIgnoreCase()
                .asString()
        )
        return stringJoiner.toString()

//    return ProtocolMatch.UDP.toString() + String.format(" -d " + routerIP + "/32 -i br0 -m udp --dport 53 -m string --string \"%s\" --algo bm --to 65535 --icase", keyword);
    }

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
}