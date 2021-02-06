package com.adamkoch.iptables.matches

/**
 * @author aakoch
 * @since 0.1.0
 */
class IgnoreCaseExtensionMatchOption private constructor() :
    GenericExtensionMatchOption("--icase") {
    override fun asString(): String {
        return "--icase"
    }

    companion object {
        val DEFAULT = IgnoreCaseExtensionMatchOption()
    }
}