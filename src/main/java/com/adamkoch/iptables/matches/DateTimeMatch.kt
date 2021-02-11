package com.adamkoch.iptables.matches

import java.time.DayOfWeek
import java.time.format.TextStyle
import java.time.temporal.Temporal
import java.util.*

/**
 * The following description is from [http://ipset.netfilter.org/iptables-extensions.man.html#lbCH](http://ipset.netfilter.org/iptables-extensions.man.html#lbCH). <br></br>
 * Copied here for posterity. Please use the above webpage as official documentation.
 *
 * <h1>Please note that all of the following options are currently implemented.</h1>
 *
 * <h1>time</h1>
 *
 * This matches if the packet arrival time/date is within a given range. All options are optional, but are ANDed when specified. All times are interpreted as UTC by default.
 *
 * <dl>
 * <dt>
 * --datestart YYYY[-MM[-DD[Thh[:mm[:ss]]]]]
 * <br></br>
 * --datestop YYYY[-MM[-DD[Thh[:mm[:ss]]]]]
</dt> *
 * <dd>Only match during the given time, which must be in ISO 8601 "T" notation. The possible time range is 1970-01-01T00:00:00 to 2038-01-19T04:17:07.
 * If --datestart or --datestop are not specified, it will default to 1970-01-01 and 2038-01-19, respectively.</dd>
 * <dt>--timestart hh:mm[:ss]
 * <br></br>
 * --timestop hh:mm[:ss]</dt>
 * <dd>Only match during the given daytime. The possible time range is 00:00:00 to 23:59:59. Leading zeroes are allowed (e.g. "06:03") and correctly interpreted as base-10.</dd>
 * <dt>[!] --monthdays day[,day...]</dt>
 * <dd>Only match on the given days of the month. Possible values are 1 to 31. Note that specifying 31 will of course not match on months which do not have a 31st day; the same goes for 28- or 29-day February.</dd>
 * <dt>[!] --weekdays day[,day...]</dt>
 * <dd>Only match on the given weekdays. Possible values are Mon, Tue, Wed, Thu, Fri, Sat, Sun, or values from 1 to 7, respectively. You may also use two-character variants (Mo, Tu, etc.).</dd>
 * <dt>--contiguous</dt>
 * <dd>When --timestop is smaller than --timestart value, match this as a single time period instead distinct intervals. See EXAMPLES.</dd>
 * <dt>--kerneltz</dt>
 * <dd>Use the kernel timezone instead of UTC to determine whether a packet meets the time regulations.</dd>
</dl> *
 *
 *
 * About kernel timezones: Linux keeps the system time in UTC, and always does so. On boot, system time is initialized from a referential time source. Where this time source has no timezone information, such as the x86 CMOS RTC, UTC will be assumed. If the time source is however not in UTC, userspace should provide the correct system time and timezone to the kernel once it has the information.
 *
 *
 * Local time is a feature on top of the (timezone independent) system time. Each process has its own idea of local time, specified via the TZ environment variable. The kernel also has its own timezone offset variable. The TZ userspace environment variable specifies how the UTC-based system time is displayed, e.g. when you run date(1), or what you see on your desktop clock. The TZ string may resolve to different offsets at different dates, which is what enables the automatic time-jumping in userspace. when DST changes. The kernel's timezone offset variable is used when it has to convert between non-UTC sources, such as FAT filesystems, to UTC (since the latter is what the rest of the system uses).
 *
 *
 * The caveat with the kernel timezone is that Linux distributions may ignore to set the kernel timezone, and instead only set the system time. Even if a particular distribution does set the timezone at boot, it is usually does not keep the kernel timezone offset - which is what changes on DST - up to date. ntpd will not touch the kernel timezone, so running it will not resolve the issue. As such, one may encounter a timezone that is always +0000, or one that is wrong half of the time of the year. As such, **using --kerneltz is highly discouraged**.
 *
 *
 * EXAMPLES. To match on weekends, use:
 *
 *
 * -m time --weekdays Sa,Su
 *
 * Or, to match (once) on a national holiday block:
 *
 *
 * -m time --datestart 2007-12-24 --datestop 2007-12-27
 *
 * Since the stop time is actually inclusive, you would need the following stop time to not match the first second of the new day:
 *
 *
 * -m time --datestart 2007-01-01T17:00 --datestop 2007-01-01T23:59:59
 *
 * During lunch hour:
 *
 *
 * -m time --timestart 12:30 --timestop 13:30
 *
 * The fourth Friday in the month:
 *
 *
 * -m time --weekdays Fr --monthdays 22,23,24,25,26,27,28
 *
 * (Note that this exploits a certain mathematical property. It is not possible to say "fourth Thursday OR fourth Friday" in one rule. It is possible with multiple rules, though.)
 *
 *
 * Matching across days might not do what is expected. For instance,
 *
 *
 * -m time --weekdays Mo --timestart 23:00 --timestop 01:00 Will match Monday, for one hour from midnight to 1 a.m., and then again for another hour from 23:00 onwards. If this is unwanted, e.g. if you would like 'match for two hours from Montay 23:00 onwards' you need to also specify the --contiguous option in the example above.
 *
 * @author aakoch
 * @since 0.1.0
 */
// TODO: this needs work/more thought. Are we allowing for open-ended matches? We should. Instead of having separate start and end just have options? Hmm...
// Nail down the relationship with DateTimeExtensionMatchOption. Hint: why do both have useKernelTZ?
class DateTimeMatch : ExtensionMatch("time") {
    override val rank: Int = 50

    private var useKernelTZ: Boolean = false
    var start: Optional<DateTimeExtensionMatchOption>
        private set
    var end: Optional<DateTimeExtensionMatchOption>
        private set
    private var contiguousFlag: Boolean
    var days: Array<DayOfWeek?>
    fun setContiguous() {
        contiguousFlag = true
    }

    fun setEnd(endTemporal: Temporal) {
        end.ifPresent { throw IllegalStateException("end time is already set") }
        end = Optional.of(EndDateTimeExtensionMatchOption(endTemporal))
    }

    fun setStart(startTemporal: Temporal) {
        start.ifPresent { throw IllegalStateException("start time is already set") }
        start = Optional.of(StartDateTimeExtensionMatchOption(startTemporal))
    }

    /**
     * Allows for an Instant, LocalDateTime, DateTime, etc.
     */
    fun to(endTemporal: Temporal): DateTimeMatch {
        val newStringExtensionMatch = copy()
        newStringExtensionMatch.end = Optional.of(EndDateTimeExtensionMatchOption(endTemporal))
        return newStringExtensionMatch
    }

    fun from(startTemporal: Temporal): DateTimeMatch {
        val newStringExtensionMatch = copy()
        newStringExtensionMatch.start =
            Optional.of(StartDateTimeExtensionMatchOption(startTemporal))
        return newStringExtensionMatch
    }

    private fun copy(): DateTimeMatch {
        val newStringExtensionMatch = DateTimeMatch()
        newStringExtensionMatch.extensionMatchOptions = extensionMatchOptions
        newStringExtensionMatch.useKernelTZ = useKernelTZ
        newStringExtensionMatch.days = days
        newStringExtensionMatch.contiguousFlag = contiguousFlag
        newStringExtensionMatch.end = end
        newStringExtensionMatch.start = start
        return newStringExtensionMatch
    }

    override fun asString(): String {
        val optionsStringBuilder = StringBuilder(128)
        start.ifPresent { _start: DateTimeExtensionMatchOption ->
            optionsStringBuilder.append(' ')
            optionsStringBuilder.append(_start.asString())
        }
        end.ifPresent { _end: DateTimeExtensionMatchOption ->
            optionsStringBuilder.append(' ')
            optionsStringBuilder.append(_end.asString())
        }
        val options = extensionMatchOptions!!
        if (!options.isEmpty()) {
            for (option in options) {
                optionsStringBuilder.append(' ')
                optionsStringBuilder.append(option)
            }
        }
        if (contiguousFlag) {
            optionsStringBuilder.append(" --contiguous")
        }
        if (days.isNotEmpty()) {
            optionsStringBuilder.append("--weekdays ")
            for (dayOfWeek in days) {
                optionsStringBuilder.append(
                    dayOfWeek!!.getDisplayName(
                        TextStyle.SHORT,
                        Locale.ENGLISH
                    )
                )
                optionsStringBuilder.append(',')
            }
            optionsStringBuilder.deleteCharAt(optionsStringBuilder.length - 1)
        }
        return "-m " + super.type + (if(useKernelTZ) " --kerneltz" else "") + if (optionsStringBuilder.isEmpty()) "" else " " + optionsStringBuilder.toString()
            .trim { it <= ' ' }
    }

    fun setUseKernelTZ(b: Boolean) {
        useKernelTZ = b
    }

    init {
        start = Optional.empty()
        end = Optional.empty()
        contiguousFlag = false
        days = arrayOfNulls(0)
    }
}