package com.adamkoch.iptables.matches;

/**
 * @author aakoch
 * @since 0.1.0
 */
public class IgnoreCaseExtensionMatchOption extends GenericExtensionMatchOption {

  public static final IgnoreCaseExtensionMatchOption DEFAULT = new IgnoreCaseExtensionMatchOption();

  private IgnoreCaseExtensionMatchOption() {
  }

  @Override
  public String asString() {
    return "--icase";
  }
}
