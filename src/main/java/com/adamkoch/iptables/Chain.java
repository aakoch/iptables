package com.adamkoch.iptables;

import com.adamkoch.annotations.Unstable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @since 0.1.0
 * @author aakoch
 */
@Unstable
public class Chain {

  private final String name;
  private final List<Rule> rules;

  public Chain(final String name) {
    this.name = name;
    this.rules = new ArrayList<>();
  }

  @Override
  public String toString() {
    String sanitized = Util.sanitize(this.name);
    return rules.stream().map(rule -> sanitized + " " + rule.asString()).collect(Collectors.joining(System.lineSeparator()));

//    StringBuilder timeScheduleString = new StringBuilder();
//    timeSchedule.ifPresent(ts -> {
//      ts.getTimeRanges().forEach(timeRange -> {
//        timeScheduleString.append("iptables -A " + sanitized + " -p tcp -m time --kerneltz --timestart " + timeRange.getStartTime() + " --timestop " + timeRange.getEndTime() + " -j REJECT\n");
//      });
//    });
//
//    return "iptables -N " + sanitized + "\n" +
//    "iptables -I INPUT -j " + sanitized + "\n" +
//    "iptables -I FORWARD -j " + sanitized + "\n" +
//    "iptables -A " + sanitized + " -p tcp -m mac ! --mac-source " + device.get().getMacAddress() + " -j RETURN\n" +
//
////            // iptables -A Adam -p tcp -m time --kerneltz --timestart 18:45 --timestop 18:48 --weekdays Mon,Tue,Wed,Thu,Fri -j REJECT
//
//        timeScheduleString.toString() +
////    "iptables -A " + sanitized + " -p tcp -m time --kerneltz --timestart 18:45 --timestop 18:48 --weekdays Mon,Tue,Wed,Thu,Fri -j REJECT\n"
//        "iptables -A " + sanitized + " -j RETURN\n";
  }

  public void add(final Rule rule) {
    rules.add(rule);
  }

  public String getName() {
    return name;
  }

  public List<Rule> getRules() {
    return rules;
  }
}
