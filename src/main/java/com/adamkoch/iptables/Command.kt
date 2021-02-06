package com.adamkoch.iptables

import com.adamkoch.annotations.Unstable
import java.util.*

/**
 *
 * @since 0.1.0
 * @author aakoch
 */
@Unstable
class Command {
    private val command: String
    private var comment: Optional<String>

    private constructor(command: String) {
        this.command = command
        comment = Optional.empty()
    }

    private constructor(command: String, comment: String) {
        this.command = command
        this.comment = Optional.of(comment)
    }

    fun withComments(): Command {
        return Command(command, comment.orElse("no comment provided"))
    }

    companion object {
        const val IPTABLES_COMMAND = "iptables"
        fun from(rule: Rule): Command {
            val command = Command(IPTABLES_COMMAND + " " + rule.asString())
            command.comment = Optional.ofNullable(rule.comment())
            return command
        }
    }
}