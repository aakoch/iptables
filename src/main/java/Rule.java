import java.util.StringJoiner;

public class Rule {

  private final String keyword;
  private final Device device;

  public Rule(final String keyword, final Device device) {
    this.keyword = keyword;
    this.device = device;
  }

  public Rule at(final Schedule schedule) {
    return new ScheduledRule(this, schedule);
  }

  public String header() {
    StringJoiner sj = new StringJoiner("\n");
    String firstLine = "#".repeat(10) + "  Device:  " + getDevice().toString() + "  " + "#".repeat(10);
    sj.add("# " + firstLine);
    String secondLine = "#".repeat(10) + "  Keyword: " + getKeyword() + "  ";
    sj.add("# " + secondLine + " ".repeat(firstLine.length() - secondLine.length() - 10) + "#".repeat(10));
    return sj.toString();
  }

  public Device getDevice() {
    return device;
  }

  public String getKeyword() {
    return keyword;
  }

  protected String toFirstIPTableRule() {
    return String
        .format(
            "iptables -I FORWARD -p tcp -m mac --mac-source %s -m webstr --url %s -j REJECT --reject-with tcp-reset",
            device.getMacAddress(), keyword);
  }

  protected String toSecondIPTableRule() {
    return String.format(
        "iptables -I FORWARD -i br0 -p udp -m mac --mac-source %s -m udp --dport 53 -m string --string \"%s\" --algo bm --to 65535 --icase -j DROP",
        device.getMacAddress(), keyword);
  }

  protected String toThirdIPTableRule() {
    return String.format(
        "iptables -I INPUT -d 192.168.50.1/32 -i br0 -p udp -m mac --mac-source %s -m udp --dport 53 -m string --string \"%s\" --algo bm --to 65535 --icase -j DROP",
        device.getMacAddress(), keyword);
  }

  @Override
  public String toString() {
    StringJoiner sj = new StringJoiner("\n");

    sj.add(this.header());
    sj.add(toFirstIPTableRule());
    sj.add(toSecondIPTableRule());
    sj.add(toThirdIPTableRule());

    return sj.toString();
  }

}
