package com.adamkoch.iptables.matches;

import com.adamkoch.iptables.objects.Protocol;
import java.util.Locale;

/**
 * The following description is from <a href="https://www.frozentux.net/iptables-tutorial/iptables-tutorial.html">https://www.frozentux.net/iptables-tutorial/iptables-tutorial.html</a>.
 * <br>Copied here for posterity. Please use the above webpage as official documentation.
 *
 * <dl>
 *    <dt>Match</dt><dd>-p, --protocol</dd>
 *    <dt>Kernel</dt><dd>2.3, 2.4, 2.5 and 2.6</dd>
 *    <dt>Example</dt><dd>iptables -A INPUT -p tcp</dd>
 *    <dt>Explanation</dt><dd>This match is used to
 * check for certain protocols. Examples of protocols are TCP, UDP and ICMP. The protocol must either be one of the
 * internally specified TCP, UDP or ICMP. It may also take a value specified in the /etc/protocols file, and if it can't
 * find the protocol there it will reply with an error. The protocl(sic) may also be an integer value. For example, the ICMP
 * protocol is integer value 1, TCP is 6 and UDP is 17. Finally, it may also take the value ALL. ALL means that it
 * matches only TCP, UDP and ICMP. If this match is given the integer value of zero (0), it means ALL protocols, which
 * in turn is the default behavior, if the --protocol match is not used. This match can also be inversed with the !
 * sign, so --protocol ! tcp would mean to match UDP and ICMP.</dd>
 * </dl>
 *
 * @author aakoch
 * @since 0.1.0
 */
public final class ProtocolMatch implements Match {

  @SuppressWarnings("StaticVariableOfConcreteClass")
  public static final ProtocolMatch ICMP = new ProtocolMatch(Protocol.ICMP);

  @SuppressWarnings("StaticVariableOfConcreteClass")
  public static final ProtocolMatch IP = new ProtocolMatch(Protocol.IP);

  @SuppressWarnings("StaticVariableOfConcreteClass")
  public static final ProtocolMatch TCP = new ProtocolMatch(Protocol.TCP);

  @SuppressWarnings("StaticVariableOfConcreteClass")
  public static final ProtocolMatch UDP = new ProtocolMatch(Protocol.UDP);

  private final Protocol protocol;

  private ProtocolMatch(final Protocol protocol) {
    this.protocol = protocol;
  }

  @Override
  public String asString() {
    return "-p " + protocol.name().toLowerCase(Locale.ENGLISH);
  }
}
