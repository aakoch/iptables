package com.adamkoch.iptables.matches;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class TimeExtensionMatchTest {

  private static final DayOfWeek[] WEEKENDS = {SATURDAY, SUNDAY};
  private static final DayOfWeek[] WEEKDAYS = {MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY};

  @Test
  void asString() {
    TimeExtensionMatch match = new TimeExtensionMatch();
    assertEquals("-m time", match.asString());
  }

  @Test
  void withStartDate() {
    TimeExtensionMatch match = new TimeExtensionMatch();
    match.setStart(LocalDate.of(2020, 4, 4));
    assertEquals("-m time --datestart 2020-04-04", match.asString());
  }

  @Test
  void withEndDate() {
    TimeExtensionMatch match = new TimeExtensionMatch();
    match.setEnd(LocalDate.of(2020, 4, 4));
    assertEquals("-m time --datestop 2020-04-04", match.asString());
  }

  @Test
  void withStartAndEndDate() {
    TimeExtensionMatch match = new TimeExtensionMatch();
    match.setStart(LocalDate.of(2020, 4, 4));
    match.setEnd(LocalDate.of(2020, 4, 4));
    assertEquals("-m time --datestart 2020-04-04 --datestop 2020-04-04", match.asString());
  }

  @Test
  void withStartTime() {
    TimeExtensionMatch match = new TimeExtensionMatch();
    match.setStart(LocalTime.of(4, 4, 4));
    assertEquals("-m time --timestart 04:04:04", match.asString());
  }

  @Test
  void withEndTime() {
    TimeExtensionMatch match = new TimeExtensionMatch();
    match.setEnd(LocalTime.of(4, 4, 4));
    assertEquals("-m time --timestop 04:04:04", match.asString());
  }

  @Test
  void withStartAndEndTime() {
    TimeExtensionMatch match = new TimeExtensionMatch();
    match.setStart(LocalTime.of(5, 5, 5));
    match.setEnd(LocalTime.of(4, 4, 4));
    assertEquals("-m time --timestart 05:05:05 --timestop 04:04:04", match.asString());
  }

  @Test
  void withContiguous() {
    TimeExtensionMatch match = new TimeExtensionMatch();
    match.setStart(LocalTime.of(5, 5, 5));
    match.setEnd(LocalTime.of(4, 4, 4));
    match.setContiguous();
    assertEquals("-m time --timestart 05:05:05 --timestop 04:04:04 --contiguous", match.asString());
  }

  @Test
  void withStartDateTime() {
    TimeExtensionMatch match = new TimeExtensionMatch();
    match.setStart(LocalDateTime.of(2020, 5, 6, 7, 8, 9));
    assertEquals("-m time --datestart 2020-05-06T07:08:09", match.asString());
  }

  @Test
  void withWeekdaysTime() {
    TimeExtensionMatch match = new TimeExtensionMatch();
    match.setDays(new DayOfWeek[]{MONDAY, TUESDAY, THURSDAY, FRIDAY});
    assertEquals("-m time --weekdays Mon,Tue,Thu,Fri", match.asString());
  }

  @Test
  void withWeekDays() {
    TimeExtensionMatch match = new TimeExtensionMatch();
    match.setDays(WEEKDAYS);
    assertEquals("-m time --weekdays Mon,Tue,Wed,Thu,Fri", match.asString());
  }

  @Test
  void withWeekends() {
    TimeExtensionMatch match = new TimeExtensionMatch();
    match.setDays(WEEKENDS);
    assertEquals("-m time --weekdays Sat,Sun", match.asString());
  }

  @Test
  void withTimezone() {
    TimeExtensionMatch match = new TimeExtensionMatch();
    match.setStart(Instant.EPOCH);
    assertEquals("-m time --datestart 1970-01-01T00:00:00 --kerneltz", match.asString());
  }


}