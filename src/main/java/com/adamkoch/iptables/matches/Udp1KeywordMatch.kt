package com.adamkoch.iptables.matches;

import com.adamkoch.annotations.Unstable;
import java.util.StringJoiner;

/**
 * Test class that combines some {@link Match}es I've seen.
 *
 * @author aakoch
 * @since 0.1.0
 */
@Unstable
public class Udp1KeywordMatch implements Match {

  private final String keyword;

  /**
   * @param keyword Keyword that will be used to REJECT packets
   */
  public Udp1KeywordMatch(final String keyword) {
    this.keyword = keyword;
  }

  @Override
  public String asString() {
//    [!] --in-interface -i input name[+]
//        network interface name ([+] for wildcard)
    // br0 must be the name of an interface
    // -m match is the extended match option

    StringJoiner stringJoiner = new StringJoiner(" ");

    stringJoiner.add(new InInterfaceMatch("br0").asString());
    stringJoiner.add(ProtocolMatch.UDP.asString());

    stringJoiner.add(new ExtensionMatch("udp").asString());
    stringJoiner.add(new DestinationPortMatch("53").asString());

    // The following rules were not found at https://www.frozentux.net/iptables-tutorial/iptables-tutorial.html
    // But I did find it at http://ipset.netfilter.org/iptables-extensions.man.html#lbCE
    stringJoiner.add(new StringExtensionMatch(keyword).asString());
    stringJoiner.add(AlgorithmExtensionMatchOption.BOYER_MOORE_OPTION.asString());
    stringJoiner.add(new ToOffsetStringExtensionMatchOption(65535).asString());
    stringJoiner.add(IgnoreCaseExtensionMatchOption.DEFAULT.asString());

    return stringJoiner.toString();
  }
}

// iptables -A Enemies -s $1 -j DROP

//-A Enemies  -m recent --name psc --update --seconds 60 -j DROP
//-A Enemies -i ! lo -m tcp -p tcp --dport 1433  -m recent --name psc --set -j DROP
//-A Enemies -i ! lo -m tcp -p tcp --dport 3306  -m recent --name psc --set -j DROP
//-A Enemies -i ! lo -m tcp -p tcp --dport 8086  -m recent --name psc --set -j DROP
//-A Enemies -i ! lo -m tcp -p tcp --dport 10000 -m recent --name psc --set -j DROP
//-A Enemies -s 99.99.99.99 -j DROP