package com.adamkoch.iptables.matches

import com.adamkoch.iptables.matches.GenericMatch
import com.adamkoch.iptables.matches.InInterfaceMatch

/**
 * The following description is from [https://www.frozentux.net/iptables-tutorial/iptables-tutorial.html](https://www.frozentux.net/iptables-tutorial/iptables-tutorial.html).
 * <br></br> Copied here for posterity. Please use the above webpage as official documentation.
 *
 *
 * <dl>
 * <dt>Match</dt>
 * <dd>-i, --in-interface</dd>
 *
 * <dt>Kernel</dt>
 * <dd>2.3, 2.4, 2.5 and 2.6</dd>
 *
 * <dt>Example</dt>
 * <dd>iptables -A INPUT -i eth0</dd>
 * <dt>Explanation</dt>
 * <dd>This match is used for the interface the packet came in on. Note that this option is only legal in the INPUT, FORWARD and
 * PREROUTING chains and will return an error message when used anywhere else. The default behavior of this match, if no
 * particular interface is specified, is to assume a string value of +. The + value is used to match a string of letters
 * and numbers. A single + would, in other words, tell the kernel to match all packets without considering which
 * interface it came in on. The + string can also be appended to the type of interface, so eth+ would be all Ethernet
 * devices. We can also invert the meaning of this option with the help of the ! sign. The line would then have a syntax
 * looking something like -i ! eth0, which would match all incoming interfaces, except eth0.</dd>
</dl> *
 *
 * @author aakoch
 * @since 0.1.0
 */
class InInterfaceMatch(value: String?) : GenericMatch(FLAGS, value!!) {
    override val weight: Int = MatchWeight.INTERFACE.weight

    companion object {
        private val FLAGS = arrayOf("-i", "--in-interface")
    }
}