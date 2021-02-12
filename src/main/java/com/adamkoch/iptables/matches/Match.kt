package com.adamkoch.iptables.matches

/**
 * A (hopefully) immutable object that represents the different ways a rule can be matched on.
 *
 * @author aakoch
 * @since 0.1.0
 */
interface Match : Comparable<Match> {
    abstract val weight: Int

    /**
     * Returns a string representation. I wanted to leave "toString()" as a method for outputting matches for developers
     * while debugging or logging.
     *
     * @return A string representation
     */
    fun asString(): String

    override fun compareTo(other: Match): Int {
        return other.weight - this.weight
    }
}