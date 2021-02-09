package com.adamkoch.iptables.matches

import com.adamkoch.annotations.Unstable
import kotlin.jvm.JvmOverloads
import com.adamkoch.iptables.matches.ExtensionMatchOption
import java.util.Objects
import java.util.HashSet

/**
 * This class hasn't stabilized yet.
 *
 * @author aakoch
 * @since 0.1.0
 */
@Unstable
open class GenericExtensionMatchOption constructor(
    flags: MutableList<String>
) : ExtensionMatchOption {
    // I might remove this
    private val possibleValues: MutableSet<String>
    private var flags: MutableList<String>

    // I might remove this
    var defaultValue: String? = null
        private set

    constructor(flag: String) : this(mutableListOf<String>(flag)) {}

    fun addDefault(defaultValue: String) {
        Objects.requireNonNull(defaultValue)
        this.defaultValue = defaultValue
    }

    fun addFlag(vararg flags: String) {
        Objects.requireNonNull(flags)
        this.flags.addAll(flags)
    }

    fun addValue(value: String) {
        Objects.requireNonNull(value)
        possibleValues.add(value)
    }

    override fun asString(): String {
        return flags[0]
    }

    init {
        Objects.requireNonNull(flags)
        this.flags = flags
        possibleValues = HashSet()
    }
}