package network;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import entry.IP;
import other.JSON;
import other.Path;

public class Client implements Runnable{
    private String name;
    private String key;
    private Link link;
    private IP serverIp;
    
    public Client(IP ip,String name) {
    	this.name=name;
    	this.key=ip.key;
    	serverIp=ip;
    	new Thread(this).start();
    }

    @Override
    public void run() {
        Socket socket=null;
        int k=0;
        while(socket == null&&k<=10) {
            try {
            	socket = new Socket(serverIp.ip, Path.socketPort);
                System.out.println("连接服务器成功...");
            } catch (IOException e) {
            	k++;
                System.out.println("遇到异常:\n"+e.getMessage()+"\n连接服务器失败，即将重新连接...\n" + e);
                try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
            }
        }

        if(k>10) System.out.println("未能连接到服务器...\n");
        else {
        	link=new Link(socket,key);
        	changeName(name);
        }
    }
    
    public void changeName(String name) {
    	this.name=name;
		Map<String,String> map=new HashMap<>();
		map.put("type", "changeName");
		map.put("name",name);
    	try {
			link.putQ.put(JSON.mapToString(map));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public String getName() {
    	return name;
    }
    
    public Link getLink() {
    	return link;
    }
    
    public void close() {
    	link.close();
    }
}
