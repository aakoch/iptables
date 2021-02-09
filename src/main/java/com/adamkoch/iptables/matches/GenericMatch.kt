package com.adamkoch.iptables.matches

import java.util.Objects

/**
 * Name came from https://www.frozentux.net/iptables-tutorial/iptables-tutorial.html#GENERICMATCHES
 *
 *
 * From https://www.lammertbies.nl/comm/info/iptables:
 *
 *
 * // iptables -A Enemies -s $1 -j DROP
 *
 * -A Enemies  -m recent --name psc --update --seconds 60 -j DROP -A Enemies -i ! lo -m tcp -p tcp --dport 1433  -m
 * recent --name psc --set -j DROP -A Enemies -i ! lo -m tcp -p tcp --dport 3306  -m recent --name psc --set -j DROP -A
 * Enemies -i ! lo -m tcp -p tcp --dport 8086  -m recent --name psc --set -j DROP -A Enemies -i ! lo -m tcp -p tcp
 * --dport 10000 -m recent --name psc --set -j DROP -A Enemies -s 99.99.99.99 -j DROP
 *
 * @author aakoch
 * @since 0.1.0
 */
open class GenericMatch : Match {
    override val rank: Int = 0

    //  public static final GenericMatch EXPLICIT_MATCH = new GenericMatch("-m");
    private val flags: Array<String>
    private val value: String

    constructor(flag: String, value: String) {
        Objects.requireNonNull(flag)
        Objects.requireNonNull(value)
        flags = arrayOf(flag)
        this.value = value
    }

    protected constructor(flags: Array<String>, value: String) {
        Objects.requireNonNull(flags)
        Objects.requireNonNull(value)
        this.flags = flags
        this.value = value
    }

    override fun asString(): String {
        return flags[0] + " " + value
    }

    override fun toString(): String {
        return asString()
    }
}