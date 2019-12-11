package test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

import entry.Cell;
import entry.IP;
import entry.Point;
import main.GameLine;
import main.HomeownerLine;
import main.RoomLine;
import main.Stage;
import network.Client;
import network.FindServer;
import network.Link;
import network.Server;
import other.Utils;

public class test {

	public static void main(String[] args) {

//		Queue<Point> queue=new LinkedList<>();
//		Point point=new Point();
//		point.x=1;point.y=1;
//		queue.offer(point);
//		point.x=2;point.y=2;
//		queue.offer(point);
//		point.x=3;point.y=3;
//		System.out.println(queue.poll().x+"   "+queue.poll().y);
		
//		Stage stage=new Stage();
//		Cell[][] cell=stage.getStage();
//		for(int i=0;i<stage.getHigh();i++) {
//			for(int j=0;j<stage.getWidth();j++) {
//				System.out.print(cell[i][j].type);
//			}
//			System.out.println();
//		}
		
//		new Server().start();
		
		
		//a0a0811277b33603020127b20021d315]]zfusj\ê??í??ò?¨?§?é?????ó??de]]pj?iheBr:6B::g;?e5nC2;?:<<;And7g93qElkj
//		FindServer findServer=new FindServer();
//		while(true) {
//			List<IP> list=findServer.getSeverIpList();
//			for(IP ip:list)
//				System.out.println(ip.name+":"+ip.ip);
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
		
//		long l1=System.currentTimeMillis();
//		String s1=String.valueOf(l1);
//		l1=Long.parseLong(s1);
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e1) {
//			e1.printStackTrace();
//		}
//		
//		System.out.println(new Long(System.currentTimeMillis()-l1).intValue());
		
		Scanner sc=new Scanner(System.in);
		System.out.println("1:创建房间\n2:寻找服务器");
		int i=sc.nextInt();
		if(i==1) {
			Server server=new Server();
			server.start();
			HomeownerLine homeownerLine=new HomeownerLine();
			new test1(homeownerLine.getChat());
			while(true) {
				System.out.println("1:聊天\n2:改变名称\n3:查看已连接\n4:开始游戏");
				int j=sc.nextInt();
				if(j==1) {
					String s=sc.next();
					homeownerLine.chat(s);
				}
				if(j==2) {
					String s=sc.next();
					server.changeName(s);
				}
				if(j==3) {
					List<Link> links=Server.getLinkList();
					for(Link link:links) {
						System.out.println(link.getName()+"   ping:"+link.getPing());
					}
				}
				if(j==4) {
					System.out.println("请输入要连接的对象和自己要扮演的角色");
					List<Link> links=Server.getLinkList();
					GameLine gameLine=new GameLine(links.get(sc.nextInt()),sc.nextInt());
				}
			}
		}
		if(i==2) {
			FindServer findServer=new FindServer();
			int j=0;
			while(j<10) {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				j++;
			}
			System.out.println("请选择要进入的房间并输入自己的昵称");
			List<IP> list=findServer.getSeverIpList();
			j=0;
			for(IP ip:list)
				System.out.println(j+++ip.name+":"+ip.ip);
			j=sc.nextInt();
			Client client=new Client(list.get(j),sc.next());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			RoomLine roomLine=new RoomLine(client);
			new test1(roomLine.getChat());
			while(true) {
				System.out.println("1:聊天\n2:改变名称\n3:查看ping值");
				j=sc.nextInt();
				if(j==1) {
					roomLine.chat(sc.next());
				}
				if(j==2) {
					roomLine.changeName(sc.next());
				}
				if(j==3) {
					System.out.println(roomLine.getPing());
				}
			}
		}
		
		
//		System.out.println(System.currentTimeMillis());
		
//		new Server().start();
//		try {
//			Socket client = new Socket("10.2.0.5", 19194);
//	         OutputStream outToServer = client.getOutputStream();
//	         DataOutputStream out = new DataOutputStream(outToServer);
//	         out.writeUTF("Hello from\n " + client.getLocalSocketAddress());
//	         out.writeUTF("aha " + client.getLocalSocketAddress());
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
//		String s1=Utils.getMD5(""),s2=Utils.getUUID();
//		System.out.println(s1+"\n"+s2+"\n"+Utils.sadd(s1, s2));
		
//		for(int i=0;i<96;i++) {
//			System.out.println((i+i%3)%32);
//		}

//		Scanner sc=new Scanner(System.in);
//		String s,key;
//		s=null;
//		key=Utils.getUUID();
//		System.out.println(key);
//		System.out.println(Utils.encrypt(s, key));
//		System.out.println(Utils.decrypt(Utils.encrypt(s, key), key));
//		while(true) {
//			s=sc.next();
//			key=Utils.getUUID();
//			System.out.println(key);
//			System.out.println(Utils.encrypt(s, key));
//			System.out.println(Utils.decrypt(Utils.encrypt(s, key), key));
//		}
		
		
//		Scanner sc=new Scanner(System.in);
//		while(true) {
//			int i=sc.nextInt();
//			if(i==1) {
//				int x=sc.nextInt();
//				System.out.println((char)Utils.change_sadd(x));
//			}
//			else {
//				byte[] c=sc.next().getBytes();
//				System.out.println(""+Utils.change_sadd(c[0]));
//			}
//		}
		
		
//		new test1();
	}
}
