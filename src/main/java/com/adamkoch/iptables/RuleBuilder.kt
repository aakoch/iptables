package com.adamkoch.iptables

import com.adamkoch.iptables.matches.Match
import com.adamkoch.iptables.matches.ProtocolMatch
import com.adamkoch.iptables.matches.DateTimeMatch
import com.adamkoch.iptables.matches.WebStringExtensionMatch

class RuleBuilder(val target: Target) {

    private val matches: MutableList<Match>

    fun keyword(keyword: String): RuleBuilder {
        matches.add(WebStringExtensionMatch(keyword))
        return this
    }

    fun time(dateTimeExtensionMatch: DateTimeMatch): RuleBuilder {
        matches.add(dateTimeExtensionMatch)
        return this
    }

    operator fun invoke(): Rule {
        val rule = Rule(target)
        matches.sorted().forEach(rule::addMatch)
        return rule
    }

    init {
        this.matches = mutableListOf()
        if (target == Target.REJECT_WITH_TCP_RESET) {
            matches.add(ProtocolMatch.TCP)
        }
    }

}
