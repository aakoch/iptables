package com.adamkoch.iptables

class AggregatedRule(val rule1: Rule, val rule2: Rule, val _actionComponent: ActionComponent) : Rule(_actionComponent) {


    override fun asString(): String {
        return rule1.asString() + " " + rule2.asString() + " " + _actionComponent.toString()
    }
}
