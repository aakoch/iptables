import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeRange {

  public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

  private final LocalTime startTime;
  private final LocalTime endTime;

  public TimeRange(final LocalTime startTime, final LocalTime endTime) {
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public String getStartTime() {
    return startTime.format(FORMATTER);
  }

  public String getEndTime() {
    return endTime.format(FORMATTER);
  }

  @Override
  public String toString() {
    return getStartTime() + "-" + getEndTime();
  }
}
