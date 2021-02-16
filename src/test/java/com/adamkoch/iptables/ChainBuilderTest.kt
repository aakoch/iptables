package com.adamkoch.iptables

import com.adamkoch.iptables.Util.sanitize
import com.adamkoch.iptables.matches.MacAddressMatch
import com.adamkoch.iptables.matches.Match
import com.adamkoch.iptables.matches.WebStringExtensionMatch
import com.adamkoch.iptables.objects.MacAddress
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ChainBuilderTest {

    @Test
    fun keyword() {
        val joelOmenChain = ChainBuilder("Joel's Omen")
            .ifContains("discord")
            .reject()
            .build()

        assertEquals(
            "Joels_Omen -p tcp -m webstr --url discord -j REJECT",
            joelOmenChain.asString()
        )
    }

    @Test
    fun keywordPlus() {
        val joelOmenChain = ChainBuilder("Joel's Omen")
            .returnIfNot(MacAddress.DUMMY)
            .ifBetweenLocal(8, 0, 11, 0)
            .ifContains("discord")
            .rejectWithTcpReset()
            .build()

        assertEquals(
            "Joels_Omen -m mac ! --mac-source ${MacAddress.DUMMY} -j RETURN\n" +
                    "Joels_Omen -p tcp -m webstr --url discord -m time --kerneltz --timestart 08:00 --timestop 11:00 -j REJECT --reject-with tcp-reset",
            joelOmenChain.asString()
        )
    }

    @Test
    fun udpKeyword1() {
        val joelOmenChain = ChainBuilder("Joel's Omen")
            .returnIfNot(MacAddress.DUMMY)
            .ifBetweenLocal(8, 0, 11, 0)
            .udpContains("discord")
            .reject()
            .build()

        assertEquals(
            "Joels_Omen -m mac ! --mac-source 00:00:00:A1:2B:CC -j RETURN\n" +
                    "Joels_Omen -i br0 -p udp -m udp --dport 53 -m string --string \"discord\" --algo bm --to 65535 --icase -m time --kerneltz --timestart 08:00 --timestop 11:00 -j REJECT",
            joelOmenChain.asString()
        )
    }

    @Test
    fun udpKeyword2() {
        val joelOmenChain = ChainBuilder("Joel's Omen")
            .returnIfNot(MacAddress.DUMMY)
            .udp2Contains("discord", "192.168.1.1")
            .reject()
            .build()

        assertEquals(
            "Joels_Omen -m mac ! --mac-source 00:00:00:A1:2B:CC -j RETURN\n" +
                    "Joels_Omen -i br0 -p udp -m udp --dport 53 -d 192.168.1.1 -m string --string \"discord\" --algo bm --to 65535 --icase -j REJECT",
            joelOmenChain.asString()
        )
    }

    @Test
    fun rejectOnly() {
        val chain: Chain = ChainBuilder("Joel's Omen").reject()
            .build()
        assertEquals("Joels_Omen -j REJECT", chain.asString())
    }

    @Test
    fun returnIfMacAddress() {
        val chain: Chain = ChainBuilder("Joel's Omen")
            .returnIfNot(MacAddress.DUMMY)
            .reject()
            .build()
        assertEquals(
            "Joels_Omen -m mac ! --mac-source ${MacAddress.DUMMY} -j RETURN\nJoels_Omen -j REJECT",
            chain.asString()
        )
    }

    @Test
    fun rejectHours() {
        val chain: Chain = ChainBuilder("Joel's Omen")
            .returnIfNot(MacAddress.DUMMY)
            .ifBetweenLocal(8, 15, 11, 45)
            .reject()
            .build()
        assertEquals(
            "Joels_Omen -m mac ! --mac-source ${MacAddress.DUMMY} -j RETURN\n" +
                    "Joels_Omen -m time --kerneltz --timestart 08:15 --timestop 11:45 -j REJECT",
            chain.asString()
        )
    }

    @Test
    fun rejectKeywordWhenHours() {
        val chain: Chain = ChainBuilder("Joel's Omen")
            .returnIfNot(MacAddress.DUMMY)
            .ifBetweenLocal(8, 15, 11, 45)
            .ifContains("discord")
            .reject()
            .build()
        assertEquals(
            "Joels_Omen -m mac ! --mac-source ${MacAddress.DUMMY} -j RETURN\n" +
                    "Joels_Omen -p tcp -m webstr --url discord -m time --kerneltz --timestart 08:15 --timestop 11:45 -j REJECT",
            chain.asString()
        )
    }

    @Test
    fun rejectRule() {
        val chain: Chain = ChainBuilder("Joel's Omen")
            .returnIfNot(MacAddress.DUMMY)
            .rule(Rule(Target.REJECT))
            .build()
        assertEquals(
            "Joels_Omen -m mac ! --mac-source ${MacAddress.DUMMY} -j RETURN\n" +
                    "Joels_Omen -j REJECT", chain.asString()
        )
    }

    @Test
    fun basic() {
        val chain = Chain(INVALID_CHAIN_NAME)
        chain.add(macAddrRule)
        chain.add(webstrRule)
        assertEquals(
            "$SANITIZED_CHAIN_NAME -m mac --mac-source 00:00:00:A1:2B:CC -j DROP" + System.lineSeparator()
                    + "$SANITIZED_CHAIN_NAME -m webstr --url keyword -j REJECT --reject-with tcp-reset",
            chain.asString()
        )
    }

    @Test
    fun inverse() {
        val chain = Chain(INVALID_CHAIN_NAME)
        chain.add(allowMacAddrRule)
        chain.add(webstrRule)
        assertEquals(
            "$SANITIZED_CHAIN_NAME -m mac --mac-source 00:00:00:A1:2B:CC -j ACCEPT" + System.lineSeparator()
                    + "$SANITIZED_CHAIN_NAME -m webstr --url keyword -j REJECT --reject-with tcp-reset",
            chain.asString()
        )
    }

    @Test
    fun shortCircuit() {
        val chain = Chain(INVALID_CHAIN_NAME)
        chain.add(returnWhenNotMacAddrRule)
        chain.add(webstrRule)
        assertEquals(
            "$SANITIZED_CHAIN_NAME -m mac ! --mac-source 00:00:00:A1:2B:CC -j RETURN" + System.lineSeparator()
                    + "$SANITIZED_CHAIN_NAME -m webstr --url keyword -j REJECT --reject-with tcp-reset",
            chain.asString()
        )
    }

    private val webstrRule: Rule
        get() {
            val rule = Rule(Target.REJECT_WITH_TCP_RESET)
            val match: Match = WebStringExtensionMatch("keyword")
            rule.addMatch(match)
            return rule
        }
    private val macAddrRule: Rule
        get() {
            val rule = Rule(Target.DROP)
            rule.addMatch(MacAddressMatch(MacAddress.DUMMY))
            return rule
        }
    private val allowMacAddrRule: Rule
        get() {
            val rule = Rule(Target.ACCEPT)
            rule.addMatch(MacAddressMatch(MacAddress.DUMMY))
            return rule
        }
    private val returnWhenNotMacAddrRule: Rule
        get() {
            val rule = Rule(Target.RETURN)
            rule.addMatch(MacAddressMatch(MacAddress.DUMMY).not())
            return rule
        }

    companion object {
        const val INVALID_CHAIN_NAME = "Adam's Computer"
        val SANITIZED_CHAIN_NAME = sanitize(INVALID_CHAIN_NAME)
    }
}