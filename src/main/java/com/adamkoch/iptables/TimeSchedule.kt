package com.adamkoch.iptables;

import com.adamkoch.annotations.Unstable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 *
 * @since 0.1.0
 * @author aakoch
 */
@Unstable
public class TimeSchedule {

  private final List<TimeRange> timeRanges;

  public TimeSchedule() {
    timeRanges = new ArrayList<>();
  }

  public void add(final TimeRange daySchedule) {
    timeRanges.add(daySchedule);
  }

  public List<TimeRange> getTimeRanges() {
    return timeRanges;
  }

  @Override
  public String toString() {
    StringJoiner sj = new StringJoiner(", ");
    timeRanges.forEach(daySchedule -> {
      sj.add(daySchedule.toString());
    });
    return sj.toString();
  }
}
