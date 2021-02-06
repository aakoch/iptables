package com.adamkoch.iptables;

import com.adamkoch.annotations.Unstable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @since 0.1.0
 * @author aakoch
 */
@Unstable
public class ScriptWriter {

  private List<Chain> chains = new ArrayList<>();

  public void add(final Chain chain) {
    chains.add(chain);
  }

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder();

    for (Chain chain : chains) {
      String sanitizedChainName = Util.sanitize(chain.getName());

      sb.append("# Creates a new user-defined chain named " + sanitizedChainName);
      sb.append(System.lineSeparator());
      sb.append("iptables -N ");
      sb.append(sanitizedChainName);
      sb.append(System.lineSeparator());

      sb.append("# Adds jump point for INPUT chain");
      sb.append(System.lineSeparator());
      sb.append("iptables -I INPUT -j ");
      sb.append(sanitizedChainName);
      sb.append(System.lineSeparator());

      sb.append("# Adds jump point for FORWARD chain");
      sb.append(System.lineSeparator());
      sb.append("iptables -I FORWARD -j ");
      sb.append(sanitizedChainName);
      sb.append(System.lineSeparator());

      for (Rule rule : chain.getRules()) {
        sb.append("iptables -A ");
        sb.append(sanitizedChainName);
        sb.append(" ");
        sb.append(rule.asString());
        sb.append(System.lineSeparator());
      }
    }

    return sb.toString();
  }

}
