package com.adamkoch.iptables;

import com.adamkoch.annotations.Unstable;
import com.adamkoch.iptables.matches.MacAddressMatch;
import com.adamkoch.iptables.matches.Match;
import com.adamkoch.iptables.matches.TcpKeywordMatch;
import com.adamkoch.iptables.matches.Udp1KeywordMatch;
import com.adamkoch.iptables.matches.Udp2KeywordMatch;
import com.adamkoch.iptables.objects.MacAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @since 0.1.0
 * @author aakoch
 */
@Unstable
public class RuleSet {

  private final String keyword;
  private final String chainName;
  List<Rule> rules = new ArrayList<>();

  public RuleSet(final String keyword, final String chainName, final MacAddress macAddress,
      final String routerIpAddress) {
    this.keyword = keyword;
    this.chainName = chainName;

    final TcpKeywordMatch keywordMatchingComponent1 = new TcpKeywordMatch(keyword);
    final Match keywordMatch2 = new Udp1KeywordMatch(keyword);
    final Match keywordMatch3 = new Udp2KeywordMatch(keyword, routerIpAddress +  "/32");
    final MacAddressMatch macAddressMatchingComponent = new MacAddressMatch(macAddress);

    Rule rule1 = new Rule(new ActionComponent.RejectActionComponent());
    rule1.addMatchingComponents(keywordMatchingComponent1);
    rules.add(rule1);

    Rule rule2 = new Rule(new ActionComponent.RejectActionComponent());
    rule2.addMatchingComponents(keywordMatch2);
    rules.add(rule2);

    Rule rule3 = new Rule(new ActionComponent.RejectActionComponent());
    rule3.addMatchingComponents(keywordMatch3);
    rules.add(rule3);

    rules.forEach(rule -> rule.addMatchingComponents(macAddressMatchingComponent));

  }

  @Override
  public String toString() {
    return rules.stream().map(Rule::asString).collect(Collectors.joining(System.lineSeparator()));
  }
}
