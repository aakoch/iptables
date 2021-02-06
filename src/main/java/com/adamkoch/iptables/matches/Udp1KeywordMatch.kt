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
class Udp1KeywordMatch
/**
 * @param keyword Keyword that will be used to REJECT packets
 */(private val keyword: String) : Match {
    override fun asString(): String {
//    [!] --in-interface -i input name[+]
//        network interface name ([+] for wildcard)
        // br0 must be the name of an interface
        // -m match is the extended match option
        val stringJoiner = StringJoiner(" ")
        stringJoiner.add(InInterfaceMatch("br0").asString())
        stringJoiner.add(ProtocolMatch.UDP.asString())
        stringJoiner.add(ExtensionMatch("udp").asString())
        stringJoiner.add(DestinationPortMatch("53").asString())

        // The following rules were not found at https://www.frozentux.net/iptables-tutorial/iptables-tutorial.html
        // But I did find it at http://ipset.netfilter.org/iptables-extensions.man.html#lbCE
        stringJoiner.add(StringExtensionMatch(keyword).asString())
        stringJoiner.add(AlgorithmExtensionMatchOption.BOYER_MOORE_OPTION.asString())
        stringJoiner.add(ToOffsetStringExtensionMatchOption(65535).asString())
        stringJoiner.add(IgnoreCaseExtensionMatchOption.DEFAULT.asString())
        return stringJoiner.toString()
    }
}