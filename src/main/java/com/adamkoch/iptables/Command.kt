package com.adamkoch.iptables;

import com.adamkoch.annotations.Unstable;
import java.util.Optional;

/**
 *
 * @since 0.1.0
 * @author aakoch
 */
@Unstable
public class Command {

  public static final String IPTABLES_COMMAND = "iptables";

  private final String command;
  private Optional<String> comment;

  private Command(final String command) {
    this.command = command;
    this.comment = Optional.empty();
  }

  private Command(final String command, final String comment) {
    this.command = command;
    this.comment = Optional.of(comment);
  }

  public static Command from(Rule rule) {
    Command command = new Command(IPTABLES_COMMAND + " " + rule.asString());
    command.comment = Optional.ofNullable(rule.comment());
    return command;
  }

  public Command withComments() {
    Command newCommand = new Command(this.command, comment.orElse("no comment provided"));
    return newCommand;
  }


}
