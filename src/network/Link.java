package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

import other.Utils;

public class Link {
	
	private String id;
	private Socket socket;
	private String name;
	private String key;
	private boolean alive;
	private int ping;
    public ArrayBlockingQueue<String> putQ;
    public ArrayBlockingQueue<String> getQ;
	
	Link(Socket socket,String key){
		id=Utils.getUUID();
		this.socket=socket;
		this.key=key;
		alive=true;
		ping=0;
		name="";
        putQ=new ArrayBlockingQueue<String>(100);
        getQ=new ArrayBlockingQueue<String>(100);
        new Thread(new put()).start();
        new Thread(new get()).start();
	}
	
	public void close() {
		alive=false;
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public int getPing(){
		return ping;
	}
	
	public String getId(){
		return id;
	}
	
	public void setPing(int ping){
		this.ping=ping;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	class put implements Runnable {
	    @Override
	    public void run() {
	        while(true) {
	            if(!alive) break;
	            if(!putQ.isEmpty()){
	                String s="";
					try {
						s = putQ.take();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					s=Utils.encrypt(s, key);
//					System.out.println(s);
	                try {
	                    OutputStream os =socket.getOutputStream();
	                    byte[] bytes=Utils.mGetBytes(s, "ISO8859-1");
//						System.out.println(Utils.mNewString(bytes, "ISO8859-1"));
	                    int l=bytes.length;
//						System.out.println(l);
	                    os.write(l>>8);
	                    os.write(l);
	                	os.write(bytes);
//	                	System.out.println("已发送字符串:"+s);
	                } catch (IOException e) {
	                	System.out.println(String.format("与%s的连接发生异常，即将断开连接...", name));
	                    alive=false;
	                }
	            }
	        }
	    }
	}

	class get implements Runnable {
	    @Override
	    public void run() {
	        while(true) {
	            if(!alive) break;
	            try {
	                InputStream is =socket.getInputStream();
	                int i1=is.read();
	                if(i1==-1) break;
	                int i2=is.read();
	                int l=(i1<<8)+i2;
	                byte[] bytes=new byte[(i1<<8)+i2];
					System.out.println((i1<<8)+i2);
	                is.read(bytes, 0, (i1<<8)+i2);
	                String s=Utils.mNewString(bytes, "ISO8859-1");
	                String _s = new String(s);
//	                System.out.println("接收到字符串："+s);
	                s=Utils.decrypt(s, key);
	                try {
						getQ.put(s);
					} catch (InterruptedException e) {
	                	//System.out.println(_s);
						e.printStackTrace();
					}finally {
	                	System.out.println(_s);
	                	for(int i=0;i<l;i++) System.out.print(bytes[i]+" ");
						System.out.println();
					}
	            } catch (IOException e) {
                	System.out.println(String.format("与%s的连接发生异常，即将断开连接...", name));
	                alive=false;
	            }
	        }
	        alive=false;
	    }
	}
}