package com.adamkoch.iptables

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
        val shortCircuitMacAddressMatch = com.adamkoch.iptables.matches.MacAddressMatch(macAddress).not();
        return returnIfInternal(shortCircuitMacAddressMatch)
    }

    private fun returnIfInternal(shortCircuitMacAddressMatch: com.adamkoch.iptables.matches.Match): ChainBuilder {
        val rule = com.adamkoch.iptables.Rule(com.adamkoch.iptables.Target.RETURN)
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

    fun ifContains(keyword: String, routerIp: String): ChainBuilder {
//        rules.add(Rule(name).also { it.addMatch(WebStringExtensionMatch(keyword)) })
//        rules.add(Rule(name).also { it.addMatch(Udp1KeywordMatch(keyword)) })
//        rules.add(Rule(name).also { it.addMatch(Udp2KeywordMatch(keyword, routerIp)) })
        separateMatches.add(com.adamkoch.iptables.matches.TcpKeywordMatch(keyword))
        separateMatches.add(com.adamkoch.iptables.matches.Udp1KeywordMatch(keyword))
        separateMatches.add(com.adamkoch.iptables.matches.Udp2KeywordMatch(keyword, routerIp))
        return this
    }

    override fun toString(): String {
        return createString()
    }

    fun createString(): String {

        val chain = com.adamkoch.iptables.Chain(com.adamkoch.iptables.Util.sanitize(this.name));
        rules.forEach(chain::add)
        return chain.toString()

        val sb = StringBuilder()
        val sanitizedChainName = com.adamkoch.iptables.Util.sanitize(this.name)
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


        for(unfinishedRule in separateMatches.sorted()) {
            val rule = com.adamkoch.iptables.Rule(com.adamkoch.iptables.Target.REJECT)
            (matches + unfinishedRule).forEach(rule::addMatch)
            rules.add(rule)
        }

//        separateMatches.map { rules.add(Rule(Target.REJECT_WITH_TCP_RESET).apply { this.addMatch(it);  } ) }

        matches.clear()
        return this
    }

    fun ifDestinationIp(s: String): ChainBuilder {
        matches.add(com.adamkoch.iptables.matches.DestinationMatch(s))
        return this
    }

    fun ifProtocol(protocol: com.adamkoch.iptables.objects.Protocol): ChainBuilder {
        matches.add(com.adamkoch.iptables.matches.ProtocolMatch.Companion.match(protocol))
        return this
    }
}