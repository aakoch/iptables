package com.adamkoch.iptables.matches

/**
 * The following description is from [http://ipset.netfilter.org/iptables-extensions.man.html#lbCE](http://ipset.netfilter.org/iptables-extensions.man.html#lbCE). <br></br>
 * Copied here for posterity. Please use the above webpage as official documentation.
 *
 * <h1>string</h1>
 *
 * This modules matches a given string by using some pattern matching strategy. It requires a linux
 * kernel &gt;= 2.6.14.
 *
 * <dl>
 * <dt>--algo {bm|kmp}</dt>
 * <dd>Select the pattern matching strategy. (bm = Boyer-Moore, kmp = Knuth-Pratt-Morris)</dd>
 * <dt>--from offset</dt>
 * <dd>Set the offset from which it starts looking for any matching. If not passed, default is 0.</dd>
 * <dt>--to offset </dt>
 * <dd>Set the
 * offset up to which should be scanned. That is, byte offset-1 (counting from 0) is the last one that is scanned. If
 * not passed, default is the packet size.</dd>
 * <dt>[!] --string pattern</dt>
 * <dd>Matches the given pattern.</dd>
 * <dt>[!] --hex-string pattern</dt>
 * <dd>Matches the given pattern in hex notation.</dd>
 * <dt>--icase</dt>
 * <dd>Ignore case when searching.</dd>
</dl> *
 *
 * Examples: <br></br>
 * # The string pattern can be used for simple text characters. <br></br>
 * `iptables -A INPUT -p tcp --dport 80 -m string --algo bm --string 'GET /index.html' -j LOG` <br></br>
 * # The hex string pattern can be used for non-printable characters, like |0D 0A| or |0D0A|. <br></br>
 * `iptables -p udp --dport 53 -m string --algo bm --from 40 --to 57 --hex-string '|03|www|09|netfilter|03|org|00|'`
 *
 * @author aakoch
 * @since 0.1.0
 */

/**
 * @param keyword Pattern to search for. Notice this is not the same parameter as the parent class {@link
 *     ExtensionMatch}!
 */
// TODO: probably need to do some validation on the keyword
class StringExtensionMatch(private val keyword: String) : ExtensionMatch("string"), Invertible {
    private var inverted = false
    override fun not(): StringExtensionMatch {
        val newStringExtensionMatch = copy()
        newStringExtensionMatch.setInvertedFlag()
        return newStringExtensionMatch
    }

    fun withAlgorithm(algorithm: String?): StringExtensionMatch {
        val newStringExtensionMatch = copy()
        newStringExtensionMatch.with(AlgorithmExtensionMatchOption(algorithm))
        return newStringExtensionMatch
    }

    fun withIgnoreCase(): StringExtensionMatch {
        val newStringExtensionMatch = copy()
        newStringExtensionMatch.with(IgnoreCaseExtensionMatchOption.DEFAULT)
        return newStringExtensionMatch
    }

    fun withToOffset(offset: Int): StringExtensionMatch {
        val newStringExtensionMatch = copy()
        newStringExtensionMatch.with(ToOffsetStringExtensionMatchOption(offset))
        return newStringExtensionMatch
    }

    private fun copy(): StringExtensionMatch {
        val newStringExtensionMatch = StringExtensionMatch(keyword)
        newStringExtensionMatch.extensionMatchOptions = extensionMatchOptions
        return newStringExtensionMatch
    }

    private fun setInvertedFlag() {
        inverted = true
    }

    override fun asString(): String {
        val options = extensionMatchOptions!!
        val optionsStringBuilder = StringBuilder(options.size shl 4)
        if (!options.isEmpty()) {
            for (option in options) {
                optionsStringBuilder.append(" ")
                optionsStringBuilder.append(option.asString())
            }
        }
        return "-m string " + (if (inverted) "! " else "") + "--string \"" + keyword + "\"" + optionsStringBuilder
    }
}