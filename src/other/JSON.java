package other;

import java.util.HashMap;
import java.util.Map;

import entry.Cell;

public class JSON {
	
	public static Cell[][] stringTOCell(String s){
		Cell[][] ans;
		String[] ss=s.split(",");
		int x=Integer.parseInt(ss[0]),y=Integer.parseInt(ss[1]),k=2;
		ans=new Cell[x][y];
		for(int i=0;i<x;i++) {
			for(int j=0;j<y;j++) {
				ans[i][j]=new Cell();
				ans[i][j].isLead=Boolean.parseBoolean(ss[k++]);
				ans[i][j].type=Integer.parseInt(ss[k++]);
				ans[i][j].time=Integer.parseInt(ss[k++]);
				ans[i][j].status=Integer.parseInt(ss[k++]);
				ans[i][j].label=Integer.parseInt(ss[k++]);
			}
		}
		return ans;
	}	
	
	public static String cellToString(Cell[][] cell){
		String ans="";
		ans+=cell.length+","+cell[0].length+",";
		for(int i=0;i<cell.length;i++) {
			for(int j=0;j<cell[0].length;j++) {
				ans+=String.valueOf(cell[i][j].isLead)+",";
				ans+=String.valueOf(cell[i][j].type)+",";
				ans+=String.valueOf(cell[i][j].time)+",";
				ans+=String.valueOf(cell[i][j].status)+",";
				ans+=String.valueOf(cell[i][j].label)+",";
			}
		}
		return ans;
	}
	
	public static Map<String,String> stringTOMap(String s) {
		if(s==null) return null;
		Map<String,String> ans=new HashMap<>();
		char[] c=s.toCharArray();
		String key="",value="";
		int flag;
		flag=0;
		for(int i=0;i<c.length;i++) {
			if(c[i]=='[') {
				if(flag==0) {
					key="";
					value="";
					flag=1;
				}
				if(flag==1) flag=2;
				if(flag==3) flag=4;
				continue;
			}
			else if(c[i]==']') {
				if(flag==2) flag=3;
				if(flag==4) flag=5;
				if(flag==5) {
					flag=0;
					ans.put(key, value);
				}
				continue;
			}
			else if(c[i]=='\\') i++;
			if(flag==2) key+=c[i];
			if(flag==4) value+=c[i];
		}
		
		return ans;
	}
	
	public static String mapToString(Map<String,String> map) {
		String ans="";
		for (Map.Entry<String,String> entry : map.entrySet()) {
			ans+="[["+forMapToString(entry.getKey())+"]["+forMapToString(entry.getValue())+"]]";
		}
		return ans;
	}
	
	private static String forMapToString(String s) {
		if(s==null) return "";
		String ans="";
		char[] c=s.toCharArray();
		for(int i=0;i<c.length;i++) {
			if(c[i]=='\\'||c[i]=='['||c[i]==']') ans+='\\';
			ans+=c[i];
		}
		return ans;
	}
}
