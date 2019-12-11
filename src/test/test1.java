package test;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import main.RoomLine;

public class test1 {
	
	test1(ArrayBlockingQueue<Map<String,String>> chatQueue) {
//        new Thread(new AAA()).start();
//        new Thread(new BBB()).start();
		new Thread(new Chat(chatQueue)).start();
	}
	
	class AAA implements Runnable {
	    @Override
	    public void run() {
	    	test2.aaa();
	    }
	}
	class BBB implements Runnable {
	    @Override
	    public void run() {
	    	test2.bbb();
	    }
	}
	
	public class Chat implements Runnable{
		private ArrayBlockingQueue<Map<String,String>> chatQueue;
		
		public Chat(ArrayBlockingQueue<Map<String,String>> chatQueue){
			this.chatQueue=chatQueue;
		}
		@Override
		public void run() {
			while(true) {
				try {
					Map<String,String> map=chatQueue.take();
					System.out.println(map.get("talker")+":"+map.get("message"));
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
