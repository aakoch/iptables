package com.adamkoch.iptables.matches

enum class MatchWeight(val weight: Int) {

    INTERFACE(1000),
    PROTOCOL(900),
    DESTINATION(875),
    MAC_ADDRESS(850),
    DESTINATION_PORT(899),
    KEYWORD(700),
    TIME(600);

}