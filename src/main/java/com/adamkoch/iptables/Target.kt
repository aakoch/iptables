package com.adamkoch.iptables

enum class Target(private val command: String) {
    DROP("DROP"),
    REJECT("REJECT"),

    // Allowable reject types
    // IPV4
    // icmp-net-unreachable, icmp-host-unreachable, icmp-port-unreachable, icmp-proto-unreachable, icmp-net-prohibited, icmp-host-prohibited, or icmp-admin-prohibited
    // IPV6
    // icmp6-no-route, no-route, icmp6-adm-prohibited, adm-prohibited, icmp6-addr-unreachable, addr-unreach, or icmp6-port-unreachable

    REJECT_WITH_RESET("REJECT --reject-with tcp-reset"),
    RETURN("RETURN"),
    ACCEPT("ACCEPT");

    override fun toString(): String {
        return command
    }
}