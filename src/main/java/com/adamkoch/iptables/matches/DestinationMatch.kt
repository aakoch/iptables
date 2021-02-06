package com.adamkoch.iptables.matches

import com.adamkoch.iptables.matches.GenericMatch

/**
 * The following description is from [https://www.frozentux.net/iptables-tutorial/iptables-tutorial.html](https://www.frozentux.net/iptables-tutorial/iptables-tutorial.html).
 * <br></br> Copied here for posterity. Please use the above webpage as official documentation.
 *
 * <dl>
 * <dt>Match</dt><dd>-d, --dst, --destination</dd>
 * <dt>Kernel</dt><dd>2.3, 2.4, 2.5 and 2.6</dd>
 * <dt>Example</dt><dd>iptables -A INPUT -d 192.168.1.1</dd>
 * <dt>Explanation</dt><dd> The --destination match is used for packets based on their destination address or
 * addresses. It works pretty much the
 * same as the --source match and has the same syntax, except that the match is based on where the packets are going to.
 * To match an IP range, we can add a netmask either in the exact netmask form, or in the number of ones (1's) counted
 * from the left side of the netmask bits. Examples are: 192.168.0.0/255.255.255.0 and 192.168.0.0/24. Both of these are
 * equivalent. We could also invert the whole match with an ! sign, just as before. --destination ! 192.168.0.1 would in
 * other words match all packets except those destined to the 192.168.0.1 IP address.</dd>
</dl> *
 *
 * @author aakoch
 * @since 0.1.0
 */
class DestinationMatch(value: String?) :
    GenericMatch(arrayOf("-d", "--dst", "--destination"), value)