package com.adamkoch.iptables;

import static org.junit.jupiter.api.Assertions.*;

import com.adamkoch.iptables.matches.MacAddressMatch;
import com.adamkoch.iptables.matches.Match;
import com.adamkoch.iptables.objects.MacAddress;
import org.junit.jupiter.api.Test;

class RuleTest {

  @Test
  public void test() {
    Rule rule = new Rule(new ActionComponent.RejectActionComponent()); // ugly
    Match match = new MacAddressMatch(MacAddress.DUMMY);
    rule.addMatchingComponents(match);
    assertEquals("-m mac --mac-source 00:00:00:a1:2b:cc -j REJECT --reject-with tcp-reset", rule.asString());
  }
}