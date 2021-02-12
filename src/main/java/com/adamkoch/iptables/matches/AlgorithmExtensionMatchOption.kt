package com.adamkoch.iptables.matches

/**
 * For use with [StringExtensionMatch]. Defines how strings are compared
 *
 * @author aakoch
 * @since 0.1.0
 */
class AlgorithmExtensionMatchOption @JvmOverloads constructor(private val algorithm: String = BOYER_MOORE) :
    GenericExtensionMatchOption("--algo") {
    override fun asString(): String {
        return "--algo $algorithm"
    }

    companion object {
        const val BOYER_MOORE = "bm"
        const val KNUTH_PRATT_MORRIS = "kmp"
        val BOYER_MOORE_OPTION = AlgorithmExtensionMatchOption(BOYER_MOORE)
        val KNUTH_PRATT_MORRIS_OPTION = AlgorithmExtensionMatchOption(KNUTH_PRATT_MORRIS)
    }
}