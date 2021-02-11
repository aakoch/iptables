package com.adamkoch.iptables

import java.util.Properties
import com.adamkoch.iptables.objects.Device
import com.adamkoch.iptables.objects.MacAddress
import com.adamkoch.iptables.DayOfWeekSchedule.Companion.WEEKDAYS
import java.time.LocalTime
import java.io.FileWriter
import java.io.IOException
import com.adamkoch.iptables.matches.*
import kotlin.Throws
import kotlin.jvm.JvmStatic
import java.io.FileReader
import java.time.DayOfWeek
import java.util.ArrayList

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
class Main(
    private val properties: Properties
) {
    private val tv1: Device
    private val tv2: Device
    private fun run() {


        val chains: MutableList<Chain> = ArrayList()
        val joelsDevices = createJoelsDevices()
        val joelsVirtualSchedule = setUpJoelSchedule()
        val joelsIPhone =
            Device("Joel's iPhone", MacAddress(properties.getProperty("joel.iphone.mac")))
        val joelsOmen = Device("Joel's Omen", MacAddress(properties.getProperty("joel.omen.mac")))
        val joelsOmenChain = Chain(Util.sanitize(joelsOmen.name))
        val match = MacAddressMatch(joelsOmen.macAddress).not()
        //    ActionComponent actionComponent = new DenyActionComponent();
        val returnActionComponent = Target.RETURN
        val tcpRejectActionComponent = Target.REJECT
        val udpRejectActionComponent = Target.REJECT
        val dropActionComponent = Target.DROP
        val returnMacNotMatching = createRule(returnActionComponent, match)
        val macAddressMatch: Match = MacAddressMatch(joelsOmen.macAddress)
        val discordTcpMatch: Match = TcpKeywordMatch("discord")
        val discordUdp1Match: Match = Udp1KeywordMatch("discord")
        val discordUdp2Match: Match =
            Udp2KeywordMatch("discord", properties.getProperty("router.ip") + "/32")
        val discordTcpRule = createRule(tcpRejectActionComponent, discordTcpMatch)
        val discordUdp1Rule = createRule(udpRejectActionComponent, discordUdp1Match)
        val discordUdp2Rule = createRule(udpRejectActionComponent, discordUdp2Match)
        joelsOmenChain.add(returnMacNotMatching)
        joelsOmenChain.add(discordTcpRule)
        joelsOmenChain.add(discordUdp1Rule)
        joelsOmenChain.add(discordUdp2Rule)
        val scriptWriter = ScriptWriter(CommandOption.APPEND)
        scriptWriter.add(joelsOmenChain)
        println(scriptWriter)


//      chains.add(new Chain(sanitize(device.getName()))
//                     .setDevice(device)
//                     .setSchedule(joelsVirtualSchedule)
//                     .addKeyword("discord")
//                     .addKeyword("disney")
//                     .addKeyword("netflix")
//                     .addKeyword("youtube"));
        writeToFile("Joel", chains)
        chains.clear()


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

    private fun createRule(target: Target, vararg matches: Match): Rule {
        val rule = Rule(target)
        rule.addMatch(*matches)
        return rule
    }

    private fun lateNight(): TimeSchedule {
        val midnight = LocalTime.MIDNIGHT
        val sixAM = LocalTime.of(6, 0)
        val elevenPM = LocalTime.of(23, 0)
        val earlyMorning = TimeRange(midnight, sixAM)
        val lateNight = TimeRange(elevenPM, midnight)
        val ts = TimeSchedule()
        ts.add(earlyMorning)
        ts.add(lateNight)
        return ts
    }

    private fun writeToFile(name: String, chains: List<Chain>) {
        try {
            FileWriter(name.toLowerCase() + ".sh").use { fileWriter ->
                for (chain in chains) {
                    fileWriter.write(chain.toString() + System.lineSeparator())
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
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
    private fun setUpAlyssasSchedule(): Schedule {
        val startOfSchool = LocalTime.of(7, 45)
        val lunchStart = LocalTime.of(11, 0)
        val lunchEnd = LocalTime.of(12, 0)
        val endOfSchool = LocalTime.of(15, 45)
        val morningSchedule = DayOfWeekSchedule(WEEKDAYS.toSet() as Set<DayOfWeek>, TimeRange(startOfSchool, lunchStart))
        val afternoonSchedule = DayOfWeekSchedule(WEEKDAYS.toSet() as Set<DayOfWeek>, TimeRange(lunchEnd, endOfSchool))
        val schedule = Schedule()
        schedule.add(morningSchedule)
        schedule.add(afternoonSchedule)
        return schedule
    }

    private fun createAlyssasDevices(): List<Device> {
        val devices: MutableList<Device> = ArrayList(3)
        val laptop =
            Device("Alyssa's laptop", MacAddress(properties.getProperty("alyssa.laptop.mac")))
        val iphone =
            Device("Alyssa's iPhone", MacAddress(properties.getProperty("alyssa.iphone.mac")))
        val ipad = Device("Alyssa's iPad", MacAddress(properties.getProperty("alyssa.ipad.mac")))
        val schoolIPad = Device(
            "Alyssa's School iPad",
            MacAddress(properties.getProperty("alyssa.school_ipad.mac"))
        )
        devices.add(laptop)
        devices.add(iphone)
        devices.add(ipad)
        devices.add(schoolIPad)
        devices.add(tv1)
        devices.add(tv2)
        return devices
    }

    private fun createJoelsDevices(): List<Device> {
        val devices: MutableList<Device> = ArrayList(2)
        val joelsIPhone =
            Device("Joel's iPhone", MacAddress(properties.getProperty("joel.iphone.mac")))
        val joelsOmen = Device("Joel's Omen", MacAddress(properties.getProperty("joel.omen.mac")))
        //    Device hpComputer = new Device("HP Computer", new MACAddress(properties.getProperty("hp_computer.mac")));
        devices.add(joelsIPhone)
        devices.add(joelsOmen)
        return devices
    }

    private fun setUpJoelSchedule(): Schedule {
        val startOfSchool = LocalTime.of(7, 45)
        val lunchStart = LocalTime.of(11, 0)
        val lunchEnd = LocalTime.of(12, 0)
        val endOfSchool = LocalTime.of(15, 0)
        val morningSchedule = DayOfWeekSchedule(WEEKDAYS.toSet() as Set<DayOfWeek>, TimeRange(startOfSchool, lunchStart))
        val afternoonSchedule = DayOfWeekSchedule(WEEKDAYS.toSet() as Set<DayOfWeek>, TimeRange(lunchEnd, endOfSchool))
        val schedule = Schedule()
        schedule.add(morningSchedule)
        schedule.add(afternoonSchedule)
        return schedule
    }

    companion object {
        @Throws(IOException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val properties = Properties()
            properties.load(FileReader("secrets.properties"))
            Main(properties).run()
        }
    }

    init {
        tv1 = Device("TV1", MacAddress(properties.getProperty("TV1.mac")))
        tv2 = Device("TV2", MacAddress(properties.getProperty("TV2.mac")))
    }
}