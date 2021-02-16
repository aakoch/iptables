package com.adamkoch.iptables

import com.adamkoch.iptables.matches.*
import com.adamkoch.iptables.objects.MacAddress
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalTime

internal class ChainTest {
    @Test
    fun tcp() {
        val omenChain = Chain("Joel's Omen")

        val shortCircuitMacAddressRule = Rule(Target.RETURN).apply {
            this.addMatch(MacAddressMatch(MacAddress.DUMMY).not())
        }

        omenChain.add(shortCircuitMacAddressRule)

        val schoolStartTime = LocalTime.of(8, 0)
        val lunchStartTime = LocalTime.of(11, 0)
        val lunchEndTime = LocalTime.of(12, 0)
        val schoolEndTime = LocalTime.of(15, 0)

        val schoolMorningsMatch = DateTimeMatch().also {
            it.setStart(schoolStartTime)
            it.setEnd(lunchStartTime)
            it.setUseKernelTZ(true)
        }

        val schoolAfternoonsMatch = DateTimeMatch().also {
            it.setStart(lunchEndTime)
            it.setEnd(schoolEndTime)
            it.setUseKernelTZ(true)
        }

        val discordKeywordMatch = WebStringExtensionMatch("discord")

        listOf(schoolMorningsMatch, schoolAfternoonsMatch).forEach {
            val timeMatch = it
            Rule(Target.REJECT_WITH_TCP_RESET).also {
                it.addMatch(discordKeywordMatch, timeMatch, ProtocolMatch.TCP)
                omenChain.add(it)
            }
        }

        assertEquals("""
            Joels_Omen -m mac ! --mac-source ${MacAddress.DUMMY} -j RETURN
            Joels_Omen -p tcp -m time --kerneltz --timestart 08:00 --timestop 11:00 -m webstr --url discord -j REJECT --reject-with tcp-reset
            Joels_Omen -p tcp -m time --kerneltz --timestart 12:00 --timestop 15:00 -m webstr --url discord -j REJECT --reject-with tcp-reset
        """.trimIndent(), omenChain.asString())
    }

    @Test
    fun udp1() {
        val omenChain = Chain("Joel's Omen")

        val shortCircuitMacAddressRule = Rule(Target.RETURN).apply {
            this.addMatch(MacAddressMatch(MacAddress.DUMMY).not())
        }

        omenChain.add(shortCircuitMacAddressRule)

        val schoolStartTime = LocalTime.of(8, 0)
        val lunchStartTime = LocalTime.of(11, 0)
        val lunchEndTime = LocalTime.of(12, 0)
        val schoolEndTime = LocalTime.of(15, 0)

        val schoolMorningsMatch = DateTimeMatch().also {
            it.setStart(schoolStartTime)
            it.setEnd(lunchStartTime)
            it.setUseKernelTZ(true)
        }

        val schoolAfternoonsMatch = DateTimeMatch().also {
            it.setStart(lunchEndTime)
            it.setEnd(schoolEndTime)
            it.setUseKernelTZ(true)
        }

        listOf(schoolMorningsMatch, schoolAfternoonsMatch).forEach { timeMatch ->
            Rule(Target.DROP).also {
                it.addMatch(*Udp1KeywordMatchSet("disney").getMatches().toTypedArray(), timeMatch)
                omenChain.add(it)
            }

        }

        assertEquals("""
            Joels_Omen -m mac ! --mac-source ${MacAddress.DUMMY} -j RETURN
            Joels_Omen -i br0 -p udp -m udp --dport 53 -m string --string "disney" --algo bm --to 65535 --icase -m time --kerneltz --timestart 08:00 --timestop 11:00 -j DROP
            Joels_Omen -i br0 -p udp -m udp --dport 53 -m string --string "disney" --algo bm --to 65535 --icase -m time --kerneltz --timestart 12:00 --timestop 15:00 -j DROP
        """.trimIndent(), omenChain.asString())
    }
}