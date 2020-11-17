class Device {

  private final String name;
  private final MACAddress macAddress;

  public Device(final String name, final MACAddress macAddress) {
    this.name = name;
    this.macAddress = macAddress;
  }

  public MACAddress getMacAddress() {
    return macAddress;
  }

  @Override
  public String toString() {
    return name + " (" + macAddress + ")";
  }
}
