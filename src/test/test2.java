package test;

public class test2 {
	private static int i=0;
	
	public static void aaa() {
		while(true) {
			if(i>20) break;
			System.out.println("A");
			i++;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void bbb() {
		while(true) {
			if(i>20) break;
			System.out.println("B");
			i++;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
