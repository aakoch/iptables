package com.adamkoch.iptables.matches;

import com.adamkoch.annotations.Unstable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This class hasn't stabilized yet.
 *
 * @author aakoch
 * @since 0.1.0
 */
@Unstable
class GenericExtensionMatchOption implements ExtensionMatchOption {

  // I might remove this
  private final Set<String> possibleValues;
  private String[] flags;
  // I might remove this
  private String defaultValue;

  public GenericExtensionMatchOption() {
    this(new String[0]);
  }

  public GenericExtensionMatchOption(final String flag) {
    this(new String[]{flag});
  }

  public GenericExtensionMatchOption(final String[] flags) {
    Objects.requireNonNull(flags);
    this.flags = flags;
    possibleValues = new HashSet<>();
  }

  public void addDefault(final String defaultValue) {
    Objects.requireNonNull(defaultValue);
    this.defaultValue = defaultValue;
  }

  public void addFlag(final String... flags) {
    Objects.requireNonNull(flags);
    this.flags = flags;
  }

  public void addValue(final String value) {
    Objects.requireNonNull(value);
    possibleValues.add(value);
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  @Override
  public String asString() {
    return flags[0];
  }
}
