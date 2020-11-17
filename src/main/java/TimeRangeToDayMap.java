import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class TimeRangeToDayMap {

  private final TimeRange timeRange;
  private final Set<String> days;

  public TimeRangeToDayMap(final TimeRange timeRange) {
    this.timeRange = timeRange;
    days = new HashSet<>();
  }

  public void addDay(final String day) {
    days.add(day);
  }

  List<String> getDays() {
    List<String> dayList = new ArrayList<>();
    if (days.contains("Mon")) {
      dayList.add("Mon");
    }
    if (days.contains("Tue")) {
      dayList.add("Tue");
    }
    if (days.contains("Wed")) {
      dayList.add("Wed");
    }
    if (days.contains("Thu")) {
      dayList.add("Thu");
    }
    if (days.contains("Fri")) {
      dayList.add("Fri");
    }

    return dayList;
  }

  public TimeRange getTimeRange() {
    return timeRange;
  }

  public String getDaysString() {
    return String.join(",", getDays());
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final TimeRangeToDayMap that = (TimeRangeToDayMap) o;
    return timeRange.equals(that.timeRange) &&
        days.equals(that.days);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeRange, days);
  }

  @Override
  public String toString() {
    return "TimeRangeMap{" +
        "timeRange=" + timeRange +
        ", days=" + getDaysString() +
        '}';
  }
}
