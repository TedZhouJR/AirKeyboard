package Backend;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Corrector {
	public static Map<String, Integer> temp = new HashMap<String, Integer>();
	public static Map<String, Integer> collection1 = new HashMap<String, Integer>();
	public static Map<String, Integer> collection2 = new HashMap<String, Integer>();
	// �������뵥�ʵ���̱༭���뼯��
	public Corrector() {
		
	};
	public static void buildOc(String word, Map<String, Integer> collection) {
		// �༭����Ϊ1�ļ���
		int i;
		// �ַ�ȱʧ
		for (i = 0; i < word.length(); i ++) {
			String x = word.substring(0, i) + word.substring(i+1, word.length());
			if (temp.get(x.toLowerCase()) != null) {
				collection.put(x, temp.get(x.toLowerCase()));
			}
			//System.out.println(x);
		}
		// �ַ�����
		for (i = 0; i <= word.length(); i ++) {
			for (int j=0; j<26;j++) {
				String x = word.substring(0, i) + (char)(97+j) + word.substring(i, word.length());
				if (temp.get(x.toLowerCase()) != null) {
					collection.put(x, temp.get(x.toLowerCase()));
				}
				//System.out.println(x);
			}
		}
		// ���ڻ�λ
		for (i = 0; i < word.length()-1; i ++) {
			String x = word.substring(0, i) + word.charAt(i + 1) + word.charAt(i) + word.substring(i+2, word.length());
			if (temp.get(x.toLowerCase()) != null) {
				collection.put(x, temp.get(x.toLowerCase()));
			}
			//System.out.println(x);
		}
		// �ַ��滻
		for (i = 0; i < word.length(); i ++) {
			for (int j=0; j<26;j++) {
				String x = word.substring(0, i) + (char)(97+j) + word.substring(i+1, word.length());
				if (temp.get(x.toLowerCase()) != null) {
					collection.put(x, temp.get(x.toLowerCase()));
				}
				//System.out.println(x);
			}
		}//*/
	}
	public static void buildTc() {
		// �༭����Ϊ2�ļ���
		for (Map.Entry<String, Integer> entry : collection1.entrySet()) {
			buildOc(entry.getKey(), collection2);
			//System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
		}
	}
	// ���ҳ���Ƶ����ߵ���̱༭���뵥�� ����1���ϣ�����2���ϣ���������ܵĵ���
	public static String searchCollection() {
		String x = "1";
		for (Map.Entry<String, Integer> entry : collection1.entrySet()) {
			if (collection1.get(x) == null) {
				x = entry.getKey();
			} else {
				if (collection1.get(x) < entry.getValue()) {
					x = entry.getKey();
				}
			}
		}
		for (Map.Entry<String, Integer> entry : collection2.entrySet()) {
			if (collection2.get(x) < entry.getValue()) {
				x = entry.getKey();
			}
		}
		return x;
	}
	// ���ϲ�10�����ܵĵ��ʽ��ж�Ԫģ��Ԥ�⣬��������ܵ�ֵ��
	public static void bigramPre() {
		
	}
	// �������
	public static void dealWith(String[] args) {
		Scanner in = new Scanner(System.in);
		try {
			FileInputStream f=new FileInputStream("dst.txt");
	        ObjectInputStream ob=new ObjectInputStream(f);
	        temp = (Map<String, Integer>) ob.readObject();
	        ob.close();
		} catch (Exception e) {
			System.out.println("Corrector Error!");
			e.printStackTrace();
		}
		String word = in.nextLine();
		buildOc(word, collection1);
		buildTc();
		String x = searchCollection();
		if (temp.get(word) != null) {
			System.out.println(word);
		} else {
			System.out.println(x);
		}
		in.close();
	}
}
