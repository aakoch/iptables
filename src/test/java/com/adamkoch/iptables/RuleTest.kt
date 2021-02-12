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
            "-m mac --mac-source 00:00:00:a1:2b:cc -j REJECT --reject-with tcp-reset",
            rule.asString()
        )
    }

    @Test
    fun test2() {
        val rule = Rule(Target.REJECT_WITH_TCP_RESET)

        rule.addMatch(MacAddressMatch(MacAddress.DUMMY))
        rule.addMatch(Udp1KeywordMatch("test"))
        rule.addMatch(Udp2KeywordMatch("foo", "0.0.0.0"))
        rule.addMatch(DateTimeMatch()
            .apply { setStart(LocalDate.of(2021, 1, 1)) }
            .apply { setEnd(LocalDate.of(2022, 12, 31)) }
        )

        Assertions.assertEquals(
            "-m mac --mac-source 00:00:00:a1:2b:cc -i br0 -p udp -m udp --dport 53 -m string --string \"test\" --algo bm --to 65535 --icase -d 0.0.0.0 -i br0 -p udp -m udp --dport 53 -m string --string \"foo\" --algo bm --to 65535 --icase -m time --datestart 2021-01-01 --datestop 2022-12-31 -j REJECT --reject-with tcp-reset",
            rule.asString()
        )
    }
}