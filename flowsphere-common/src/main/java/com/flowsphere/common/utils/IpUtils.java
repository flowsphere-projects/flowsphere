package com.flowsphere.common.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class IpUtils {


    public static String getIpv4Address() throws SocketException {
        String ip = null;
        Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
        while (nis.hasMoreElements()) {
            NetworkInterface ni = nis.nextElement();
            Enumeration<InetAddress> ias = ni.getInetAddresses();
            while (ias.hasMoreElements()) {
                InetAddress ia = ias.nextElement();
                if (ia instanceof Inet4Address && !ia.isLoopbackAddress()) {
                    ip = ia.getHostAddress();
                }
            }
        }
        return ip;
    }

}
