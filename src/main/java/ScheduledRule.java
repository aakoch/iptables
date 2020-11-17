import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

public class ScheduledRule extends Rule {

  private final Schedule schedule;

  public ScheduledRule(final Rule rule, final Schedule schedule) {
    super(rule.getKeyword(), rule.getDevice());

    this.schedule = schedule;
  }

  public String header(final TimeRangeToDayMap timeRangeToDayMap) {
    StringJoiner sj = new StringJoiner("\n");
    sj.add(super.header());
    String firstLine = "#".repeat(10) + "  Days:  " + timeRangeToDayMap.getDaysString();
    sj.add("# " + firstLine);
    String secondLine = "#".repeat(10) + "  Time:  " + timeRangeToDayMap.getTimeRange();
    sj.add("# " + secondLine);
    return sj.toString();
  }

  private String getTime(String day, final TimeRange timeRange) {
    return String
        .format("-m time --kerneltz --timestart %s --timestop %s --weekdays %s",
                timeRange.getStartTime(),
                timeRange.getEndTime(), day);
  }

  private String toFirstIPTableRule(final TimeRangeToDayMap timeRangeToDayMap) {
    return super.toFirstIPTableRule() + " " + getTime(timeRangeToDayMap.getDaysString(), timeRangeToDayMap.getTimeRange());
  }

  private String toSecondIPTableRule(final TimeRangeToDayMap timeRangeToDayMap) {
    return super.toSecondIPTableRule() + " " + getTime(timeRangeToDayMap.getDaysString(), timeRangeToDayMap.getTimeRange());
  }

  private String toThirdIPTableRule(final TimeRangeToDayMap timeRangeToDayMap) {
    return super.toThirdIPTableRule() + " " + getTime(timeRangeToDayMap.getDaysString(), timeRangeToDayMap.getTimeRange());
  }

  @Override
  public String toString() {

    StringJoiner sj = new StringJoiner("\n");

    Set<TimeRangeToDayMap> timeRangeToDayMaps = getTimeRangeToDayMap();
    for (TimeRangeToDayMap timeRangeToDayMap : timeRangeToDayMaps) {
      sj.add(this.header(timeRangeToDayMap));
      sj.add(toFirstIPTableRule(timeRangeToDayMap));
      sj.add(toSecondIPTableRule(timeRangeToDayMap));
      sj.add(toThirdIPTableRule(timeRangeToDayMap) + "\n");
    }

    return sj.toString();
  }

  public Set<TimeRangeToDayMap> getTimeRangeToDayMap() {

    Set<TimeRangeToDayMap> timeRangeToDayMaps = new HashSet<>();
    for (DaySchedule daySchedule : schedule.getDaySchedules()) {
      for (TimeRange timeRange : daySchedule.getTimeRanges()) {

        TimeRangeToDayMap timeRangeToDayMap = new TimeRangeToDayMap(timeRange);
        timeRangeToDayMap.addDay(daySchedule.getDay());

        // find other days
        for (DaySchedule otherDaySchedule : schedule.getDaySchedules()) {
          // don't look at self
          if (!daySchedule.equals(otherDaySchedule)) {
            for (TimeRange otherTimeRange : otherDaySchedule.getTimeRanges()) {
              if (timeRange.equals(otherTimeRange)) {
                timeRangeToDayMap.addDay(otherDaySchedule.getDay());
              }
            }
          }
        }
        timeRangeToDayMaps.add(timeRangeToDayMap);
      }
    }

    return timeRangeToDayMaps;
  }
}
