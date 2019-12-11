package entry;

public class Cell {
	public boolean isLead;//true��ʾ�������� |false��ʾ����
	public int type;// 0:��ͨ��·
					 // 1:��ͨ����
	 				 // 2:����ʽ����
	 				 // 3:�ɿ�ʽ����
	 				 // 4:�Թ�����
	 				 // 5:������
	public int time;//��ʾʱ��
	public int time1;//����ʱ��
	public int time2;//��ȴʱ��
	public int status;//0 ��ͨ״̬|1 ����״̬|2 ��ȴ״̬
	public int label;//�����ǩ
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
