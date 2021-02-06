package com.adamkoch.iptables

import com.adamkoch.iptables.ActionComponent.RejectActionComponent
import com.adamkoch.iptables.matches.MacAddressMatch
import com.adamkoch.iptables.matches.Match
import com.adamkoch.iptables.objects.MacAddress
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class RuleTest {
    @Test
    fun test() {
        val rule = Rule(RejectActionComponent()) // ugly
        val match: Match = MacAddressMatch(MacAddress.DUMMY)
        rule.addMatch(match)
        Assertions.assertEquals(
            "-m mac --mac-source 00:00:00:a1:2b:cc -j REJECT --reject-with tcp-reset",
            rule.asString()
        )
    }
}