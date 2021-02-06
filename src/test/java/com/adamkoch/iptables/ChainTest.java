package com.adamkoch.iptables;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.adamkoch.iptables.ActionComponent.AcceptActionComponent;
import com.adamkoch.iptables.ActionComponent.DropActionComponent;
import com.adamkoch.iptables.ActionComponent.RejectActionComponent;
import com.adamkoch.iptables.ActionComponent.ReturnActionComponent;
import com.adamkoch.iptables.matches.MacAddressMatch;
import com.adamkoch.iptables.matches.Match;
import com.adamkoch.iptables.matches.WebStringExtensionMatch;
import com.adamkoch.iptables.objects.MacAddress;
import org.junit.jupiter.api.Test;

@SuppressWarnings("DuplicateStringLiteralInspection")
class ChainTest {

  public static final String INVALID_CHAIN_NAME = "Adam's Computer";
  public static final String SANITIZED_CHAIN_NAME = Util.sanitize(INVALID_CHAIN_NAME);

  @Test
  void basic() {
    Chain chain = new Chain(INVALID_CHAIN_NAME);
    chain.add(getMacAddrRule());
    chain.add(getWebstrRule());
    assertEquals("Adams_Computer -m mac --mac-source 00:00:00:a1:2b:cc -j DROP" + System.lineSeparator()
                     + "Adams_Computer -m webstr --url keyword -j REJECT --reject-with tcp-reset", chain.toString());
  }

  @Test
  void inverse() {
    Chain chain = new Chain(INVALID_CHAIN_NAME);
    chain.add(getAllowMacAddrRule());
    chain.add(getWebstrRule());
    assertEquals("Adams_Computer -m mac --mac-source 00:00:00:a1:2b:cc -j ACCEPT" + System.lineSeparator()
                     + "Adams_Computer -m webstr --url keyword -j REJECT --reject-with tcp-reset", chain.toString());
  }

  @Test
  void shortCircuit() {
    Chain chain = new Chain(INVALID_CHAIN_NAME);
    chain.add(getReturnWhenNotMacAddrRule());
    chain.add(getWebstrRule());

    assertEquals("Adams_Computer -m mac ! --mac-source 00:00:00:a1:2b:cc -j RETURN" + System.lineSeparator()
                     + "Adams_Computer -m webstr --url keyword -j REJECT --reject-with tcp-reset", chain.toString());


    // -A Alyssa -p tcp -m mac ! --mac-source 00:00:00:00:00:00 -j RETURN
    // -A Alyssa -p tcp -m time --kerneltz --timestart 18:45 --timestop 18:46 --weekdays Mon,Tue,Wed,Thu,Fri -j RETURN
    // -A Alyssa -p tcp -m webstr --url hulu -j REJECT --reject-with tcp-reset

  }

  private Rule getWebstrRule() {
    Rule rule = new Rule(new RejectActionComponent());
    Match match = new WebStringExtensionMatch("keyword");
    rule.addMatch(match);
    return rule;
  }

  private Rule getMacAddrRule() {
    Rule rule = new Rule(new DropActionComponent());
    rule.addMatch(new MacAddressMatch(MacAddress.DUMMY));
    return rule;
  }

  private Rule getAllowMacAddrRule() {
    Rule rule = new Rule(new AcceptActionComponent());
    rule.addMatch(new MacAddressMatch(MacAddress.DUMMY));
    return rule;
  }

  private Rule getReturnWhenNotMacAddrRule() {
    Rule rule = new Rule(new ReturnActionComponent());
    rule.addMatch(new MacAddressMatch(MacAddress.DUMMY).not());
    return rule;
  }
}