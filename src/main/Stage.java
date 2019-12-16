package main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import entry.Cell;
import entry.Point;

public class Stage {
	
	private int high,width;
	
	public int getHigh() {
		return high;
	}

	public int getWidth() {
		return width;
	}

	private Lock lock;
	private int time;
	private boolean isAlive;
	private Cell[][] stage;
	private int x,y;
	private int status;//0:游戏进行中|1:通关成功|2:通关失败
	
	public Stage() {
		high=width=31;
		lock=new ReentrantLock();
		time=0;
		isAlive=true;
		status=0;
		stage=makeStage();
		new Thread(new Timing()).start();
		new Thread(new Refresh()).start();
	}
	
	public int getStatus() {
		return status;
	}
	
	public Cell[][] getStage() {
		Cell[][] ans=new Cell[high][width];
		for(int i=0;i<high;i++)
			for(int j=0;j<width;j++)
				ans[i][j]=stage[i][j].clone();
		return ans;
	}

	class Timing implements Runnable{
		@Override
		public void run() {
			while(true) {
				if(!isAlive) break;
				time++;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	class Refresh implements Runnable{
		@Override
		public void run() {
			while(true) {
				if(!isAlive) break;
				lock.lock();
				for(int i=0;i<high;i++) {
					for(int j=0;j<width;j++) {
						int timeX=time-stage[i][j].timePoint;
						if(timeX<stage[i][j].time1) {
							stage[i][j].status=1;
							stage[i][j].time=stage[i][j].time1-timeX;
						}else if(timeX<stage[i][j].time1+stage[i][j].time2) {
							stage[i][j].status=2;
							stage[i][j].time=stage[i][j].time1+stage[i][j].time2-timeX;
						}else{
							stage[i][j].status=0;
							stage[i][j].time=0;
						}
					}
				}
				switch (stage[x][y].type) {
					case 1:
						status=2;
						isAlive=false;
						break;
					case 2:
						if(stage[x][y].status==2) {
							status=2;
							isAlive=false;
						}
						break;
					case 3:
						if(stage[x][y].status==0||stage[x][y].status==2) {
							status=2;
							isAlive=false;
						}
						break;
					case 4:
						status=1;
						isAlive=false;
						break;
					default:
						break;
				}
				lock.unlock();
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void trigger() {
		if(stage[x][y].type==2&&stage[x][y].status==0) {
			stage[x][y].status=1;
			stage[x][y].timePoint=time;
			change(x,y,time);
		}
	}
	
	public void up() {
		lock.lock();
		stage[x][y].isLead=false;
		x--;
		stage[x][y].isLead=true;
		trigger();
		lock.unlock();
	}
	
	public void down() {
		lock.lock();
		stage[x][y].isLead=false;
		x++;
		stage[x][y].isLead=true;
		trigger();
		lock.unlock();
	}
	
	public void left() {
		lock.lock();
		stage[x][y].isLead=false;
		y--;
		stage[x][y].isLead=true;
		trigger();
		lock.unlock();
	}
	
	public void right() {
		lock.lock();
		stage[x][y].isLead=false;
		y++;
		stage[x][y].isLead=true;
		trigger();
		lock.unlock();
	}
	
	public void click(int x,int y) {
		lock.lock();
		if(stage[x][y].type==3&&stage[x][y].status==0) {
			stage[x][y].status=1;
			stage[x][y].timePoint=time;
			change(x,y,time);
		}
		lock.unlock();
	}
	
	private void change(int x,int y,int time) {
		Queue<Point> queue=new LinkedList<>();
		Point point;
		int[][] w=new int[][] {{-1,0},{1,0},{0,-1},{0,1}};
		boolean[][] flag=new boolean[high][width];

		for(int i=0;i<high;i++)
			for(int j=0;j<width;j++)
				flag[i][j]=true;
		queue.offer(new Point(x,y));
		flag[x][y]=false;
		while(!queue.isEmpty()) {
			int _x,_y;
			point=queue.poll();
			stage[point.x][point.y].timePoint=time;
			for(int i=0;i<4;i++) {
				_x=point.x+w[i][0];
				_y=point.y+w[i][1];
				if(flag[_x][_y]&&stage[_x][_y].label==stage[point.x][point.y].label) {
					queue.offer(new Point(_x,_y));
					flag[_x][_y]=false;
				}
			}
		}
	}
	
	private Cell[][] makeStage(){
		Cell[][] ans=new Cell[high][width];
		boolean[][] flag=new boolean[high][width];
		boolean[][] flag1=new boolean[10][10];
		Random random=new Random();
		int x,y;
		for(int i=0;i<high;i++)
			for(int j=0;j<width;j++)
				flag[i][j]=true;
		for(int i=0;i<10;i++)
			for(int j=0;j<10;j++)
				flag1[i][j]=true;
		
		for(int i=0;i<high;i++) {
			for(int j=0;j<width;j++) {
				ans[i][j]=new Cell();
				if(i%2==0||j%2==0) {
					ans[i][j].type=1;
					flag[i][j]=false;
				}
			}
		}

		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				do {
					x=random.nextInt(10);
					y=random.nextInt(10);
				}while(i*10+x==0||i*10+x==high-1||j*10+y==0||j*10+y==width-1||!flag1[x][y]);
				flag1[x][y]=false;
				flag[i*10+x][j*10+y]=false;
				ans[i*10+x][j*10+y].type=5;
			}
		}
		
		do {
			x=random.nextInt(15)*2+1;
			y=random.nextInt(15)*2+1;
		}while(!flag[x][y]);
		ans[x][y].isLead=true;
		this.x=x;
		this.y=y;
		ans[x][y].type=0;
		flag[x][y]=false;
		
		List<Point> list=new ArrayList<>();
		Point point;
		int[][] w=new int[][] {{-1,0},{1,0},{0,-1},{0,1}};
		int _x,_y;
		for(int i=0;i<4;i++) {
			_x=x+w[i][0];
			_y=y+w[i][1];
			if(ans[_x][_y].type==1) list.add(new Point(_x,_y));
		}
		while(list.size()>0) {
			point=list.remove(random.nextInt(list.size()));
			for(int i=0;i<4;i++) {
				x=point.x+w[i][0];
				y=point.y+w[i][1];
				if(x<0||y<0||x==high||y==width) continue; 
				if(flag[x][y]) {
					flag[x][y]=false;
					ans[x][y].type=0;
					ans[point.x][point.y].type=0;
					for(int j=0;j<4;j++) {
						_x=x+w[j][0];
						_y=y+w[j][1];
						if(ans[_x][_y].type==1) list.add(new Point(_x,_y));
					}
					break;
				}
			}
		}
		
		int n=random.nextInt(20);
		int label=1;
		while(n>0) {
			n--;
			x=random.nextInt(high);
			y=random.nextInt(width);
			int type=random.nextInt(2)+2;
			int time1,time2;
			if(type==2) {
				time1=random.nextInt(10)+5;
				time2=random.nextInt(10)+1;
			}
			else{
				time1=random.nextInt(10)+5;
				time2=random.nextInt(10)+5;
			}
			int i=random.nextInt(4);
			while(x>=0&&y>=0&&x<high&&y<width&&ans[x][y].type!=0) {
				x+=w[i][0];
				y+=w[i][1];
			}
			while(x>=0&&y>=0&&x<high&&y<width&&ans[x][y].type==0) {
				if(ans[x][y].isLead) break;
				ans[x][y].type=type;
				ans[x][y].label=label;
				ans[x][y].time1=time1;
				ans[x][y].time2=time2;
				x+=w[i][0];
				y+=w[i][1];
			}
			label++;
		}
		
		do {
			x=random.nextInt(high);
			y=random.nextInt(width);
		}while(ans[x][y].type!=0||ans[x][y].isLead);
		ans[x][y].type=4;
		
		return ans;
	}
}
