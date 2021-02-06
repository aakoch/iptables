package com.adamkoch.iptables

import com.adamkoch.annotations.Unstable
import java.util.stream.Collectors
import java.util.Optional
import com.adamkoch.iptables.TimeRange
import java.util.ArrayList

/**
 *
 * @since 0.1.0
 * @author aakoch
 */
@Unstable
class Chain(val name: String) {
    private val rules: MutableList<Rule>
    override fun toString(): String {
        val sanitized = Util.sanitize(name)
        return rules.stream().map { rule: Rule -> sanitized + " " + rule.asString() }
            .collect(Collectors.joining(System.lineSeparator()))

//    StringBuilder timeScheduleString = new StringBuilder();
//    timeSchedule.ifPresent(ts -> {
//      ts.getTimeRanges().forEach(timeRange -> {
//        timeScheduleString.append("iptables -A " + sanitized + " -p tcp -m time --kerneltz --timestart " + timeRange.getStartTime() + " --timestop " + timeRange.getEndTime() + " -j REJECT\n");
//      });
//    });
//
//    return "iptables -N " + sanitized + "\n" +
//    "iptables -I INPUT -j " + sanitized + "\n" +
//    "iptables -I FORWARD -j " + sanitized + "\n" +
//    "iptables -A " + sanitized + " -p tcp -m mac ! --mac-source " + device.get().getMacAddress() + " -j RETURN\n" +
//
////            // iptables -A Adam -p tcp -m time --kerneltz --timestart 18:45 --timestop 18:48 --weekdays Mon,Tue,Wed,Thu,Fri -j REJECT
//
//        timeScheduleString.toString() +
////    "iptables -A " + sanitized + " -p tcp -m time --kerneltz --timestart 18:45 --timestop 18:48 --weekdays Mon,Tue,Wed,Thu,Fri -j REJECT\n"
//        "iptables -A " + sanitized + " -j RETURN\n";
    }

    fun add(rule: Rule) {
        rules.add(rule)
    }

    fun getRules(): List<Rule> {
        return rules
    }

    init {
        rules = ArrayList()
    }
}