package com.adamkoch.iptables.matches;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;

public abstract class DateTimeExtensionMatchOption extends GenericExtensionMatchOption {

  private final Temporal temporal;
  private boolean useKernelTZ;

  protected DateTimeExtensionMatchOption(final Temporal temporal) {
    this.temporal = temporal;
    useKernelTZ = true;
  }

  public void setUseKernelTZ(boolean newValue) {
    useKernelTZ = newValue;
  }

  protected String asString(final boolean startOrStop) {

    final String type;
    if (temporal.isSupported(ChronoField.YEAR) || temporal instanceof Instant) {
      type = "date";
    }
    else {
      type = "time";
    }

    final String temporalString = temporal.toString();

    StringBuilder sb = new StringBuilder();
    sb.append("--").append(type);

    if (startOrStop) {
      sb.append("start ");
    }
    else {
      sb.append("stop ");
    }

    sb.append(temporalString);

    if (temporalString.endsWith("Z") && useKernelTZ) {
      sb.deleteCharAt(sb.length() - 1);
      sb.append(" --kerneltz");
    }

    return sb.toString();
  }

  protected Temporal getTemporal() {
    return temporal;
  }
}
