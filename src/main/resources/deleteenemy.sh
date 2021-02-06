#!/bin/sh

# From https://www.lammertbies.nl/comm/info/iptables
echo "Deleting enemy $1"
iptables -D Enemies -s $1 -j DROP