package com.adamkoch.iptables

import com.adamkoch.iptables.ActionComponent
import kotlin.jvm.JvmOverloads

/**
 * Represents the actions a rule can take.
 *
 * @since 0.1.0
 * @author aakoch
 */
open class ActionComponent {
    /**
     * Drop the packet
     */
    class DropActionComponent : ActionComponent() {
        override fun toString(): String {
            return "-j DROP"
        }
    }

    /**
     * Reject the packet
     */
    class RejectActionComponent @JvmOverloads constructor(private val rejectWithTcpReset: Boolean = true) :
        ActionComponent() {
        override fun toString(): String {
            return "-j REJECT" + if (rejectWithTcpReset) " --reject-with tcp-reset" else ""
        }
    }

    /**
     * Return from chain
     */
    class ReturnActionComponent : ActionComponent() {
        override fun toString(): String {
            return "-j RETURN"
        }
    }

    /**
     * Accept rule
     */
    class AcceptActionComponent : ActionComponent() {
        override fun toString(): String {
            return "-j ACCEPT"
        }
    }
}