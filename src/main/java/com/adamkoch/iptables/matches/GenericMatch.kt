package com.adamkoch.iptables.matches;

import java.util.Arrays;
import java.util.Objects;

/**
 * Name came from https://www.frozentux.net/iptables-tutorial/iptables-tutorial.html#GENERICMATCHES
 *
 *
 * From https://www.lammertbies.nl/comm/info/iptables:
 *
 *
 * // iptables -A Enemies -s $1 -j DROP
 *
 * -A Enemies  -m recent --name psc --update --seconds 60 -j DROP -A Enemies -i ! lo -m tcp -p tcp --dport 1433  -m
 * recent --name psc --set -j DROP -A Enemies -i ! lo -m tcp -p tcp --dport 3306  -m recent --name psc --set -j DROP -A
 * Enemies -i ! lo -m tcp -p tcp --dport 8086  -m recent --name psc --set -j DROP -A Enemies -i ! lo -m tcp -p tcp
 * --dport 10000 -m recent --name psc --set -j DROP -A Enemies -s 99.99.99.99 -j DROP
 *
 * @author aakoch
 * @since 0.1.0
 */
public class GenericMatch implements Match {

//  public static final GenericMatch EXPLICIT_MATCH = new GenericMatch("-m");

  private final String[] flags;
  private final String value;

  public GenericMatch(final String flag, final String value) {
    Objects.requireNonNull(flag);
    Objects.requireNonNull(value);
    this.flags = new String[]{flag};
    this.value = value;
  }

  protected GenericMatch(final String[] flags, final String value) {
    Objects.requireNonNull(flags);
    Objects.requireNonNull(value);
    this.flags = flags;
    this.value = value;
  }

  @Override
  public String asString() {
    return flags[0] + " " + value;
  }

  @Override
  public String toString() {
    return asString();
  }
}
