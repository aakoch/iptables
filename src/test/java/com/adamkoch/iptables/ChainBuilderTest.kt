package com.adamkoch.iptables

import com.adamkoch.iptables.Util.sanitize
import com.adamkoch.iptables.ChainBuilder
import com.adamkoch.iptables.matches.*
import com.adamkoch.iptables.objects.MacAddress
import com.adamkoch.iptables.objects.Protocol
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.FileReader
import java.time.LocalTime
import java.util.*

internal class ChainBuilderTest {

    @Test
    @Disabled("not finished")
    fun keyword() {
        val properties = Properties()
        properties.load(FileReader("secrets.properties"))

        val joelOmenChain = ChainBuilder("Joel's Omen")
            .ifContains("discord", properties.getProperty("router.ip"))
            .rejectWithTcpReset();

//        iptables -A Joels_Omen -m mac ! --mac-source 00:00:00:A1:2B:CC -j RETURN
//        iptables -A Joels_Omen -p tcp -m webstr --url discord -j REJECT
//                iptables -A Joels_Omen -i br0 -p udp -m udp --dport 53 -m string --string "discord" --algo bm --to 65535 --icase -j REJECT
//        iptables -A Joels_Omen -d 192.168.50.1/32 -i br0 -p udp -m udp --dport 53 -m string --string "discord" --algo bm --to 65535 --icase -j REJECT

        assertEquals(
                "Joels_Omen -p tcp -m webstr --url discord -j REJECT\n" +
                "Joels_Omen -i br0 -p udp -m udp --dport 53 -m string --string \"discord\" --algo bm --to 65535 --icase -j REJECT\n" +
                "Joels_Omen -d 192.168.50.1/32 -i br0 -p udp -m udp --dport 53 -m string --string \"discord\" --algo bm --to 65535 --icase -j REJECT",
            joelOmenChain.createString())
    }

    @Test
    @Disabled("not finished")
    fun keywordPlus() {
        val properties = Properties()
        properties.load(FileReader("secrets.properties"))

        val joelOmenChain = ChainBuilder("Joel's Omen")
            .returnIfNot(MacAddress.DUMMY)
            .ifBetweenLocal(8, 0, 11, 0)
            .ifContains("discord", properties.getProperty("router.ip"))
            .rejectWithTcpReset();

//        iptables -A Joels_Omen -m mac ! --mac-source 00:00:00:A1:2B:CC -j RETURN
//        iptables -A Joels_Omen -p tcp -m webstr --url discord -j REJECT
//                iptables -A Joels_Omen -i br0 -p udp -m udp --dport 53 -m string --string "discord" --algo bm --to 65535 --icase -j REJECT
//        iptables -A Joels_Omen -d 192.168.50.1/32 -i br0 -p udp -m udp --dport 53 -m string --string "discord" --algo bm --to 65535 --icase -j REJECT

        assertEquals("Joels_Omen -m mac ! --mac-source ${MacAddress.DUMMY} -j RETURN\n" +
                "Joels_Omen -p tcp -m time --kerneltz --timestart 08:00 --timestop 11:00 -m webstr --url discord -j REJECT --reject-with tcp-reset\n" +
                "Joels_Omen -i br0 -p udp -m udp --dport 53 -m string --string \"discord\" --algo bm --to 65535 --icase -j REJECT\n" +
                "Joels_Omen -d 192.168.50.1/32 -i br0 -p udp -m udp --dport 53 -m string --string \"discord\" --algo bm --to 65535 --icase -j REJECT",
            joelOmenChain.createString())
    }

    @Test
    @Disabled("not finished")
    fun morningSchoolTime() {
        val properties = Properties()
        properties.load(FileReader("secrets.properties"))

        val chainBuilder = ChainBuilder("Joel's Omen")
            .returnIfNot(MacAddress(properties.getProperty("joel.omen.mac")))
            .ifBetweenLocal(8, 0, 11, 0)
            .ifContains("discord", properties.getProperty("router.ip"))
            .rejectWithTcpReset()
            .ifBetweenLocal(12, 0, 15, 0)
            .ifContains("discord", properties.getProperty("router.ip"))
            .rejectWithTcpReset()
            .ifBetweenLocal(8, 0, 11, 0)
            .ifDestinationIp("159.153.191.238")
            .ifProtocol(Protocol.TCP)
            .rejectWithTcpReset()
            .ifBetweenLocal(12, 0, 15, 0)
            .ifDestinationIp("159.153.191.238")
            .ifProtocol(Protocol.TCP)
            .rejectWithTcpReset()

        val omenChain = Chain("Joel's Omen")
        val shortCircuitMacAddress = MacAddress(properties.getProperty("joel.omen.mac"))
        val shortCircuitMacAddressMatch = MacAddressMatch(shortCircuitMacAddress).not();
        val shortCircuitMacAddressRule = Rule(Target.RETURN)
        shortCircuitMacAddressRule.addMatch(shortCircuitMacAddressMatch)

        omenChain.add(shortCircuitMacAddressRule)

        val schoolStartTime = LocalTime.of(8, 0)
        val lunchStartTime = LocalTime.of(11, 0)

//        schoolMornings.add(TimeRange(schoolStartTime, lunchStartTime))
        val schoolMorningsMatch = DateTimeMatch()
        schoolMorningsMatch.setStart(schoolStartTime)
        schoolMorningsMatch.setEnd(lunchStartTime)
        schoolMorningsMatch.setUseKernelTZ(true)

        val schoolAfternoonsMatch = DateTimeMatch()

        val lunchEndTime = LocalTime.of(12, 0)
        val schoolEndTime = LocalTime.of(15, 0)

        schoolAfternoonsMatch.setStart(lunchEndTime)
        schoolAfternoonsMatch.setEnd(schoolEndTime)
        schoolAfternoonsMatch.setUseKernelTZ(true)

        val discordTcpKeywordMatch = TcpKeywordMatch("discord")

        val preventDiscordMorningsRule = Rule(Target.REJECT_WITH_TCP_RESET)
        preventDiscordMorningsRule.addMatch(discordTcpKeywordMatch, schoolMorningsMatch)
        omenChain.add(preventDiscordMorningsRule)

        val preventDiscordAfternoonsRule = Rule(Target.REJECT_WITH_TCP_RESET)
        preventDiscordAfternoonsRule.addMatch(discordTcpKeywordMatch, schoolAfternoonsMatch)
        omenChain.add(preventDiscordAfternoonsRule)


        val destinationMatch = DestinationMatch("159.153.191.238")

        val morningsDestinationMatchRule = Rule(Target.REJECT_WITH_TCP_RESET)
        morningsDestinationMatchRule.addMatch(ProtocolMatch.TCP, destinationMatch, schoolMorningsMatch)
        omenChain.add(morningsDestinationMatchRule)


        val afternoonsDestinationMatchRule = Rule(Target.REJECT_WITH_TCP_RESET)
        afternoonsDestinationMatchRule.addMatch(ProtocolMatch.TCP, destinationMatch, schoolAfternoonsMatch)
        omenChain.add(afternoonsDestinationMatchRule)

        println(omenChain.toString())
        println(chainBuilder.toString())

        assertEquals(System.lineSeparator() + omenChain.toString(), System.lineSeparator() + chainBuilder.toString())

        val writer = ScriptWriter(CommandOption.APPEND)
        writer.add(omenChain)
        println(writer.toString())


        val testRules = listOf(TimeRange(schoolStartTime, lunchStartTime), TimeRange(lunchEndTime, schoolEndTime)).map {
            val testRule = Rule(Target.REJECT_WITH_TCP_RESET)
            val timeRangeMatch = DateTimeMatch()
                .from(it.startTime)
                .to(it.endTime)
            testRule.addMatch(timeRangeMatch)
            testRule.asString()
        }

        println(testRules)
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