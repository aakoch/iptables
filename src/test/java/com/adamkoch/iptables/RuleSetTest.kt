package com.adamkoch.iptables

import com.adamkoch.iptables.matches.ProtocolMatch
import com.adamkoch.iptables.matches.DateTimeMatch
import com.adamkoch.iptables.matches.WebStringExtensionMatch
import com.adamkoch.iptables.objects.MacAddress
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalTime
import kotlin.test.assertEquals

internal class RuleSetTest {


    @Test
    fun testRuleSet() {
        //  -m time --kerneltz --timestart 07:45 --timestop 11:00 --weekdays Mon,Tue,Wed,Thu,Fri
        val ruleSet = RuleSet("discord", "FORWARD", MacAddress("00:00:00:00:01"), "255.255.255.255")
        val actual = ruleSet.toString()
        Assertions.assertEquals(
            """-p tcp -m webstr --url discord -m mac --mac-source 00:00:00:00:01 -j REJECT --reject-with tcp-reset
-i br0 -p udp -m udp --dport 53 -m string --string "discord" --algo bm --to 65535 --icase -m mac --mac-source 00:00:00:00:01 -j REJECT --reject-with tcp-reset
-d 255.255.255.255/32 -i br0 -p udp -m udp --dport 53 -m string --string "discord" --algo bm --to 65535 --icase -m mac --mac-source 00:00:00:00:01 -j REJECT --reject-with tcp-reset""",
            actual
        )
    }

    @Test
    fun lunchBreakSchedule() {
        val multipleTimeRangesRuleSet = MultipleTimeRangesRuleSet()
        multipleTimeRangesRuleSet.run {
            dateTimeExtensionMatch1 = timeRangeMatch(8, 0, 11, 0)
            dateTimeExtensionMatch2 = timeRangeMatch(12, 0, 15, 0)
            match1 = ProtocolMatch.TCP
            match2 = WebStringExtensionMatch("discord")
        }
        assertEquals(
            "-p tcp -m time --timestart 08:00 --timestop 11:00 -m webstr --url discord -j REJECT --reject-with tcp-reset" + System.lineSeparator() +
                    "-p tcp -m time --timestart 12:00 --timestop 15:00 -m webstr --url discord -j REJECT --reject-with tcp-reset",
            multipleTimeRangesRuleSet.asString()
        )
    }

    private fun timeRangeMatch(
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int
    ): DateTimeMatch {

        val timeExtensionMatch = DateTimeMatch()
        timeExtensionMatch.setStart(LocalTime.of(startHour, startMinute))
        timeExtensionMatch.setEnd(LocalTime.of(endHour, endMinute))
        return timeExtensionMatch
    }

}