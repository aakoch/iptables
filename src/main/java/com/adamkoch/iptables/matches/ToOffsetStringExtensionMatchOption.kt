package com.adamkoch.iptables.matches

/**
 * Used with the [StringExtensionMatch] to specify the end offset.
 *
 * @author aakoch
 * @since 0.1.0
 */
class ToOffsetStringExtensionMatchOption @JvmOverloads constructor(private val offset: Int = 0) :
    GenericExtensionMatchOption("--to") {
    override fun asString(): String {
        return "--to $offset"
    }
}