package com.adamkoch.iptables;

import com.adamkoch.annotations.Unstable;

/**
 *
 * @since 0.1.0
 * @author aakoch
 */
@Unstable
public final class Util {

  private Util() {}

  /**
   * Sanitze the chain name to something accepted by iptables. This is not intended to cover all cases. Pull requests are encouraged for more sanitization.
   *
   * @param value The value to sanitize. This could be the name of a chain.
   * @return String with certain symbols removed or replaced with an underscore.
   */
  public static String sanitize(final String value) {
    return value.replaceAll("[-'\"_():]", "").replaceAll(" ", "_");
  }
}