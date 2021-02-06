package com.adamkoch.iptables.matches;

/**
 * An extension can have options. This interface represents those options.
 */
@FunctionalInterface
public interface ExtensionMatchOption {

  /**
   * Output as a string for use in the terminal. I left toString() open to provide a way for developers to log the object in a more meaninful way.
   * @return The option represented as a string
   */
  String asString();
}
