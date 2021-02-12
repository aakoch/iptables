package com.adamkoch.iptables

import com.adamkoch.annotations.Unstable
import com.adamkoch.iptables.matches.Match
import java.util.ArrayList

/**
 *
 * @since 0.1.0
 * @author aakoch
 */
@Unstable
open class Rule(val target: Target) {
    val matches: MutableList<Match>

    fun comment(): String? {
        return null
    }

    // Usage: iptables -[ACD] chain rule-specification [options]
    //  public String header() {
    //    StringJoiner sj = new StringJoiner("\n");
    //    String firstLine = "#".repeat(10) + "  Device:  " + getDevice().toString() + "  " + "#".repeat(10);
    //    sj.add("# " + firstLine);
    //    String secondLine = "#".repeat(10) + "  Keyword: " + getKeyword() + "  ";
    //    sj.add("# " + secondLine + " ".repeat(firstLine.length() - secondLine.length() - 10) + "#".repeat(10));
    //    return sj.toString();
    //  }
    //  protected String toFirstIPTableRule() {
    //    return matchingComponents.stream().map(Object::toString).collect(Collectors.joining(" ")) + " " + actionComponent.toString();
    //    return String
    //        .format(
    //            "iptables -I FORWARD -p tcp -m mac --mac-source %s -m webstr --url %s -j REJECT --reject-with tcp-reset",
    //            device.getMacAddress(), keyword);
    //  }
    //  protected String toSecondIPTableRule() {
    //    return String.format(
    //        "iptables -I FORWARD -i br0 -p udp -m mac --mac-source %s -m udp --dport 53 -m string --string \"%s\" --algo bm --to 65535 --icase -j DROP",
    //        device.getMacAddress(), keyword);
    //  }
    //
    //  protected String toThirdIPTableRule() {
    //    return String.format(
    //        "iptables -I INPUT -d 192.168.0.1/32 -i br0 -p udp -m mac --mac-source %s -m udp --dport 53 -m string --string \"%s\" --algo bm --to 65535 --icase -j DROP",
    //        device.getMacAddress(), keyword);
    //  }
    open fun asString(): String {
        return matches.sorted().joinToString (" ", transform = Match::asString ) + " -j " + target.toString()
    }

    fun addMatch(vararg matches: Match) {

        for (match in matches) {
            this.matches.add(match)
        }
    }

    fun addMatch(match: Match) {
        this.matches.add(match)
    }

    init {
        matches = ArrayList()
    }
}