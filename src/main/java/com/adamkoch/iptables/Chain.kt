package com.adamkoch.iptables

import com.adamkoch.annotations.Unstable
import java.util.stream.Collectors
import java.util.Optional
import com.adamkoch.iptables.TimeRange
import java.util.ArrayList

/**
 * A chain is a group of rules with a label.
 *
 * When creating a chain in iptables you would first run "iptables -N <chain name>" and then wire
 * it to the INPUT and FORWARD targets:
 * iptables -I INPUT -j <chain name>
 * iptables -I FORWARD -j <chain name>
 *
 * This is, of course, an oversimplification.
 *
 * @since 0.1.0
 * @author aakoch
 */
class Chain(val name: String) {
    val rules: MutableList<Rule> = mutableListOf()

    fun add(vararg rule: Rule) {
        rules += rule
    }

    fun asString(): String {
        val sanitized = Util.sanitize(name)
        val s = rules.joinToString(System.lineSeparator()) { rule: Rule -> sanitized + " " + rule.asString() }
        return s
    }
}