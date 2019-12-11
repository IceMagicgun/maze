package network;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

public class LANAddressTool {
	public static InetAddress getLANAddressOnWindows() {
		NetworkInterface netInterface;
		try {
			netInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
		    if (!netInterface.isLoopback()&& netInterface.isUp()) {
		        List<InterfaceAddress> interfaceAddresses = netInterface.getInterfaceAddresses();
		        for (InterfaceAddress interfaceAddress : interfaceAddresses) {
		            if(interfaceAddress.getBroadcast()!= null){
		                return interfaceAddress.getBroadcast();
		            }
		        }
		    }
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
}
