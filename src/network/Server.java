package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import other.JSON;
import other.Path;
import other.Utils;

public class Server implements Runnable {
    private static Thread t;
    private static ServerSocket server;
    private static String name;
    private static String key;
    private static CopyOnWriteArrayList<Link> linkList;
    private static boolean isAlive;
    private static Lock lock;
    
    static {
		lock=new ReentrantLock();
    	t=null;
        server=null;
    	name="一个神秘的房间";
    	key=Utils.getUUID();
    	linkList=new CopyOnWriteArrayList<>();
    	isAlive=false;
    }
	
    @Override
    public void run() {
        Socket socket;
        int k=0;
        while(server == null&&k<=10) {
            try {
                server = new ServerSocket(Path.socketPort);
                System.out.println("服务器启动成功...");
            } catch (IOException e) {
            	k++;
                System.out.println("遇到异常:\n"+e.getMessage()+"\n服务器启动失败，即将重新启动...\n" + e);
                try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
            }
        }

        if(k>10) System.out.println("服务器未能正常启动...\n");
        else {
        	new Thread(new Broadcast()).start();
        	new Thread(new RefreshLinkList()).start();
	        while(true) {
	            try {
	                socket = server.accept();
	                String ip=socket.getInetAddress().toString().substring(1);
	                System.out.println(String.format("与%s建立连接...",ip));
	                lock.lock();
	                linkList.add(new Link(socket,key));
	                lock.unlock();
	                changeName(name);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
        }
    }
    
    public static void changeName(String _name) {
    	name=new String(_name);
		Map<String,String> map=new HashMap<>();
		map.put("type", "changeName");
		map.put("name",name);
		for(Link link:linkList)
    	try {
			link.putQ.put(JSON.mapToString(map));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public static String getName() {
    	return name;
    }    
    
    public static CopyOnWriteArrayList<Link> getLinkList() {
    	return linkList;
    }

    public void start(){
    	isAlive=true;
        if (t == null) {
            t = new Thread (this);
            t.start();
        }
    }
    
    public void end() {
    	isAlive=false;
    	if(server==null) return;
    	try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    class RefreshLinkList implements Runnable{
		@Override
		public void run() {
			while(true) {
				if(!isAlive) break;
				CopyOnWriteArrayList<Link> newLinkList=new CopyOnWriteArrayList<Link>();
				lock.lock();
				for(Link link:linkList) {
					if(link.isAlive()) newLinkList.add(link);
				}
				linkList=newLinkList;
				lock.unlock();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
    }
    
    class Broadcast implements Runnable{
		@Override
		public void run() {
			DatagramSocket socket=null;
			try {
				socket = new DatagramSocket(Path.UDPPort);
			} catch (SocketException e1) {
				e1.printStackTrace();
			}
			while(true) {
				if(!isAlive) break;
				
				Map<String,String> map=new HashMap<>();
				map.put("key", key);
				map.put("name", name);
				byte[] data=null;
				data = Utils.mGetBytes(Utils.encrypt(JSON.mapToString(map), Utils.getMD5(Path.ver)),"ISO8859-1");
				DatagramPacket packet = new DatagramPacket(data, data.length, LANAddressTool.getLANAddressOnWindows(), Path.UDPPort);
				
				try {
					if(socket!=null) socket.send(packet);
//					System.out.println("已广播:"+Utils.encrypt(JSON.mapToString(map), Utils.getMD5(Path.ver)));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(socket!=null) socket.close();
		}
    }
}
