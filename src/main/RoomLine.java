package main;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import network.Client;
import network.Link;
import other.JSON;

public class RoomLine {
	private boolean isAlive;
	private Link link;
	private ArrayBlockingQueue<Map<String,String>> chatQueue;
	private GameLine gameLine;
	private Client client;
	
	public RoomLine(Client client) {
		isAlive=true;
		gameLine=null;
		this.client=client;
		this.link=client.getLink();
		chatQueue=new ArrayBlockingQueue<Map<String,String>>(100);
		new Thread(new Ping()).start();
		new Thread(new Process()).start();
	}
	
	public void end() {
		isAlive=false;
	}
	
	public void changeName(String s) {
		client.changeName(s);
	}
	
	public void chat(String s) {
		Map<String,String> map=new HashMap<>();
		map.put("type", "chat");
		map.put("talker", client.getName());
		map.put("message", s);
		try {
			link.putQ.put(JSON.mapToString(map));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayBlockingQueue<Map<String,String>> getChat(){
		return chatQueue;
	}
	
	public GameLine getGameLine() {
		return gameLine;
	}
	
	public int getPing() {
		return link.getPing();
	}
	
	private void refreshPing(long time) {
		link.setPing(new Long(System.currentTimeMillis()-time).intValue());
	}
	
	class Ping implements Runnable{
		@Override
		public void run() {
			while(true) {
				if(!isAlive) break;
				Map<String,String> map=new HashMap<>();
				map.put("type", "ping1");
				map.put("ping", String.valueOf(System.currentTimeMillis()));
				try {
					link.putQ.put(JSON.mapToString(map));
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class Process implements Runnable{
		@Override
		public void run() {
			while(true) {
				if(!isAlive) break;
				String s=null;
				try {
					s=link.getQ.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Map<String,String> map=JSON.stringTOMap(s);
				switch (map.get("type")) {
				case "ping1":
					Map<String,String> map1=new HashMap<>();
					map1.put("type", "ping2");
					map1.put("ping",map.get("ping"));
					try {
						link.putQ.put(JSON.mapToString(map1));
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					break;
				case "ping2":
					refreshPing(Long.parseLong(map.get("ping")));
					break;
				case "changeName":
					link.setName(map.get("name"));
					break;
				case "chat":
					Map<String,String> chatMap=new HashMap<>();
					chatMap.put("talker", map.get("talker"));
					chatMap.put("message", map.get("message"));
					try {
						chatQueue.put(chatMap);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
				case "game":
					gameLine=new GameLine(link,Integer.parseInt(map.get("role")+2));
					break;
				default:
					try {
						link.getQ.put(s);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
	}
}
