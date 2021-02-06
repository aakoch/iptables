package com.adamkoch.iptables.matches;

import com.adamkoch.annotations.Unstable;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Test class that combines some {@link Match}es I've seen.
 *
 * @author aakoch
 * @since 0.1.0
 */
@Unstable
public class Udp2KeywordMatch implements Match {

  private final String keyword;
  private final String ipAddress;

  /**
   * @param keyword Keyword that will be used to REJECT packets
   * @param ipAddress IP Address-like
   */
  public Udp2KeywordMatch(final String keyword, final String ipAddress) {
    Objects.requireNonNull(keyword);
    Objects.requireNonNull(ipAddress);
    this.keyword = keyword;
    this.ipAddress = ipAddress;
  }

  @Override
  public String asString() {
    //return "-p tcp -m webstr --url " + keyword;
    //return super.toString() + String.format("-i br0 -m udp --dport 53 -m string --string \"%s\" --algo bm --to 65535 --icase", keyword);
    // -d destinationIP/mask

    StringJoiner stringJoiner = new StringJoiner(" ");
    stringJoiner.add(new DestinationMatch(ipAddress).asString()); // mask of 32 does ... what?
    stringJoiner.add(new InInterfaceMatch("br0").asString()); // still don't know what "br0" is
    stringJoiner.add(ProtocolMatch.UDP.asString());

    stringJoiner.add(new ExtensionMatch("udp").asString());
    stringJoiner.add(new DestinationPortMatch("53").asString());

    // The following rules were not found at https://www.frozentux.net/iptables-tutorial/iptables-tutorial.html
    // But I did find it at http://ipset.netfilter.org/iptables-extensions.man.html#lbCE
    stringJoiner.add(new StringExtensionMatch(keyword)
                         .withAlgorithm(AlgorithmExtensionMatchOption.BOYER_MOORE)
                         .withToOffset(65535)
                         .withIgnoreCase()
                         .asString());
    return stringJoiner.toString();

//    return ProtocolMatch.UDP.toString() + String.format(" -d " + routerIP + "/32 -i br0 -m udp --dport 53 -m string --string \"%s\" --algo bm --to 65535 --icase", keyword);
  }
}
