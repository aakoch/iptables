import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

public class Main {

  private final Properties properties;
  private final Device tv1;
  private final Device tv2;

  public Main(final Properties properties) {
    this.properties = properties;

    tv1 = new Device("TV1", new MACAddress(properties.getProperty("TV1.mac")));
    tv2 = new Device("TV2", new MACAddress(properties.getProperty("TV2.mac")));
  }

  public static void main(String[] args) throws IOException {
    final Properties properties = new Properties();
    properties.load(new FileReader("secrets.properties"));
    new Main(properties).run();
  }

  private void run() {

    ArrayList<Rule> rules = new ArrayList<>();

    List<Device> joelsDevices = createJoelsDevices();
    Schedule joelsVirtualSchedule = setUpJoelSchedule();

    for (Device device : joelsDevices) {
      Rule blockDiscordDuringSchool = new Rule("discord", device).at(joelsVirtualSchedule);
      Rule blockDisneyDuringSchool = new Rule("disney", device).at(joelsVirtualSchedule);
      rules.add(blockDiscordDuringSchool);
      rules.add(blockDisneyDuringSchool);
    }
    
    writeToFile("Joel", rules);

    rules.clear();

    List<Device> alyssasDevices = createAlyssasDevices();
    Schedule alyssasSchedule = setUpAlyssasSchedule();

    for (Device device : alyssasDevices) {
      List.of("netflix", "hulu", "facebook").forEach(keyword -> {
        Rule blockKeywordDuringSchool = new Rule(keyword, device).at(alyssasSchedule);
        rules.add(blockKeywordDuringSchool);
      });

      Rule blockTikTokAlways = new Rule("tiktok", device);
      rules.add(blockTikTokAlways);
    }

    writeToFile("Alyssa", rules);
  }

  private void writeToFile(final String name, final ArrayList<Rule> rules) {
    try (FileWriter fileWriter = new FileWriter(name.toLowerCase() + ".sh")) {
      fileWriter.write("# " + StringUtils.repeat('#', 70) + "\n");
      fileWriter.write("# " + StringUtils.center(" " + name + " ", 70, '#') + "\n");
      fileWriter.write("# " + StringUtils.repeat('#', 70) + "\n");

      for (Rule rule : rules) {
        fileWriter.write(rule.toString());
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Schedule setUpAlyssasSchedule() {
    LocalTime startOfSchool = LocalTime.of(7, 45);
    LocalTime lunchStart = LocalTime.of(11, 0);
    LocalTime lunchEnd = LocalTime.of(12, 0);
    LocalTime endOfSchool = LocalTime.of(15, 45);

    TimeRange morningVirtual = new TimeRange(startOfSchool, lunchStart);
    TimeRange afternoonVirtual = new TimeRange(lunchEnd, endOfSchool);

    DaySchedule mondaySchedule = new DaySchedule("Mon");
    mondaySchedule.add(morningVirtual);
    mondaySchedule.add(afternoonVirtual);

    DaySchedule tuesdaySchedule = new DaySchedule("Tue");
    tuesdaySchedule.add(morningVirtual);
    tuesdaySchedule.add(afternoonVirtual);

    DaySchedule wednesdaySchedule = new DaySchedule("Wed");
    wednesdaySchedule.add(morningVirtual);
    wednesdaySchedule.add(afternoonVirtual);

    DaySchedule thursdaySchedule = new DaySchedule("Thu");
    thursdaySchedule.add(morningVirtual);
    thursdaySchedule.add(afternoonVirtual);

    DaySchedule fridaySchedule = new DaySchedule("Fri");
    fridaySchedule.add(morningVirtual);
    fridaySchedule.add(afternoonVirtual);

    Schedule schedule = new Schedule();
    schedule.add(mondaySchedule);
    schedule.add(tuesdaySchedule);
    schedule.add(wednesdaySchedule);
    schedule.add(thursdaySchedule);
    schedule.add(fridaySchedule);
    return schedule;
  }

  private List<Device> createAlyssasDevices() {
    List<Device> devices = new ArrayList<>(3);

    Device laptop = new Device("Alyssa's laptop", new MACAddress(properties.getProperty("alyssa.laptop.mac")));
    Device iphone = new Device("Alyssa's iPhone", new MACAddress(properties.getProperty("alyssa.iphone.mac")));
    Device iphone2 = new Device("Alyssa's iPhone 2", new MACAddress(properties.getProperty("alyssa.iphone2.mac")));

    devices.add(laptop);
    devices.add(iphone);
    devices.add(iphone2);
    devices.add(tv1);
    devices.add(tv2);
    return devices;
  }

  private List<Device> createJoelsDevices() {
    List<Device> devices = new ArrayList<>(2);

    Device joelsIPhone = new Device("Joel's iPhone", new MACAddress(properties.getProperty("joel.iphone.mac")));
    Device hpComputer = new Device("HP Computer", new MACAddress(properties.getProperty("hp_computer.mac")));

    devices.add(joelsIPhone);
    devices.add(hpComputer);
    devices.add(tv1);
    devices.add(tv2);
    return devices;
  }

  private Schedule setUpJoelSchedule() {
    LocalTime startOfSchool = LocalTime.of(7, 45);
    LocalTime lunchStart = LocalTime.of(11, 0);
    LocalTime lunchEnd = LocalTime.of(12, 0);
    LocalTime endOfSchool = LocalTime.of(15, 0);

    TimeRange morningVirtual = new TimeRange(startOfSchool, lunchStart);
    TimeRange afternoonVirtual = new TimeRange(lunchEnd, endOfSchool);

    DaySchedule mondaySchedule = new DaySchedule("Mon");
    mondaySchedule.add(morningVirtual);
    mondaySchedule.add(afternoonVirtual);

    DaySchedule tuesdaySchedule = new DaySchedule("Tue");
    tuesdaySchedule.add(morningVirtual);
    tuesdaySchedule.add(afternoonVirtual);

    DaySchedule wednesdaySchedule = new DaySchedule("Wed");
    wednesdaySchedule.add(morningVirtual);
    wednesdaySchedule.add(afternoonVirtual);

    DaySchedule thursdaySchedule = new DaySchedule("Thu");
    thursdaySchedule.add(morningVirtual);
    thursdaySchedule.add(afternoonVirtual);

    DaySchedule fridaySchedule = new DaySchedule("Fri");
    fridaySchedule.add(morningVirtual);
    fridaySchedule.add(afternoonVirtual);

    Schedule schedule = new Schedule();
    schedule.add(mondaySchedule);
    schedule.add(tuesdaySchedule);
    schedule.add(wednesdaySchedule);
    schedule.add(thursdaySchedule);
    schedule.add(fridaySchedule);
    return schedule;
  }

}
