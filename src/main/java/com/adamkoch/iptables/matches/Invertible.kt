package com.adamkoch.iptables.matches

/**
 * Specifies when a [Match] can be inverted.
 *
 * @author aakoch
 * @since 0.1.0
 */
fun interface Invertible {
    /**
     * Returns a new inverted [Match]. The Match it was called upon will not change.
     *
     * @return an inverted Match
     */
    operator fun not(): Match
}