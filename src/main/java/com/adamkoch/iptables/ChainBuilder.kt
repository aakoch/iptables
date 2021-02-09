package com.adamkoch.iptables

import com.adamkoch.iptables.matches.*
import com.adamkoch.iptables.objects.MacAddress
import java.time.LocalTime

class ChainBuilder(val name: String) {
    val rules: MutableList<Rule> = mutableListOf()
    val matches: MutableList<Match> = mutableListOf()

    fun returnIf(macAddress: MacAddress) : ChainBuilder {
        val shortCircuitMacAddressMatch = MacAddressMatch(macAddress);
        val rule = Rule(ActionComponent.ReturnActionComponent())
        rule.addMatch(shortCircuitMacAddressMatch)
        rules.add(rule)
        matches.clear()
        return this
    }

    fun ifBetween(startHour: Int, startMinute: Int, endHour: Int, endMinute: Int): ChainBuilder {

        val schedule = TimeSchedule()

        val startTime = LocalTime.of(startHour, startMinute)
        val endTime = LocalTime.of(endHour, endMinute)

        schedule.add(TimeRange(startTime, endTime))
        val scheduleMatch = TimeExtensionMatch()
        scheduleMatch.setStart(startTime)
        scheduleMatch.setEnd(endTime)

        matches.add(scheduleMatch)

        return this
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

    fun reject(): ChainBuilder {
        val rule = Rule(ActionComponent.RejectActionComponent())
        matches.forEach(rule::addMatch)
        rules.add(rule)
        matches.clear()
        return this
    }
}
