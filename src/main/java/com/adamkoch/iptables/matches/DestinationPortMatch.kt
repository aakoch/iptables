package com.adamkoch.iptables.matches;

/**
 * Matches against a destination port.
 *
 * @author aakoch
 * @since 0.1.0
 */
public class DestinationPortMatch extends GenericMatch {

  public DestinationPortMatch(final String value) {
    this(Integer.parseInt(value));
  }

  public DestinationPortMatch(final int value) {
    super("--dport", Integer.toString(value));
    requireWithinPortRange(value);
  }

  private static void requireWithinPortRange(final int value) {
    if (value < 0 || value > 65535)
      throw new IllegalArgumentException("port \"" + value + "\" is outside the allowed port range of 0 - 65535");
  }
}
