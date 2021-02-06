package com.adamkoch.iptables.matches;

/**
 * The following description is from <a href="https://www.frozentux.net/iptables-tutorial/iptables-tutorial.html">https://www.frozentux.net/iptables-tutorial/iptables-tutorial.html</a>.
 * <br> Copied here for posterity. Please use the above webpage as official documentation.
 *
 * <dl>
 *    <dt>Match</dt><dd>-s, --src, --source</dd>
 *    <dt>Kernel</dt><dd>2.3, 2.4, 2.5 and 2.6</dd>
 *    <dt>Example</dt><dd>iptables -A INPUT -s 192.168.1.1</dd>
 *    <dt>Explanation</dt><dd>
 * This is
 * the source match, which is used to match packets, based on their source IP address. The main form can be used to
 * match single IP addresses, such as 192.168.1.1. It could also be used with a netmask in a CIDR "bit" form, by
 * specifying the number of ones (1's) on the left side of the network mask. This means that we could for example add
 * /24 to use a 255.255.255.0 netmask. We could then match whole IP ranges, such as our local networks or network
 * segments behind the firewall. The line would then look something like 192.168.0.0/24. This would match all packets in
 * the 192.168.0.x range. Another way is to do it with a regular netmask in the 255.255.255.255 form (i.e.,
 * 192.168.0.0/255.255.255.0). We could also invert the match with an ! just as before. If we were, in other words, to
 * use a match in the form of --source ! 192.168.0.0/24, we would match all packets with a source address not coming
 * from within the 192.168.0.x range. The default is to match all IP addresses.</dd>
 * </dl>
 *
 * @author aakoch
 * @since 0.1.0
 */
public class SourceMatch extends GenericMatch {

  public SourceMatch(final String value) {
    super(new String[]{"-s", "--src", "--source"}, value);
  }
}
