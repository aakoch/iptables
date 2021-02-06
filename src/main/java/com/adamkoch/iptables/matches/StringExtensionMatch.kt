package com.adamkoch.iptables.matches;

import java.util.List;

/**
 * The following description is from <a href="http://ipset.netfilter.org/iptables-extensions.man.html#lbCE">http://ipset.netfilter.org/iptables-extensions.man.html#lbCE</a>. <br>
 * Copied here for posterity. Please use the above webpage as official documentation.
 *
 * <h1>string</h1>
 * <p>This modules matches a given string by using some pattern matching strategy. It requires a linux
 * kernel &gt;= 2.6.14.</p>
 *
 * <dl>
 *   <dt>--algo {bm|kmp}</dt>
 *   <dd>Select the pattern matching strategy. (bm = Boyer-Moore, kmp = Knuth-Pratt-Morris)</dd>
 *   <dt>--from offset</dt>
 *   <dd>Set the offset from which it starts looking for any matching. If not passed, default is 0.</dd>
 *   <dt>--to offset </dt>
 *   <dd>Set the
 *  offset up to which should be scanned. That is, byte offset-1 (counting from 0) is the last one that is scanned. If
 *  not passed, default is the packet size.</dd>
 *   <dt>[!] --string pattern</dt>
 *   <dd>Matches the given pattern.</dd>
 *   <dt>[!] --hex-string pattern</dt>
 *   <dd>Matches the given pattern in hex notation.</dd>
 *   <dt>--icase</dt>
 *   <dd>Ignore case when searching.</dd>
 * </dl>
 *
 * Examples: <br>
 * # The string pattern can be used for simple text characters. <br>
 * {@code iptables -A INPUT -p tcp --dport 80 -m string --algo bm --string 'GET /index.html' -j LOG} <br>
 * # The hex string pattern can be used for non-printable characters, like |0D 0A| or |0D0A|. <br>
 * {@code iptables -p udp --dport 53 -m string --algo bm --from 40 --to 57 --hex-string '|03|www|09|netfilter|03|org|00|'}
 *
 * @author aakoch
 * @since 0.1.0
 */
@SuppressWarnings({"MethodReturnOfConcreteClass", "PublicMethodNotExposedInInterface"})
public class StringExtensionMatch extends ExtensionMatch implements Invertible {

  private final String keyword;

  /**
   * Specifies that the inverse of the rule is to be applied. (The "!")
   */
  private boolean inverted;

  /**
   * @param keyword Pattern to search for. Notice this is not the same parameter as the parent class {@link
   *     ExtensionMatch}!
   */
  public StringExtensionMatch(final String keyword) {
    super("string");

    // TODO: probably need to do some validation on this
    this.keyword = keyword;
  }

  @Override
  public StringExtensionMatch not() {
    StringExtensionMatch newStringExtensionMatch = copy();
    //noinspection LawOfDemeter
    newStringExtensionMatch.setInvertedFlag();
    return newStringExtensionMatch;
  }

  public StringExtensionMatch withAlgorithm(final String algorithm) {
    StringExtensionMatch newStringExtensionMatch = copy();
    //noinspection LawOfDemeter
    newStringExtensionMatch.with(new AlgorithmExtensionMatchOption(algorithm));
    return newStringExtensionMatch;
  }

  public StringExtensionMatch withIgnoreCase() {
    StringExtensionMatch newStringExtensionMatch = copy();
    //noinspection LawOfDemeter
    newStringExtensionMatch.with(IgnoreCaseExtensionMatchOption.DEFAULT);
    return newStringExtensionMatch;
  }

  public StringExtensionMatch withToOffset(final int offset) {
    StringExtensionMatch newStringExtensionMatch = copy();
    //noinspection LawOfDemeter
    newStringExtensionMatch.with(new ToOffsetStringExtensionMatchOption(offset));
    return newStringExtensionMatch;
  }

  private StringExtensionMatch copy() {
    StringExtensionMatch newStringExtensionMatch = new StringExtensionMatch(keyword);
    newStringExtensionMatch.setExtensionMatchOptions(getExtensionMatchOptions());
    return newStringExtensionMatch;
  }

  private void setInvertedFlag() {
    inverted = true;
  }

  @Override
  public String asString() {
    List<ExtensionMatchOption> options = getExtensionMatchOptions();
    StringBuilder optionsStringBuilder = new StringBuilder(options.size() << 4);
    if (!options.isEmpty()) {
      for (ExtensionMatchOption option : options) {
        optionsStringBuilder.append(" ");
        optionsStringBuilder.append(option.asString());
      }
    }
    return "-m string " + (inverted ? "! " : "") + "--string \"" + keyword + "\"" + optionsStringBuilder;
  }
}
