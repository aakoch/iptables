package com.adamkoch.iptables

import com.adamkoch.iptables.matches.*

class MultipleTimeRangesRuleSet() {
    lateinit var match2: Match
    lateinit var match1: Match
    lateinit var dateTimeExtensionMatch2: DateTimeMatch
    lateinit var dateTimeExtensionMatch1: DateTimeMatch


    fun asString() : String {

       val rule1 = RuleBuilder(Target.REJECT_WITH_TCP_RESET)
            .keyword("discord")
            .time(dateTimeExtensionMatch1)()


        val rule2 = RuleBuilder(Target.REJECT_WITH_TCP_RESET)
            .keyword("discord")
            .time(dateTimeExtensionMatch2)()


        val strBuilder = StringBuilder()
        strBuilder.append(rule1.asString())
        strBuilder.append(System.lineSeparator())
        strBuilder.append(rule2.asString())
        return strBuilder.toString()
    }

}
