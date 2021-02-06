package com.adamkoch.iptables.matches

import com.adamkoch.iptables.matches.ExtensionMatchOption
import java.util.Collections
import java.lang.StringBuilder
import java.util.ArrayList
import java.util.Objects

/**
 * A [Match] that is not a generic match.
 *
 * @author aakoch
 * @since 0.1.0
 */
open class ExtensionMatch(type: String) : Match {
    val type: String
    private var options: MutableList<ExtensionMatchOption>
    protected var extensionMatchOptions: List<ExtensionMatchOption>?
        get() = Collections.unmodifiableList(options)
        protected set(extensionMatchOptions) {
            options = ArrayList(extensionMatchOptions)
        }

    protected fun with(extensionMatchOption: ExtensionMatchOption) {
        options.add(extensionMatchOption)
    }

    override fun asString(): String {
        val optionsStringBuilder = StringBuilder(options.size * 16)
        if (!options.isEmpty()) {
            for (option in options) {
                optionsStringBuilder.append(" ")
                optionsStringBuilder.append(option)
            }
        }
        return "-m $type$optionsStringBuilder"
    }

    override fun toString(): String {
        return asString()
    }

    init {
        Objects.requireNonNull(type)
        this.type = type
        options = ArrayList()
    }
}