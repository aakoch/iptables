import java.util.ArrayList;
import java.util.List;

public class DaySchedule {

  private final String day;
  private final List<TimeRange> timeRanges;
  private boolean marked;

  public DaySchedule(String day) {
    this.day = day;
    this.timeRanges = new ArrayList<>();
  }

  public void add(final TimeRange timeRange) {
    this.timeRanges.add(timeRange);
  }

  public List<TimeRange> getTimeRanges() {
    timeRanges.sort((range1, range2) -> range2.getEndTime().compareTo(range1.getStartTime()));
    return timeRanges;
  }

  public String getDay() {
    return day;
  }

  @Override
  public String toString() {
    return day + timeRanges;
  }
}
