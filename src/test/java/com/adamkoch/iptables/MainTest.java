package com.adamkoch.iptables;

import static org.junit.jupiter.api.Assertions.*;

import com.adamkoch.iptables.matches.MacAddressMatch;
import com.adamkoch.iptables.matches.Match;
import com.adamkoch.iptables.objects.Device;
import com.adamkoch.iptables.objects.MacAddress;
import org.junit.jupiter.api.Test;

class MainTest {

  @Test
  public void testSimpleChain() {
    Device joelsOmen = new Device("Joel's Omen", new MacAddress("00:00:00:00:00:00"));
    Match match = new MacAddressMatch(joelsOmen.getMacAddress()).not();
    ActionComponent actionComponent = new ActionComponent.ReturnActionComponent();
    Rule returnMacNotMatchingRule = createRule(actionComponent, match);
    assertEquals("-m mac ! --mac-source 00:00:00:00:00:00 -j RETURN", returnMacNotMatchingRule.asString());
    String name = Util.sanitize("Joel's Omen");
    Chain chain = new Chain("Joel's Omen");
    chain.add(returnMacNotMatchingRule);
    assertEquals(name + " -m mac ! --mac-source 00:00:00:00:00:00 -j RETURN", chain.toString());

    ScriptWriter sc = new ScriptWriter();
    sc.add(chain);
    assertEquals("# Creates a new user-defined chain named Joels_Omen\n"
                     + "iptables -N Joels_Omen\n"
                     + "# Adds jump point for INPUT chain\n"
                     + "iptables -I INPUT -j Joels_Omen\n"
                     + "# Adds jump point for FORWARD chain\n"
                     + "iptables -I FORWARD -j Joels_Omen\n"
                     + "iptables -A Joels_Omen -m mac ! --mac-source 00:00:00:00:00:00 -j RETURN" + System.lineSeparator()
                     , sc.toString());

    //            // iptables -N Adam
//            // iptables -I INPUT -j Adam
//            // iptables -I FORWARD -j Adam
//            // iptables -A Adam -p tcp -m mac ! --mac-source 00:00:00:00:00:00 -j RETURN
//            // iptables -A Adam -p tcp -m time --kerneltz --timestart 18:45 --timestop 18:48 --weekdays Mon,Tue,Wed,Thu,Fri -j REJECT
  }

  private Rule createRule(final ActionComponent actionComponent, final Match match) {
    Rule rule = new Rule(actionComponent);
    rule.addMatch(match);
    return rule;
  }
  
  @Test
  public void testRuleSet() {
    //  -m time --kerneltz --timestart 07:45 --timestop 11:00 --weekdays Mon,Tue,Wed,Thu,Fri


    RuleSet ruleSet = new RuleSet("discord", "FORWARD", new MacAddress("00:00:00:00:01"), "255.255.255.255");
    String actual = ruleSet.toString();
    assertEquals("-p tcp -m webstr --url discord -m mac --mac-source 00:00:00:00:01 -j REJECT --reject-with tcp-reset\n"
                     + "-i br0 -p udp -m udp --dport 53 -m string --string \"discord\" --algo bm --to 65535 --icase -m mac --mac-source 00:00:00:00:01 -j REJECT --reject-with tcp-reset\n"
                     + "-d 255.255.255.255/32 -i br0 -p udp -m udp --dport 53 -m string --string \"discord\" --algo bm --to 65535 --icase -m mac --mac-source 00:00:00:00:01 -j REJECT --reject-with tcp-reset", actual);
  }
}