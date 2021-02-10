package com.adamkoch.iptables

class AggregatedRule(val rule1: Rule, val rule2: Rule, target: Target) : Rule(target) {


    override fun asString(): String {
        return rule1.asString() + " " + rule2.asString() + " -j " + target.toString()
    }
}
