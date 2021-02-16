package com.adamkoch.iptables

import com.adamkoch.iptables.matches.*
import java.time.LocalTime

class ChainBuilder(val name: String) {
    val rules: MutableList<com.adamkoch.iptables.Rule> = mutableListOf()
    val matches: MutableList<com.adamkoch.iptables.matches.Match> = mutableListOf()
    val separateMatches: MutableList<com.adamkoch.iptables.matches.Match> = mutableListOf()

    fun returnIf(macAddress: com.adamkoch.iptables.objects.MacAddress) : ChainBuilder {
        val shortCircuitMacAddressMatch = com.adamkoch.iptables.matches.MacAddressMatch(macAddress);
        return returnIfInternal(shortCircuitMacAddressMatch)
    }

    fun returnIfNot(macAddress: com.adamkoch.iptables.objects.MacAddress) : ChainBuilder {
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
    ): com.adamkoch.iptables.matches.DateTimeMatch {
        val scheduleMatch = com.adamkoch.iptables.matches.DateTimeMatch()
        scheduleMatch.setStart(LocalTime.of(startHour, startMinute))
        scheduleMatch.setEnd(LocalTime.of(endHour, endMinute))
        return scheduleMatch
    }

    fun ifContains(keyword: String): ChainBuilder {
//        rules.add(Rule(name).also { it.addMatch(WebStringExtensionMatch(keyword)) })
//        rules.add(Rule(name).also { it.addMatch(Udp1KeywordMatch(keyword)) })
//        rules.add(Rule(name).also { it.addMatch(Udp2KeywordMatch(keyword, routerIp)) })
        matches.add(TcpKeywordMatch(keyword))


//        separateMatches.add(Udp1KeywordMatchSet(keyword))
//        separateMatches.addAll(Udp2KeywordMatchSet(keyword, routerIp).getMatches())



        return this
    }

    override fun toString(): String {
        return createString()
    }

    fun createString(): String {

        val chain = Chain(Util.sanitize(this.name));
        rules.forEach(chain::add)
        return chain.asString()

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

        val rule = Rule(Target.REJECT)

        for (match in matches.sorted()) {
            rule.addMatch(match)
        }

        rules.add(rule)

        return this

//        for(match in separateMatches.sorted()) {
//            val rule = Rule(Target.REJECT)
//            rule.addMatch(match)
//            rule.addMatch(*matches.toTypedArray())
//            rules.add(rule)
//        }
//
////        separateMatches.map { rules.add(Rule(Target.REJECT_WITH_TCP_RESET).apply { this.addMatch(it);  } ) }
//
//        matches.clear()
//
//        val chain = Chain(name)
//        chain.add(* rules.toTypedArray())
//        return chain
    }

    fun ifDestinationIp(s: String): ChainBuilder {
        matches.add(com.adamkoch.iptables.matches.DestinationMatch(s))
        return this
    }

    fun ifProtocol(protocol: com.adamkoch.iptables.objects.Protocol): ChainBuilder {
        matches.add(ProtocolMatch.valueOf(protocol))
        return this
    }

    fun rule(rule: Rule): ChainBuilder {
        rules.add(rule)
        return this
    }

    fun build(): Chain {
        val chain = Chain(name)
        chain.add(*rules.toTypedArray())
        return chain
    }

    fun rejectWithTcpReset(): ChainBuilder {
        val rule = Rule(Target.REJECT_WITH_TCP_RESET)

        for (match in matches.sorted()) {
            rule.addMatch(match)
        }

        rules.add(rule)
        return this
    }

    fun udpContains(keyword: String): ChainBuilder {
        matches.addAll(Udp1KeywordMatchSet(keyword).getMatches())
        return this
    }

    fun udp2Contains(keyword: String, ipAddress: String): ChainBuilder {
        matches.addAll(Udp2KeywordMatchSet(keyword, ipAddress).getMatches())
        return this
    }
}