import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Schedule {

  private final List<DaySchedule> daySchedules;

  public Schedule() {
    daySchedules = new ArrayList<>();
  }

  public void add(final DaySchedule daySchedule) {
    daySchedules.add(daySchedule);
  }

  public List<DaySchedule> getDaySchedules() {
    return daySchedules;
  }

  @Override
  public String toString() {
    StringJoiner sj = new StringJoiner(", ");
    daySchedules.forEach(daySchedule -> {
      sj.add(daySchedule.toString());
    });
    return sj.toString();
  }
}