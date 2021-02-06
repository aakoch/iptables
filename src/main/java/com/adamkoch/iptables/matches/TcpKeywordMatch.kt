package com.adamkoch.iptables.matches;

import java.util.List;

/**
 * Combination of TCP Protocol Match and the Web String Extension Match.
 *
 * @author aakoch
 * @since 0.1.0
 */
public class TcpKeywordMatch extends AggregatedMatch {

  public TcpKeywordMatch(final String keyword) {
    super(List.of(ProtocolMatch.TCP, new WebStringExtensionMatch(keyword)));
  }
}
