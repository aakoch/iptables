package com.adamkoch.iptables.matches;

import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;

public class EndDateTimeExtensionMatchOption extends DateTimeExtensionMatchOption {

  public EndDateTimeExtensionMatchOption(final Temporal endTemporal) {
    super(endTemporal);
  }

  public String asString() {
    return super.asString(false);
  }

}
