package com.adamkoch.iptables

import com.adamkoch.iptables.ActionComponent.ReturnActionComponent
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
        val actionComponent: ActionComponent = ReturnActionComponent()
        val returnMacNotMatchingRule = createRule(actionComponent, match)
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
        val sc = ScriptWriter()
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

    private fun createRule(actionComponent: ActionComponent, match: Match): Rule {
        val rule = Rule(actionComponent)
        rule.addMatch(match)
        return rule
    }

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
}