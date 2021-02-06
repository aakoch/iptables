package com.adamkoch.iptables;

/**
 * Represents the actions a rule can take.
 *
 * @since 0.1.0
 * @author aakoch
 */
public class ActionComponent {

  /**
   * Drop the packet
   */
  public static final class DropActionComponent extends ActionComponent {
    public String toString () {
      return "-j DROP";
    }
  }

  /**
   * Reject the packet
   */
  public static final class RejectActionComponent extends ActionComponent {
    private final boolean rejectWithTcpReset;
    public RejectActionComponent() {
      this(true);
    }
    public RejectActionComponent(boolean rejectWithTcpReset) {
      this.rejectWithTcpReset = rejectWithTcpReset;
    }
    public String toString () {
      return "-j REJECT" + (rejectWithTcpReset ? " --reject-with tcp-reset" : "");
    }
  }

  /**
   * Return from chain
   */
  public static final class ReturnActionComponent extends ActionComponent {
    public String toString () {
      return "-j RETURN";
    }
  }

  /**
   * Accept rule
   */
  public static final class AcceptActionComponent extends ActionComponent {
    public String toString () {
      return "-j ACCEPT";
    }
  }
}
