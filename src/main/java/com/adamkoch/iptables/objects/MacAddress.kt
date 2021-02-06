package com.adamkoch.iptables.objects;

import java.util.Objects;

/**
 *
 * @since 0.1.0
 * @author aakoch
 */
public class MacAddress {

  public static final MacAddress DUMMY = new MacAddress("00:00:00:a1:2b:cc");

  private final String addr;

  public MacAddress(final String addr) {
    Objects.requireNonNull(addr);
    this.addr = addr;
  }

  @Override
  public String toString() {
    return addr;
  }
}
