package main;

import java.util.HashMap;
import java.util.Map;

import entry.Cell;
import network.Link;
import other.JSON;

public class GameLine {
	private Stage stage;
	private int status;
	private Cell[][] cell;
	private Link link;
	private int role;//1为房主扮演引路人|2为房主扮演逃生者|3为另一玩家扮演引路人|4为另一玩家扮演逃生者
	private int ping;
	private boolean isAlive;
	
	public GameLine(Link link,int role) {
		if(role==1||role==2) {
			stage=new Stage();
			new Thread(new RefreshStage()).start();
		}
		cell=null;
		this.link=link;
		this.role=role;
		ping=0;
		status=0;
		isAlive=true;
		new Thread(new Ping()).start();
		new Thread(new Process()).start();
	}
	
	public void end() {
		isAlive=false;
	}
	
	public int getStatus() {
		return status;
	}

	public int getRole(){return role;}
	
	public void up() {
		if(role==2) stage.up();
		if(role==4) {
			Map<String,String> map=new HashMap<>();
			map.put("type", "move");
			map.put("move", "up");
			try {
				link.putQ.put(JSON.mapToString(map));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void down() {
		if(role==2) stage.down();
		if(role==4) {
			Map<String,String> map=new HashMap<>();
			map.put("type", "move");
			map.put("move", "down");
			try {
				link.putQ.put(JSON.mapToString(map));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void left() {
		if(role==2) stage.left();
		if(role==4) {
			Map<String,String> map=new HashMap<>();
			map.put("type", "move");
			map.put("move", "left");
			try {
				link.putQ.put(JSON.mapToString(map));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void right() {
		if(role==2) stage.right();
		if(role==4) {
			Map<String,String> map=new HashMap<>();
			map.put("type", "move");
			map.put("move", "right");
			try {
				link.putQ.put(JSON.mapToString(map));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void click(int x,int y) {
		if(role==1) stage.click(x, y);
		if(role==3) {
			Map<String,String> map=new HashMap<>();
			map.put("type", "click");
			map.put("x", String.valueOf(x));
			map.put("y", String.valueOf(y));
			try {
				link.putQ.put(JSON.mapToString(map));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Cell[][] getStage(){
		if(role==1||role==2) return stage.getStage();
		return cell;
	}
	
	public int getPing() {
		return ping;
	}
	
	private void refreshPing(long time) {
		ping=(int)(System.currentTimeMillis()-time);
	}
	
	class Ping implements Runnable{
		@Override
		public void run() {
			while(true) {
				if(!isAlive) break;
				Map<String,String> map=new HashMap<>();
				map.put("type", "ping");
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
	
	class RefreshStage implements Runnable{
		@Override
		public void run() {
			int stage_order=0;
			while(true) {
				if(!isAlive) break;
				status=stage.getStatus();
				Map<String,String> map1=new HashMap<>();
				map1.put("type", "stage");
				map1.put("stage", JSON.cellToString(stage.getStage()));
				map1.put("stage_order", String.valueOf(stage_order));
				stage_order=(stage_order+1)%1000;
				Map<String,String> map2=new HashMap<>();
				map2.put("type", "status");
				map2.put("status", String.valueOf(status));
				try {
					link.putQ.put(JSON.mapToString(map1));
					link.putQ.put(JSON.mapToString(map2));
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class Process implements Runnable{
		@Override
		public void run() {
			int stage_old=-1,stage_new;
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
				case "ping":
					refreshPing(Long.parseLong(map.get("ping")));
					break;
				case "move":
					switch (map.get("move")) {
					case "up":
						stage.up();
						break;
					case "down":
						stage.down();
						break;
					case "left":
						stage.left();
						break;
					case "right":
						stage.right();
						break;
					default:
						break;
					}
					break;
				case "click":
					stage.click(Integer.parseInt(map.get("x")), Integer.parseInt(map.get("y")));
					break;
				case "stage":
					stage_new=Integer.parseInt(map.get("stage_order"));
					if(stage_new>stage_old||stage_old-stage_new>900){
						cell=JSON.stringTOCell(map.get("stage"));
						stage_old=stage_new;
					}
					break;
				case "status":
					status=Integer.parseInt(map.get("status"));
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
