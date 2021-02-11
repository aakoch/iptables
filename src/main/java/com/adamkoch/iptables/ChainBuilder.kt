package com.adamkoch.iptables

import com.adamkoch.iptables.matches.*
import com.adamkoch.iptables.objects.MacAddress
import com.adamkoch.iptables.objects.Protocol
import java.time.LocalTime

class ChainBuilder(val name: String) {
    val rules: MutableList<Rule> = mutableListOf()
    val matches: MutableList<Match> = mutableListOf()

    fun returnIf(macAddress: MacAddress) : ChainBuilder {
        val shortCircuitMacAddressMatch = MacAddressMatch(macAddress);
        return returnIfInternal(shortCircuitMacAddressMatch)
    }

    fun returnIfNot(macAddress: MacAddress) : ChainBuilder {
        val shortCircuitMacAddressMatch = MacAddressMatch(macAddress).not();
        return returnIfInternal(shortCircuitMacAddressMatch)
    }

    private fun returnIfInternal(shortCircuitMacAddressMatch: Match): ChainBuilder {
        val rule = Rule(Target.RETURN)
        rule.addMatch(shortCircuitMacAddressMatch)
        rules.add(rule)
        matches.clear()
        return this
    }

    fun ifBetween(startHour: Int, startMinute: Int, endHour: Int, endMinute: Int): ChainBuilder {
        val scheduleMatch = createScheduleMatch(startHour, startMinute, endHour, endMinute)
        matches.add(scheduleMatch)
        return this
    }

    fun ifBetweenLocal(startHour: Int, startMinute: Int, endHour: Int, endMinute: Int): ChainBuilder {
        val scheduleMatch = createScheduleMatch(startHour, startMinute, endHour, endMinute)
        scheduleMatch.setUseKernelTZ(true)
        matches.add(scheduleMatch)
        return this
    }

    private fun createScheduleMatch(
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int
    ): DateTimeMatch {
        val scheduleMatch = DateTimeMatch()
        scheduleMatch.setStart(LocalTime.of(startHour, startMinute))
        scheduleMatch.setEnd(LocalTime.of(endHour, endMinute))
        return scheduleMatch
    }

    fun ifContains(keyword: String): ChainBuilder {
        matches.add(TcpKeywordMatch(keyword))
        return this
    }

    override fun toString(): String {
        return createString()
    }

    fun createString(): String {

        val chain = Chain(Util.sanitize(this.name));
        rules.forEach(chain::add)
        return chain.toString()

        val sb = StringBuilder()
        val sanitizedChainName = Util.sanitize(this.name)
        sb.append("# Creates a new user-defined chain named $sanitizedChainName")
        sb.append(System.lineSeparator())
        sb.append("iptables -N ")
        sb.append(sanitizedChainName)
        sb.append(System.lineSeparator())
        sb.append("# Adds jump point for INPUT chain")
        sb.append(System.lineSeparator())
        sb.append("iptables -I INPUT -j ")
        sb.append(sanitizedChainName)
        sb.append(System.lineSeparator())
        sb.append("# Adds jump point for FORWARD chain")
        sb.append(System.lineSeparator())
        sb.append("iptables -I FORWARD -j ")
        sb.append(sanitizedChainName)
        sb.append(System.lineSeparator())
        for (rule in this.rules) {
            sb.append("iptables -A ")
            sb.append(sanitizedChainName)
            sb.append(" ")
            sb.append(rule.asString())
            sb.append(System.lineSeparator())
        }
        return sb.toString()
    }

    fun rejectWithTcpReset(): ChainBuilder {
        val rule = Rule(Target.REJECT_WITH_TCP_RESET)
        matches.forEach(rule::addMatch)
        rules.add(rule)
        matches.clear()
        return this
    }

    fun ifDestinationIp(s: String): ChainBuilder {
        matches.add(DestinationMatch(s))
        return this
    }

    fun ifProtocol(protocol: Protocol): ChainBuilder {
        matches.add(ProtocolMatch.match(protocol))
        return this
    }
}
