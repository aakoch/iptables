I don't know what the next 2 lines do or what the difference is

    iptables -I FORWARD -i br0 -p udp -m mac --mac-source %s -m udp --dport 53 -m string --string "%s" --algo bm --to 65535 --icase -j DROP
    iptables -I INPUT -d 192.168.0.1/32 -i br0 -p udp -m mac --mac-source %s -m udp --dport 53 -m string --string "%s" --algo bm --to 65535 --icase -j DROP

I know the TCP keyword match wasn't enough because YouTube swiches to UDP if it can't connect via TCP.

