package entry;

public class Cell {
	public boolean isLead;//true表示人物在这 |false表示不在
	public int type;// 0:普通道路
					 // 1:普通陷阱
	 				 // 2:触发式陷阱
	 				 // 3:可控式陷阱
	 				 // 4:迷宫出口
	 				 // 5:高亮点
	public int time;//显示时间
	public int time1;//触发时间
	public int time2;//冷却时间
	public int status;//0 普通状态|1 触发状态|2 冷却状态
	public int label;//区域标签
	public int timePoint;
	
	public Cell() {
		isLead=false;
		time=time1=time2=status=0;
		timePoint=-999;
	}
	
	public Cell clone() {
		Cell ans=new Cell();
		ans.isLead=isLead;
		ans.type=type;
		ans.time=time;
		ans.status=status;
		ans.label=label;
		return ans;
	}
}
