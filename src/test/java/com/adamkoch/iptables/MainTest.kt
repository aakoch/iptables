package com.adamkoch.iptables

import com.adamkoch.iptables.Util.sanitize
import com.adamkoch.iptables.matches.MacAddressMatch
import com.adamkoch.iptables.matches.Match
import com.adamkoch.iptables.objects.Device
import com.adamkoch.iptables.objects.MacAddress
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class MainTest {
    @Test
    fun testSimpleChain() {
        val joelsOmen = Device("Joel's Omen", MacAddress("00:00:00:00:00:00"))
        val match = MacAddressMatch(joelsOmen.macAddress).not()
        val target = Target.RETURN
        val returnMacNotMatchingRule = createRule(target, match)
        Assertions.assertEquals(
            "-m mac ! --mac-source 00:00:00:00:00:00 -j RETURN",
            returnMacNotMatchingRule.asString()
        )
        val name = sanitize("Joel's Omen")
        val chain = Chain("Joel's Omen")
        chain.add(returnMacNotMatchingRule)
        Assertions.assertEquals(
            "$name -m mac ! --mac-source 00:00:00:00:00:00 -j RETURN",
            chain.toString()
        )
        val sc = ScriptWriter(CommandOption.APPEND)
        sc.add(chain)
        Assertions.assertEquals(
            """
    # Creates a new user-defined chain named Joels_Omen
    iptables -N Joels_Omen
    # Adds jump point for INPUT chain
    iptables -I INPUT -j Joels_Omen
    # Adds jump point for FORWARD chain
    iptables -I FORWARD -j Joels_Omen
    iptables -A Joels_Omen -m mac ! --mac-source 00:00:00:00:00:00 -j RETURN${System.lineSeparator()}
    """.trimIndent(), sc.toString()
        )

        //            // iptables -N Adam
//            // iptables -I INPUT -j Adam
//            // iptables -I FORWARD -j Adam
//            // iptables -A Adam -p tcp -m mac ! --mac-source 00:00:00:00:00:00 -j RETURN
//            // iptables -A Adam -p tcp -m time --kerneltz --timestart 18:45 --timestop 18:48 --weekdays Mon,Tue,Wed,Thu,Fri -j REJECT
    }

    private fun createRule(target: Target, match: Match): Rule {
        val rule = Rule(target)
        rule.addMatch(match)
        return rule
    }
}