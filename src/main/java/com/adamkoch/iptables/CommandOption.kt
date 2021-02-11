package com.adamkoch.iptables

enum class CommandOption {

    APPEND, INSERT, DELETE;

    override fun toString(): String {
        return this.name.slice(IntRange(0, 0))
    }

}
