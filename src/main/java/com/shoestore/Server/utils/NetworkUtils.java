package com.shoestore.Server.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkUtils {
    public static String getIpAddress() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            return ip.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }
}
