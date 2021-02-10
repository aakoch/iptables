package com.adamkoch.iptables

import com.adamkoch.iptables.Util.sanitize
import com.adamkoch.iptables.matches.*
import com.adamkoch.iptables.objects.MacAddress
import com.adamkoch.iptables.objects.Protocol
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
            .returnIfNot(MacAddress(properties.getProperty("joel.omen.mac")))
            .ifBetweenLocal(8, 0, 11, 0)
            .ifContains("discord")
            .reject()
            .ifBetweenLocal(12, 0, 15, 0)
            .ifContains("discord")
            .reject()
            .ifBetweenLocal(8, 0, 11, 0)
            .ifIp("159.153.191.238")
            .ifProtocol(Protocol.TCP)
            .reject()
            .ifBetweenLocal(12, 0, 15, 0)
            .ifIp("159.153.191.238")
            .ifProtocol(Protocol.TCP)
            .reject()




        val omenChain = Chain("Joel's Omen")
        val shortCircuitMacAddress = MacAddress(properties.getProperty("joel.omen.mac"))
        val shortCircuitMacAddressMatch = MacAddressMatch(shortCircuitMacAddress).not();
        val shortCircuitMacAddressRule = Rule(Target.RETURN)
        shortCircuitMacAddressRule.addMatch(shortCircuitMacAddressMatch)

        omenChain.add(shortCircuitMacAddressRule)

        val schoolMornings = TimeSchedule()

        val schoolStartTime = LocalTime.of(8, 0)
        val lunchStartTime = LocalTime.of(11, 0)

        schoolMornings.add(TimeRange(schoolStartTime, lunchStartTime))
        val schoolMorningsMatch = TimeExtensionMatch()
        schoolMorningsMatch.setStart(schoolStartTime)
        schoolMorningsMatch.setEnd(lunchStartTime)
        schoolMorningsMatch.setUseKernelTZ(true)

        val schoolAfternoonsMatch = TimeExtensionMatch()

        val lunchEndTime = LocalTime.of(12, 0)
        val schoolEndTime = LocalTime.of(15, 0)

        schoolAfternoonsMatch.setStart(lunchEndTime)
        schoolAfternoonsMatch.setEnd(schoolEndTime)
        schoolAfternoonsMatch.setUseKernelTZ(true)

        val discordTcpKeywordMatch = TcpKeywordMatch("discord")

        val preventDiscordMorningsRule = Rule(Target.REJECT_WITH_RESET)
        preventDiscordMorningsRule.addMatch(discordTcpKeywordMatch, schoolMorningsMatch)
        omenChain.add(preventDiscordMorningsRule)

        val preventDiscordAfternoonsRule = Rule(Target.REJECT_WITH_RESET)
        preventDiscordAfternoonsRule.addMatch(discordTcpKeywordMatch, schoolAfternoonsMatch)
        omenChain.add(preventDiscordAfternoonsRule)


        val destinationMatch = DestinationMatch("159.153.191.238")

        val morningsDestinationMatchRule = Rule(Target.REJECT_WITH_RESET)
        morningsDestinationMatchRule.addMatch(ProtocolMatch.TCP, destinationMatch, schoolMorningsMatch)
        omenChain.add(morningsDestinationMatchRule)


        val afternoonsDestinationMatchRule = Rule(Target.REJECT_WITH_RESET)
        afternoonsDestinationMatchRule.addMatch(ProtocolMatch.TCP, destinationMatch, schoolAfternoonsMatch)
        omenChain.add(afternoonsDestinationMatchRule)

        println(omenChain.toString())
        println(chainBuilder.toString())

        assertEquals(System.lineSeparator() + omenChain.toString(), System.lineSeparator() + chainBuilder.toString())

        val writer = ScriptWriter("A")
        writer.add(omenChain)
        println(writer.toString())
    }


    @Test
    fun basic() {
        val chain = Chain(INVALID_CHAIN_NAME)
        chain.add(macAddrRule)
        chain.add(webstrRule)
        Assertions.assertEquals(
            "$SANITIZED_CHAIN_NAME -m mac --mac-source 00:00:00:a1:2b:cc -j DROP" + System.lineSeparator()
                    + "$SANITIZED_CHAIN_NAME -m webstr --url keyword -j REJECT --reject-with tcp-reset",
            chain.toString()
        )
    }

    @Test
    fun inverse() {
        val chain = Chain(INVALID_CHAIN_NAME)
        chain.add(allowMacAddrRule)
        chain.add(webstrRule)
        Assertions.assertEquals(
            "$SANITIZED_CHAIN_NAME -m mac --mac-source 00:00:00:a1:2b:cc -j ACCEPT" + System.lineSeparator()
                    + "$SANITIZED_CHAIN_NAME -m webstr --url keyword -j REJECT --reject-with tcp-reset",
            chain.toString()
        )
    }

    @Test
    fun shortCircuit() {
        val chain = Chain(INVALID_CHAIN_NAME)
        chain.add(returnWhenNotMacAddrRule)
        chain.add(webstrRule)
        Assertions.assertEquals(
            "$SANITIZED_CHAIN_NAME -m mac ! --mac-source 00:00:00:a1:2b:cc -j RETURN" + System.lineSeparator()
                    + "$SANITIZED_CHAIN_NAME -m webstr --url keyword -j REJECT --reject-with tcp-reset",
            chain.toString()
        )


        // -A Alyssa -p tcp -m mac ! --mac-source 00:00:00:00:00:00 -j RETURN
        // -A Alyssa -p tcp -m time --kerneltz --timestart 18:45 --timestop 18:46 --weekdays Mon,Tue,Wed,Thu,Fri -j RETURN
        // -A Alyssa -p tcp -m webstr --url hulu -j REJECT --reject-with tcp-reset
    }

    private val webstrRule: Rule
        get() {
            val rule = Rule(Target.REJECT_WITH_RESET)
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