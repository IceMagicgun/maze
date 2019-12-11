package other;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.UUID;

public class Utils {
	
	//byte 数组与 int 的相互转换
	public static int byteArrayToInt(byte[] b) {
	    return   b[3] & 0xFF |
	            (b[2] & 0xFF) << 8 |
	            (b[1] & 0xFF) << 16 |
	            (b[0] & 0xFF) << 24;
	}
 
	public static byte[] intToByteArray(int a) {
	    return new byte[] {
	        (byte) ((a >> 24) & 0xFF),
	        (byte) ((a >> 16) & 0xFF),   
	        (byte) ((a >> 8) & 0xFF),   
	        (byte) (a & 0xFF)
	    };
	}
	
	//合并byte数组
	public static  byte[] byteMerger(byte[] byte_1, byte[] byte_2){  
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];  
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);  
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);  
        return byte_3;  
    }
	
	//获取MD5
	public static String getMD5(String s) {
	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        md.update(mGetBytes(s, "UTF-8"));
	        return fillMD5(new BigInteger(1, md.digest()).toString(16));
	    } catch (Exception e) {
	        System.out.println(String.format("对字符串%s进行MD5加密出现错误",s));
	    }
	    return null;
	}
	private static String fillMD5(String md5){
        return md5.length()==32?md5:fillMD5("0"+md5);
    }
	
	//获取唯一标识符
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-","");
	}
	
	//加密解密
	public static String encrypt(String s,String key) {
		if(s==null) return null;
		s=mNewString(mGetBytes(s,"UTF-8"),"ISO8859-1");
		int s_l=s.length(),key_l=key.length(),k;
		byte[] s_byte=mGetBytes(s,"ISO8859-1"),key_byte=key.getBytes(),ans_byte=new byte[s_l];
		for(int i=0;i<s_l;i++) {
			k=(int)s_byte[i]+change_sadd(key_byte[(i+i%3)%key_l]);
			ans_byte[i]=(byte)(k>=128?k-256:k);
		}
		String ans=mNewString(ans_byte,"ISO8859-1");
		return sadd(getMD5(ans),key)+ans;
	}
	public static String decrypt(String s,String key) {
		if(s==null||s.length()<32) return null;
		String vcode=s.substring(0,32),ctext=s.substring(32);
		if(!vcode.equals(sadd(getMD5(ctext),key))) return null;
		int ctext_l=ctext.length(),key_l=key.length(),k;
		byte[] ctext_byte=mGetBytes(ctext,"ISO8859-1"),key_byte=key.getBytes(),ans_byte=new byte[ctext_l];
		for(int i=0;i<ctext_l;i++) {
			k=(int)ctext_byte[i]-change_sadd(key_byte[(i+i%3)%key_l]);
			ans_byte[i]=(byte)(k<-128?k+256:k);
		}
		String ans=null;
		ans=mNewString(ans_byte,"UTF-8");
		return ans;
	}
	private static String sadd(String a,String b) {
		byte[] x=a.getBytes(),y=b.getBytes(),ans=new byte[32];
		for(int i=0;i<32;i++) {
			ans[i]=change_sadd((change_sadd(x[i])+change_sadd(y[(i+3)%32]))%(change_sadd(x[(i+2)%32])+1));
		}
		return new String(ans);
	}
	private static byte change_sadd(int x) {
		if(x<10) return (byte)(('0'+x)&0xFF);
		else return (byte)(('a'+x-10)&0xFF);
	}
	private static int change_sadd(byte x) {
		if(x>='0'&&x<='9') return x-'0';
		else return x-'a'+10;
	}
	public static byte[] mGetBytes(String s,String c) {
		byte[] ans=null;
		try {
			ans=s.getBytes(c);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ans;
	}
	public static String mNewString(byte[] bytes,String c) {
		String ans=null;
		try {
			ans=new String(bytes,c);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ans;
	}
	
}
