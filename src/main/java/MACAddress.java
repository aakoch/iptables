import java.util.Objects;

class MACAddress {

  private final String addr;

  public MACAddress(final String addr) {
    Objects.requireNonNull(addr);
    this.addr = addr;
  }

  @Override
  public String toString() {
    return addr;
  }
}
