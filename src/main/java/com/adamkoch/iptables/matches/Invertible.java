package com.adamkoch.iptables.matches;

/**
 * Specifies when a {@link Match} can be inverted.
 *
 * @author aakoch
 * @since 0.1.0
 */
@FunctionalInterface
public interface Invertible {

  /**
   * Returns a new inverted {@link Match}. The Match it was called upon will not change.
   *
   * @return an inverted Match
   */
  Match not();
}
