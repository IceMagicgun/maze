package main;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import network.Link;
import network.Server;
import other.JSON;

public class HomeownerLine {
	private boolean isAlive;
	private ArrayBlockingQueue<Map<String,String>> chatQueue;
	private GameLine gameLine;
	
	public HomeownerLine() {
		isAlive=true;
		gameLine=null;
		chatQueue=new ArrayBlockingQueue<Map<String,String>>(100);
		new Thread(new Ping()).start();
		new Thread(new Process()).start();
	}
	
	public void end() {
		isAlive=false;
	}
	
	public void game(Link link,int role) {
		gameLine=new GameLine(link,role);
		Map<String,String> map=new HashMap<>();
		map.put("type", "game");
		map.put("role", String.valueOf(role%2+1));
		try {
			link.putQ.put(JSON.mapToString(map));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public GameLine getGameLine() {
		return gameLine;
	}
	
	public void chat(String s) {
		Map<String,String> chatMap=new HashMap<>();
		chatMap.put("talker", Server.getName());
		chatMap.put("message", s);
		try {
			chatQueue.put(chatMap);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Map<String,String> map=new HashMap<>();
		map.put("type", "chat");
		map.put("talker", Server.getName());
		map.put("message", s);
		CopyOnWriteArrayList<Link> linkList=Server.getLinkList();
		String _s=JSON.mapToString(map);
		for(Link link:linkList) {
			try {
				link.putQ.put(_s);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ArrayBlockingQueue<Map<String,String>> getChat(){
		return chatQueue;
	}
	
	private void refreshPing(Link link,long time) {
		link.setPing(new Long(System.currentTimeMillis()-time).intValue());
	}
	
	class Ping implements Runnable{
		@Override
		public void run() {
			while(true) {
				if(!isAlive) break;
				CopyOnWriteArrayList<Link> linkList=Server.getLinkList();
				Map<String,String> map=new HashMap<>();
				map.put("type", "ping1");
				map.put("ping", String.valueOf(System.currentTimeMillis()));
				String s=JSON.mapToString(map);
				for(Link link:linkList) {
					try {
						link.putQ.put(s);
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
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
				CopyOnWriteArrayList<Link> linkList=Server.getLinkList();
				for(Link link:linkList) {
					s=link.getQ.poll();
					if(s==null) continue;
					Map<String,String> map=JSON.stringTOMap(s);
					switch (map.get("type")) {
					case "ping2":
						refreshPing(link,Long.parseLong(map.get("ping")));
						break;
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
					case "changeName":
						link.setName(map.get("name"));
						break;
					case "chat":
						CopyOnWriteArrayList<Link> linkList1=Server.getLinkList();
						for(Link link1:linkList1) {
							try {
								link1.putQ.put(s);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						Map<String,String> chatMap=new HashMap<>();
						chatMap.put("talker", map.get("talker"));
						chatMap.put("message", map.get("message"));
						try {
							chatQueue.put(chatMap);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
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
}
