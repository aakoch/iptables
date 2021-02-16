package com.adamkoch.iptables.matches

import com.adamkoch.iptables.objects.Protocol
import java.util.*

/**
 * The following description is from [https://www.frozentux.net/iptables-tutorial/iptables-tutorial.html](https://www.frozentux.net/iptables-tutorial/iptables-tutorial.html).
 * <br></br>Copied here for posterity. Please use the above webpage as official documentation.
 *
 * <dl>
 * <dt>Match</dt><dd>-p, --protocol</dd>
 * <dt>Kernel</dt><dd>2.3, 2.4, 2.5 and 2.6</dd>
 * <dt>Example</dt><dd>iptables -A INPUT -p tcp</dd>
 * <dt>Explanation</dt><dd>This match is used to
 * check for certain protocols. Examples of protocols are TCP, UDP and ICMP. The protocol must either be one of the
 * internally specified TCP, UDP or ICMP. It may also take a value specified in the /etc/protocols file, and if it can't
 * find the protocol there it will reply with an error. The protocl(sic) may also be an integer value. For example, the ICMP
 * protocol is integer value 1, TCP is 6 and UDP is 17. Finally, it may also take the value ALL. ALL means that it
 * matches only TCP, UDP and ICMP. If this match is given the integer value of zero (0), it means ALL protocols, which
 * in turn is the default behavior, if the --protocol match is not used. This match can also be inversed with the !
 * sign, so --protocol ! tcp would mean to match UDP and ICMP.</dd>
</dl> *
 *
 * @author aakoch
 * @since 0.1.0
 */
class ProtocolMatch private constructor(private val protocol: Protocol) : Match {
    override val weight: Int = MatchWeight.PROTOCOL.weight

    override fun asString(): String {
        return "-p ${protocol.name.toLowerCase(Locale.ENGLISH)}"
    }

    override fun toString(): String {
        return "ProtocolMatch." + protocol.name
    }

    companion object {
        fun valueOf(protocol: Protocol): Match {
            return when (protocol) {
                Protocol.ICMP -> ICMP
                Protocol.TCP -> TCP
                Protocol.IP -> IP
                Protocol.UDP -> UDP
            }
        }

        @JvmField val ICMP = ProtocolMatch(Protocol.ICMP)
        @JvmField val TCP = ProtocolMatch(Protocol.TCP)
        @JvmField val IP = ProtocolMatch(Protocol.IP)
        @JvmField val UDP = ProtocolMatch(Protocol.UDP)
    }
}