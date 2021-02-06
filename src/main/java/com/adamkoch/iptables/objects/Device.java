package com.adamkoch.iptables.objects;

/**
 * Represents a network-enabled device such as a computer, TV, gaming system, etc.
 *
 * @author aakoch
 * @since 0.1.0
 */
public class Device {

  private final String name;
  private final MacAddress macAddress;

  public Device(final String name, final MacAddress macAddress) {
    this.name = name;
    this.macAddress = macAddress;
  }

  public MacAddress getMacAddress() {
    return macAddress;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name + " (" + macAddress + ")";
  }
}
