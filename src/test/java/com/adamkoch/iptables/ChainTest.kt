package com.adamkoch.iptables

import com.adamkoch.iptables.ActionComponent.*
import com.adamkoch.iptables.Util.sanitize
import com.adamkoch.iptables.matches.*
import com.adamkoch.iptables.objects.MacAddress
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.FileReader
import java.time.LocalTime
import java.util.*
import kotlin.test.assertEquals

internal class ChainTest {

    @Test
    fun morningSchoolTime() {
        val properties = Properties()
        properties.load(FileReader("secrets.properties"))

        val chainBuilder = ChainBuilder("Joel's Omen")
            .returnIf(MacAddress(properties.getProperty("joel.omen.mac")))
            .ifBetween(8, 0, 11, 0)
            .ifContains("discord")
            .reject()
            .ifBetween(12, 0, 15, 0)
            .ifContains("discord")
            .reject()

        val omenChain = Chain("Joel's Omen")
        val shortCircuitMacAddress = MacAddress(properties.getProperty("joel.omen.mac"))
        val shortCircuitMacAddressMatch = MacAddressMatch(shortCircuitMacAddress);
        val shortCircuitMacAddressRule = Rule(ActionComponent.ReturnActionComponent())
        shortCircuitMacAddressRule.addMatch(shortCircuitMacAddressMatch)

        omenChain.add(shortCircuitMacAddressRule)

        val schoolMornings = TimeSchedule()

        val schoolStartTime = LocalTime.of(8, 0)
        val lunchStartTime = LocalTime.of(11, 0)

        schoolMornings.add(TimeRange(schoolStartTime, lunchStartTime))
        val schoolMorningsMatch = TimeExtensionMatch()
        schoolMorningsMatch.setStart(schoolStartTime)
        schoolMorningsMatch.setEnd(lunchStartTime)

        val schoolAfternoonsMatch = TimeExtensionMatch()

        val lunchEndTime = LocalTime.of(12, 0)
        val schoolEndTime = LocalTime.of(15, 0)

        schoolAfternoonsMatch.setStart(lunchEndTime)
        schoolAfternoonsMatch.setEnd(schoolEndTime)

        val discordTcpKeywordMatch = TcpKeywordMatch("discord")

        val preventDiscordMorningsRule = Rule(ActionComponent.RejectActionComponent())
        preventDiscordMorningsRule.addMatch(discordTcpKeywordMatch, schoolMorningsMatch)
        omenChain.add(preventDiscordMorningsRule)

        val preventDiscordAfternoonsRule = Rule(ActionComponent.RejectActionComponent())
        preventDiscordAfternoonsRule.addMatch(discordTcpKeywordMatch, schoolAfternoonsMatch)
        omenChain.add(preventDiscordAfternoonsRule)

        System.out.println(omenChain.toString())
        System.out.println(chainBuilder.toString())

        assertEquals(omenChain.toString(), chainBuilder.toString())

        val writer = ScriptWriter()
        writer.add(omenChain)
        println(writer.toString())
    }


    @Test
    fun basic() {
        val chain = Chain(INVALID_CHAIN_NAME)
        chain.add(macAddrRule)
        chain.add(webstrRule)
        Assertions.assertEquals(
            "Adams_Computer -m mac --mac-source 00:00:00:a1:2b:cc -j DROP" + System.lineSeparator()
                    + "Adams_Computer -m webstr --url keyword -j REJECT --reject-with tcp-reset",
            chain.toString()
        )
    }

    @Test
    fun inverse() {
        val chain = Chain(INVALID_CHAIN_NAME)
        chain.add(allowMacAddrRule)
        chain.add(webstrRule)
        Assertions.assertEquals(
            "Adams_Computer -m mac --mac-source 00:00:00:a1:2b:cc -j ACCEPT" + System.lineSeparator()
                    + "Adams_Computer -m webstr --url keyword -j REJECT --reject-with tcp-reset",
            chain.toString()
        )
    }

    @Test
    fun shortCircuit() {
        val chain = Chain(INVALID_CHAIN_NAME)
        chain.add(returnWhenNotMacAddrRule)
        chain.add(webstrRule)
        Assertions.assertEquals(
            "Adams_Computer -m mac ! --mac-source 00:00:00:a1:2b:cc -j RETURN" + System.lineSeparator()
                    + "Adams_Computer -m webstr --url keyword -j REJECT --reject-with tcp-reset",
            chain.toString()
        )


        // -A Alyssa -p tcp -m mac ! --mac-source 00:00:00:00:00:00 -j RETURN
        // -A Alyssa -p tcp -m time --kerneltz --timestart 18:45 --timestop 18:46 --weekdays Mon,Tue,Wed,Thu,Fri -j RETURN
        // -A Alyssa -p tcp -m webstr --url hulu -j REJECT --reject-with tcp-reset
    }

    private val webstrRule: Rule
        private get() {
            val rule = Rule(RejectActionComponent())
            val match: Match = WebStringExtensionMatch("keyword")
            rule.addMatch(match)
            return rule
        }
    private val macAddrRule: Rule
        private get() {
            val rule = Rule(DropActionComponent())
            rule.addMatch(MacAddressMatch(MacAddress.DUMMY))
            return rule
        }
    private val allowMacAddrRule: Rule
        private get() {
            val rule = Rule(AcceptActionComponent())
            rule.addMatch(MacAddressMatch(MacAddress.DUMMY))
            return rule
        }
    private val returnWhenNotMacAddrRule: Rule
        private get() {
            val rule = Rule(ReturnActionComponent())
            rule.addMatch(MacAddressMatch(MacAddress.DUMMY).not())
            return rule
        }

    companion object {
        const val INVALID_CHAIN_NAME = "Adam's Computer"
        val SANITIZED_CHAIN_NAME = sanitize(INVALID_CHAIN_NAME)
    }
}