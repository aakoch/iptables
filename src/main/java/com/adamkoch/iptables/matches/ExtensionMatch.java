package com.adamkoch.iptables.matches;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A {@link Match} that is not a generic match.
 *
 * @author aakoch
 * @since 0.1.0
 */
public class ExtensionMatch implements Match {

  private final String type;
  private List<ExtensionMatchOption> options;

  public ExtensionMatch(final String type) {
    Objects.requireNonNull(type);
    this.type = type;
    options = new ArrayList<>();
  }

  public String getType() {
    return type;
  }

  protected List<ExtensionMatchOption> getExtensionMatchOptions() {
    return Collections.unmodifiableList(options);
  }

  protected void setExtensionMatchOptions(final List<? extends ExtensionMatchOption> extensionMatchOptions) {
    options = new ArrayList<>(extensionMatchOptions);
  }

  protected void with(final ExtensionMatchOption extensionMatchOption) {
    options.add(extensionMatchOption);
  }

  @Override
  public String asString() {
    final StringBuilder optionsStringBuilder = new StringBuilder(options.size() << 4);
    if (!options.isEmpty()) {
      for (final ExtensionMatchOption option : options) {
        optionsStringBuilder.append(" ");
        optionsStringBuilder.append(option);
      }
    }

    return "-m " + type + optionsStringBuilder;
  }

  @Override
  public String toString() {
    return asString();
  }
}
