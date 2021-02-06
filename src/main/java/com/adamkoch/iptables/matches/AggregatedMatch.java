package com.adamkoch.iptables.matches;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Combination of {@link Match}es
 *
 * @author aakoch
 * @since 0.1.0
 */
public class AggregatedMatch implements Match {

  private final List<? extends Match> matches;

  public AggregatedMatch(final List<? extends Match> matches) {
    this.matches = new ArrayList<>(matches);
  }

  @Override
  public String asString() {
    return matches.stream().map(Match::asString).collect(Collectors.joining(" "));
  }
}
