package com.adamkoch.iptables

import com.adamkoch.iptables.matches.*
import com.adamkoch.iptables.objects.MacAddress
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class RuleTest {

    @Test
    fun test() {
        val rule = Rule(Target.REJECT_WITH_TCP_RESET)
        val match: Match = MacAddressMatch(MacAddress.DUMMY)
        rule.addMatch(match)
        Assertions.assertEquals(
            "-m mac --mac-source 00:00:00:A1:2B:CC -j REJECT --reject-with tcp-reset",
            rule.asString()
        )
    }

    @Test
    fun test2() {
        val rule = Rule(Target.REJECT)

        rule.addMatch(MacAddressMatch(MacAddress.DUMMY))
//        rule.addMatch(Udp1KeywordMatchSet("test"))
        rule.addMatch(*Udp2KeywordMatchSet("foo", "0.0.0.0").getMatches().toTypedArray())
        rule.addMatch(DateTimeMatch()
            .apply { setStart(LocalDate.of(2021, 1, 1)) }
            .apply { setEnd(LocalDate.of(2022, 12, 31)) }
        )

        Assertions.assertEquals(
            "-i br0 -p udp -m udp --dport 53 -d 0.0.0.0 -m mac --mac-source 00:00:00:A1:2B:CC -m string --string \"foo\" --algo bm --to 65535 --icase -m time --datestart 2021-01-01 --datestop 2022-12-31 -j REJECT",
            rule.asString()
        )
    }
}