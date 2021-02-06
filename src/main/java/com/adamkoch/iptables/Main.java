package com.adamkoch.iptables;

import com.adamkoch.iptables.matches.MacAddressMatch;
import com.adamkoch.iptables.matches.Match;
import com.adamkoch.iptables.matches.TcpKeywordMatch;
import com.adamkoch.iptables.matches.Udp1KeywordMatch;
import com.adamkoch.iptables.matches.Udp2KeywordMatch;
import com.adamkoch.iptables.objects.Device;
import com.adamkoch.iptables.objects.MacAddress;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/* Do we need?
* *filter
:INPUT ACCEPT [0:0]
:FORWARD ACCEPT [0:0]
:OUTPUT ACCEPT [0:0]
:Always - [0:0]
:Allow - [0:0]
:Bogus - [0:0]
:Enemies - [0:0]
:Friends - [0:0]

-A INPUT -j Bogus
-A INPUT -j Always
-A INPUT -j Enemies
-A INPUT -j Allow

-A FORWARD -j Bogus
-A FORWARD -j Always
-A FORWARD -j Enemies
-A FORWARD -j Allow
 */
public class Main {

  // Chain "Alyssa" doesn't have a default action
  // :Alyssa - [0:0]
  // Unconditional jump to Alyssa Chain
  // -A INPUT -j Alyssa
  // -A FORWARD -j Alyssa

  // -A FORWARD -p tcp -m mac --mac-source 00:00:00:00:00:00 -m webstr --url hulu -j REJECT --reject-with tcp-reset -m time --kerneltz --timestart 07:45 --timestop 11:00 --weekdays Mon,Tue,Wed,Thu,Fri
  // -j RETURN
  // Chain "Joel" doesn't have a default action
  // :Joel - [0:0]
  // Unconditional jump to Joel filter
  // -A INPUT -j Joel
  // -A FORWARD -j Joel

  // -A Alyssa -p tcp -m mac ! --mac-source 00:00:00:00:00:00 -j RETURN
  // -A Alyssa -p tcp -m time --kerneltz --timestart 18:45 --timestop 18:46 --weekdays Mon,Tue,Wed,Thu,Fri -j RETURN
  // -A Alyssa -p tcp -m webstr --url hulu -j REJECT --reject-with tcp-reset

  // # Creates a new user-defined chain named "Adam"
  // iptables -N Adam

  // # Forwards traffic from the INPUT chain to the Adam chain
  // iptables -I INPUT -j Adam

  // # Forwards traffic from the FORWARD chain to the Adam chain
  // iptables -I FORWARD -j Adam

  // # Looks for a matching MAC address and returns if it doesn't match
  // iptables -A Adam -p tcp -m mac ! --mac-source 00:00:00:00:00:00 -j RETURN

  // # Reject all traffic for the above MAC address between 6:45PM - 6:48PM in the timezone of the router
  // iptables -A Adam -p tcp -m time --kerneltz --timestart 18:45 --timestop 18:48 --weekdays Mon,Tue,Wed,Thu,Fri -j REJECT

  //                                          Matching component                 Action component
  //                               ____________________|________________________       |
  //                              |                                            |       |
  //Rule -> iptables -A JoelsOmen -p tcp -m mac ! --mac-source 00:00:00:00:00:00 -j RETURN

  private final Properties properties;
  private final Device tv1;
  private final Device tv2;

  public Main(final Properties properties) {
    this.properties = properties;

    tv1 = new Device("TV1", new MacAddress(properties.getProperty("TV1.mac")));
    tv2 = new Device("TV2", new MacAddress(properties.getProperty("TV2.mac")));
  }

  public static void main(String[] args) throws IOException {
    final Properties properties = new Properties();
    properties.load(new FileReader("secrets.properties"));
    new Main(properties).run();
  }

  private void run() {

    List<Chain> chains = new ArrayList<>();

    List<Device> joelsDevices = createJoelsDevices();
    Schedule joelsVirtualSchedule = setUpJoelSchedule();
    Device joelsIPhone = new Device("Joel's iPhone", new MacAddress(properties.getProperty("joel.iphone.mac")));


    Device joelsOmen = new Device("Joel's Omen", new MacAddress(properties.getProperty("joel.omen.mac")));


    Chain joelsOmenChain = new Chain(Util.sanitize(joelsOmen.getName()));


    Match match = new MacAddressMatch(joelsOmen.getMacAddress()).not();
//    ActionComponent actionComponent = new DenyActionComponent();
    ActionComponent returnActionComponent = new ActionComponent.ReturnActionComponent();
    ActionComponent tcpRejectActionComponent = new ActionComponent.RejectActionComponent(false);
    ActionComponent udpRejectActionComponent = new ActionComponent.RejectActionComponent(false);
    ActionComponent dropActionComponent = new ActionComponent.DropActionComponent();


    Rule returnMacNotMatching = createRule(returnActionComponent, match);

    final Match macAddressMatch = new MacAddressMatch(joelsOmen.getMacAddress());
    final Match discordTcpMatch = new TcpKeywordMatch("discord");
    final Match discordUdp1Match = new Udp1KeywordMatch("discord");
    final Match discordUdp2Match = new Udp2KeywordMatch("discord", properties.getProperty("router.ip") +  "/32");
    Rule discordTcpRule = createRule(tcpRejectActionComponent, discordTcpMatch);
    Rule discordUdp1Rule = createRule(udpRejectActionComponent, discordUdp1Match);
    Rule discordUdp2Rule = createRule(udpRejectActionComponent, discordUdp2Match);

    joelsOmenChain.add(returnMacNotMatching);
    joelsOmenChain.add(discordTcpRule);
    joelsOmenChain.add(discordUdp1Rule);
    joelsOmenChain.add(discordUdp2Rule);

    ScriptWriter scriptWriter = new ScriptWriter();
    scriptWriter.add(joelsOmenChain);
    System.out.println(scriptWriter);


//      chains.add(new Chain(sanitize(device.getName()))
//                     .setDevice(device)
//                     .setSchedule(joelsVirtualSchedule)
//                     .addKeyword("discord")
//                     .addKeyword("disney")
//                     .addKeyword("netflix")
//                     .addKeyword("youtube"));


    writeToFile("Joel", chains);

    chains.clear();


//    List<Device> alyssasDevices = createAlyssasDevices();
//    Schedule alyssasVirtualSchedule = setUpAlyssasSchedule();
//
//    for (Device device : alyssasDevices) {
////      chains.add(new Chain(sanitize(device.getName()))
////                     .setDevice(device)
////                     .setSchedule(alyssasVirtualSchedule)
////                     .addKeyword("tictok"));
//
//      chains.add(new Chain(Util.sanitize(device.getName()))
//                     .setDevice(device)
//                     .setTimeSchedule(lateNight()));
//    }
//
//    writeToFile("Alyssa", chains);

//    List<Device> alyssasDevices = createAlyssasDevices();
//    Schedule alyssasSchedule = setUpAlyssasSchedule();
//
//    for (Device device : alyssasDevices) {
//      List.of("netflix", "hulu", "facebook").forEach(keyword -> {
//        Rule blockKeywordDuringSchool = new Rule(keyword, device).at(alyssasSchedule);
//        rules.add(blockKeywordDuringSchool);
//      });
//
//      Rule blockTikTokAlways = new Rule("tiktok", device);
//      rules.add(blockTikTokAlways);
//    }

//    writeToFile("Alyssa", rules);
  }

  private Rule createRule(final ActionComponent actionComponent, final Match... matches) {
    Rule rule = new Rule(actionComponent);
    rule.addMatchingComponents(matches);
    return rule;
  }

  private TimeSchedule lateNight() {
    LocalTime midnight = LocalTime.MIDNIGHT;
    LocalTime sixAM = LocalTime.of(6, 0);
    LocalTime elevenPM = LocalTime.of(23, 0);

    TimeRange earlyMorning = new TimeRange(midnight, sixAM);
    TimeRange lateNight = new TimeRange(elevenPM, midnight);

    TimeSchedule ts = new TimeSchedule();
    ts.add(earlyMorning);
    ts.add(lateNight);
    return ts;
  }

  private void writeToFile(final String name, final List<Chain> chains) {

    try (FileWriter fileWriter = new FileWriter(name.toLowerCase() + ".sh")) {
      for (Chain chain : chains) {
        fileWriter.write(chain.toString() + System.lineSeparator());
      }

    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

//  private void writeToFile(final String name, final ArrayList<Rule> rules) {
//
//    // change grouping - first by person, then MAC address, then schedule, then keyword
//
//    try (FileWriter fileWriter = new FileWriter(name.toLowerCase() + ".sh")) {
//
//      fileWriter.write("# " + StringUtils.repeat('#', 70) + "\n");
//      fileWriter.write("# " + StringUtils.center(" " + name + " ", 70, '#') + "\n");
//      fileWriter.write("# " + StringUtils.repeat('#', 70) + "\n");
//
//      rules.stream().map(rule -> rule.getDevice()).distinct().forEach(device -> {
//
//        rules.stream().filter(rule -> rule.getDevice().equals(device)).forEach(rule2 -> {
//          try {
//            String firstLine = "#".repeat(10) + "  Device:  " + device.toString() + "  " + "#".repeat(10);
//            fileWriter.write(firstLine + System.lineSeparator());
//
//            // iptables -N Adam
//            // iptables -I INPUT -j Adam
//            // iptables -I FORWARD -j Adam
//            // iptables -A Adam -p tcp -m mac ! --mac-source 00:00:00:00:00:00 -j RETURN
//            // iptables -A Adam -p tcp -m time --kerneltz --timestart 18:45 --timestop 18:48 --weekdays Mon,Tue,Wed,Thu,Fri -j REJECT
//            String filterName = sanitize(device.toString());
//            fileWriter.write("iptables -N " + filterName + System.lineSeparator());
//            fileWriter.write("iptables -I INPUT -j " + filterName + System.lineSeparator());
//            fileWriter.write("iptables -I FORWARD -j " + filterName + System.lineSeparator());
//            fileWriter.write(
//                "iptables -A " + filterName + " -p tcp -m mac ! --mac-source " + device.get().getMacAddress()
//                    + " -j RETURN" + System.lineSeparator());
//
//            fileWriter.write(rule2.toString());
//          }
//          catch (IOException e) {
//            e.printStackTrace();
//          }
//        });
//      });
//
//    }
//    catch (IOException e) {
//      e.printStackTrace();
//    }
//  }


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

    Device laptop = new Device("Alyssa's laptop", new MacAddress(properties.getProperty("alyssa.laptop.mac")));
    Device iphone = new Device("Alyssa's iPhone", new MacAddress(properties.getProperty("alyssa.iphone.mac")));
    Device ipad = new Device("Alyssa's iPad", new MacAddress(properties.getProperty("alyssa.ipad.mac")));
    Device schoolIPad = new Device("Alyssa's School iPad", new MacAddress(properties.getProperty("alyssa.school_ipad.mac")));

    devices.add(laptop);
    devices.add(iphone);
    devices.add(ipad);
    devices.add(schoolIPad);
    devices.add(tv1);
    devices.add(tv2);
    return devices;
  }

  private List<Device> createJoelsDevices() {
    List<Device> devices = new ArrayList<>(2);

    Device joelsIPhone = new Device("Joel's iPhone", new MacAddress(properties.getProperty("joel.iphone.mac")));
    Device joelsOmen = new Device("Joel's Omen", new MacAddress(properties.getProperty("joel.omen.mac")));
//    Device hpComputer = new Device("HP Computer", new MACAddress(properties.getProperty("hp_computer.mac")));

    devices.add(joelsIPhone);
    devices.add(joelsOmen);
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
