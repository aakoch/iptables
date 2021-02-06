package com.adamkoch.iptables.matches;

import java.time.temporal.Temporal;

/**
 * Used with the {@link StringExtensionMatch} to specify the end offset.
 *
 * @author aakoch
 * @since 0.1.0
 */
public class ToOffsetStringExtensionMatchOption extends GenericExtensionMatchOption {

  private final int offset;

  public ToOffsetStringExtensionMatchOption() {
    this(0);
  }

  public ToOffsetStringExtensionMatchOption(int offset) {
    this.offset = offset;
  }

  @Override
  public String asString() {
    return "--to " + offset;
  }
}
