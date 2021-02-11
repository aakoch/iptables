package com.adamkoch.iptables

import com.adamkoch.annotations.Unstable
import java.util.*

/**
 *
 * @since 0.1.0
 * @author aakoch
 */
@Unstable
class ScriptWriter(val appendInsertDelete: CommandOption) {
    private val chains: MutableList<Chain> = ArrayList()
    fun add(chain: Chain) {
        chains.add(chain)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (chain in chains) {
            val sanitizedChainName = Util.sanitize(chain.name)
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
            for (rule in chain.getRules()) {
                sb.append("iptables -$appendInsertDelete ")
                sb.append(sanitizedChainName)
                sb.append(" ")
                sb.append(rule.asString())
                sb.append(System.lineSeparator())
            }
        }
        return sb.toString()
    }
}