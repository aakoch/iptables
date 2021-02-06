package com.adamkoch.iptables

import com.adamkoch.annotations.Unstable
import com.adamkoch.iptables.ActionComponent.RejectActionComponent
import com.adamkoch.iptables.matches.*
import com.adamkoch.iptables.objects.MacAddress
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors

/**
 *
 * @since 0.1.0
 * @author aakoch
 */
@Unstable
class RuleSet(
    private val keyword: String, private val chainName: String, macAddress: MacAddress?,
    routerIpAddress: String
) {
    var rules: MutableList<Rule> = ArrayList()
    override fun toString(): String {
        return rules.stream().map { obj: Rule -> obj.asString() }
            .collect(Collectors.joining(System.lineSeparator()))
    }

    init {
        val keywordMatchingComponent1 = TcpKeywordMatch(keyword)
        val keywordMatch2: Match = Udp1KeywordMatch(keyword)
        val keywordMatch3: Match = Udp2KeywordMatch(keyword, "$routerIpAddress/32")
        val macAddressMatchingComponent = MacAddressMatch(macAddress!!)
        val rule1 = Rule(RejectActionComponent())
        rule1.addMatch(keywordMatchingComponent1)
        rules.add(rule1)
        val rule2 = Rule(RejectActionComponent())
        rule2.addMatch(keywordMatch2)
        rules.add(rule2)
        val rule3 = Rule(RejectActionComponent())
        rule3.addMatch(keywordMatch3)
        rules.add(rule3)
        rules.forEach(Consumer { rule: Rule -> rule.addMatch(macAddressMatchingComponent) })
    }
}