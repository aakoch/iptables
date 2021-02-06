package com.adamkoch.iptables.matches

import com.adamkoch.annotations.Unstable

/**
 * Matches a string in the URL.
 *
 * TODO: Does the keyword need to be surrounded with quotes, single or double?
 *
 * @author aakoch
 * @since 0.1.0
 */
@Unstable
class WebStringExtensionMatch(keyword: String) : ExtensionMatch("webstr") {
    private val keyword: String
    override fun asString(): String {
        val optionsStringBuilder = StringBuilder(32)
        val options = extensionMatchOptions!!
        if (!options.isEmpty()) {
            for (option in options) {
                optionsStringBuilder.append(" ")
                optionsStringBuilder.append(option)
            }
        }
        return "-m webstr --url $keyword$optionsStringBuilder"
    }

    /**
     * @param keyword Pattern to search for. Notice this is not the same parameter as the parent class [     ]!
     */
    init {
        require(!keyword.matches(Regex(".*\\s+.*"))) { "Currently does not support keywords with spaces" }
        this.keyword = keyword
    }
}