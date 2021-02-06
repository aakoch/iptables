package com.adamkoch.iptables.matches;

import com.adamkoch.annotations.Unstable;
import java.util.List;

/**
 * Matches a string in the URL.
 *
 * TODO: Does the keyword need to be surrounded with quotes, single or double?
 *
 * @author aakoch
 * @since 0.1.0
 */
@Unstable
public class WebStringExtensionMatch extends ExtensionMatch {

  private final String keyword;

  /**
   * @param keyword Pattern to search for. Notice this is not the same parameter as the parent class {@link
   *     ExtensionMatch}!
   */
  public WebStringExtensionMatch(final String keyword) {
    super("webstr");
    if (keyword.matches(".*\\s+.*"))
      throw new IllegalArgumentException("Currently does not support keywords with spaces");

    this.keyword = keyword;
  }

  @Override
  public String asString() {
    StringBuilder optionsStringBuilder = new StringBuilder(32);
    List<ExtensionMatchOption> options = getExtensionMatchOptions();
    if (!options.isEmpty()) {
      for (ExtensionMatchOption option : options) {
        optionsStringBuilder.append(" ");
        optionsStringBuilder.append(option);
      }
    }
    return "-m webstr --url " + keyword + optionsStringBuilder;
  }

}
