package com.adamkoch.iptables.matches;

import com.adamkoch.iptables.objects.MacAddress;

/**
 * Match for MAC addresses.
 *
 * @author aakoch
 * @since 0.1.0
 */
public class MacAddressMatch implements Match {

  private final MacAddress macAddress;
  private boolean inverseFlag;

  public MacAddressMatch(final MacAddress macAddress) {
    this.macAddress = macAddress;
  }

  public Match not() {
    final MacAddressMatch newMatchingComponent = new MacAddressMatch(macAddress);
    newMatchingComponent.inverseFlag = true;
    return newMatchingComponent;
  }

  @Override
  public String asString() {
    return "-m mac " + (inverseFlag ? "! " : "") + "--mac-source " + macAddress;
  }
}
