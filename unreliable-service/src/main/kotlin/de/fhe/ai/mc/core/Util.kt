package de.fhe.ai.mc.core

import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*

/**
 * Private helper method to determine the IP address of the container
 * this application instance is running in. Mainly to check if round
 * robin load balancing of Traefik is working correctly.
 */
internal fun getIpAddresses(): String {
    val returnValue = StringBuffer()

    val e: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
    while (e.hasMoreElements()) {
        val n = e.nextElement()
        val ee: Enumeration<InetAddress> = n.inetAddresses
        while (ee.hasMoreElements()) {
            val i = ee.nextElement()
            if (!i.hostAddress.equals("127.0.0.1") && i.hostAddress.indexOf(".") == 3)
                returnValue.append(i.hostAddress).append(" ")
        }
    }

    return returnValue.trim().toString()
}