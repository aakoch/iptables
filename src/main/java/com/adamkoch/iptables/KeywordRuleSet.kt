package com.adamkoch.iptables

import com.adamkoch.annotations.Unstable
import com.adamkoch.iptables.matches.*
import java.util.*

/**
 *
 * @since 0.1.0
 * @author aakoch
 */
@Unstable
class KeywordRuleSet(
    private val keyword: String, routerIpAddress: String
) {
    var rules: MutableList<Rule> = ArrayList()

    fun asString(): String {
        return rules.map(Rule::asString).joinToString(System.lineSeparator())
    }

    init {
        val tcpKeywordMatch = TcpKeywordMatch(keyword)
        val keywordMatch2: Match = Udp1KeywordMatch(keyword)
        val keywordMatch3: Match = Udp2KeywordMatch(keyword, "$routerIpAddress/32")
//        val macAddressMatchingComponent = MacAddressMatch(macAddress)
        val rule1 = Rule(Target.REJECT_WITH_TCP_RESET)
        rule1.addMatch(tcpKeywordMatch)
        rules.add(rule1)
        val rule2 = Rule(Target.REJECT)
        rule2.addMatch(keywordMatch2)
        rules.add(rule2)
        val rule3 = Rule(Target.REJECT)
        rule3.addMatch(keywordMatch3)
        rules.add(rule3)
    }
}