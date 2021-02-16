package com.adamkoch.iptables.matches

class DestinationPortOption(val port: Int) : ExtensionMatchOption {

    override fun asString(): String {
        return "--dport $port"
    }

    companion object {
        private fun requireWithinPortRange(value: Int) {
            require(!(value < 0 || value > 65535)) { "port \"$value\" is outside the allowed port range of 0 - 65535" }
        }
    }

    init {
        requireWithinPortRange(port)
    }

}
