package network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import entry.IP;
import other.JSON;
import other.Path;
import other.Utils;

public class FindServer {
	
	private List<IP> severIpList;
	private boolean isAlive;
	private DatagramSocket socket;
	
	public FindServer() {
		severIpList=new CopyOnWriteArrayList<>();
		isAlive=true;
		socket=null;
		try {
			socket = new DatagramSocket(Path.UDPPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		new Thread(new FindSeverRunnable()).start();
	}
	
	public List<IP> getSeverIpList(){
		return severIpList;
	}
	
	public void end() {
		isAlive=false;
	}
	
	class FindSeverRunnable implements Runnable{
		@Override
		public void run() {
			int i=0;
			while(true) {
				if(i>6000||!isAlive) return;
				if(i%100==0) severIpList.clear();
				byte[] data = new byte[1024];
		        DatagramPacket packet = new DatagramPacket(data, data.length);
		        try {
					socket.receive(packet);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		        String s=null;
				try {
					s = new String(data, 0, packet.getLength(),"ISO8859-1");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
		        s=Utils.decrypt(s, Utils.getMD5(Path.ver));
		        Map<String,String> map=JSON.stringTOMap(s);
		        IP ip=new IP();
		        ip.ip=packet.getAddress().getHostAddress();
		        ip.key=map.get("key");
		        ip.name=map.get("name");
		        boolean flag=true;
		        for(IP _ip:severIpList) {
		        	if(ip.ip.equals(_ip.ip)) {
		        		flag=false;
		        		break;
		        	}
		        }
		        if(flag) severIpList.add(ip);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				i++;
			}
		}
	}

}
