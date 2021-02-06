package com.adamkoch.iptables.matches;

import java.time.temporal.Temporal;

public class StartDateTimeExtensionMatchOption extends DateTimeExtensionMatchOption {

  public StartDateTimeExtensionMatchOption(final Temporal startTemporal) {
    super(startTemporal);
  }

  @Override
  public String asString() {
    return super.asString(true);
  }
}
