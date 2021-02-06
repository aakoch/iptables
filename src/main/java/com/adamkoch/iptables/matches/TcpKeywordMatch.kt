package com.adamkoch.iptables.matches

import java.util.List

/**
 * Combination of TCP Protocol Match and the Web String Extension Match.
 *
 * @author aakoch
 * @since 0.1.0
 */
class TcpKeywordMatch(keyword: String) :
    AggregatedMatch(List.of(ProtocolMatch.TCP, WebStringExtensionMatch(keyword)))