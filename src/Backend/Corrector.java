package Backend;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Corrector {
	private static Map<String, Integer> temp = new HashMap<String, Integer>();
	private static Map<String, Integer> temp2 = new HashMap<String, Integer>();
	private static Map<String, Integer> collection1 = new HashMap<String, Integer>();
	private static Map<String, Integer> collection2 = new HashMap<String, Integer>();
	public Corrector() {
		
	};
	// 构建编辑距离为1 的集合
	private static void buildOc(String word, Map<String, Integer> collection) {
		int i;
		// 删除
		for (i = 0; i < word.length(); i ++) {
			String x = word.substring(0, i) + word.substring(i+1, word.length());
			if (temp.get(x.toLowerCase()) != null) {
				collection.put(x, temp.get(x.toLowerCase()));
			}
			//System.out.println(x);
		}
		// 增加
		for (i = 0; i <= word.length(); i ++) {
			for (int j=0; j<26;j++) {
				String x = word.substring(0, i) + (char)(97+j) + word.substring(i, word.length());
				if (temp.get(x.toLowerCase()) != null) {
					collection.put(x, temp.get(x.toLowerCase()));
				}
				//System.out.println(x);
			}
		}
		// 相邻替换
		for (i = 0; i < word.length()-1; i ++) {
			String x = word.substring(0, i) + word.charAt(i + 1) + word.charAt(i) + word.substring(i+2, word.length());
			if (temp.get(x.toLowerCase()) != null) {
				collection.put(x, temp.get(x.toLowerCase()));
			}
			//System.out.println(x);
		}
		// 字符替换
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

	// 构建编辑距离为2 的集合
	private static void buildTc() {
		for (Map.Entry<String, Integer> entry : collection1.entrySet()) {
			buildOc(entry.getKey(), collection2);
			//System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
	}

	// 查找最短编辑距离集合 利用bigram模型计算概率得出概率最大的返回。
	private static String searchCollection(String pre) {
		String x = "1";
		for (Map.Entry<String, Integer> entry : collection1.entrySet()) {
			if (collection1.get(x) == null) {
				x = entry.getKey();
			} else {
				if (temp2.get(pre + " " + entry.getKey()) != null && temp2.get(pre + " " + x) != null) {
					if (temp2.get(pre + " " + entry.getKey()) > temp2.get(pre + " " + x)) {
						x = entry.getKey();
					}
				} else {
					if (collection1.get(x) < entry.getValue()) {
						x = entry.getKey();
					}
				}
			}
		}
		/*for (Map.Entry<String, Integer> entry : collection2.entrySet()) {
			if (collection2.get(x) < entry.getValue()) {
				x = entry.getKey();
			}
		}*/
		return x;
	}

	// 程序入口
	public static void dealWith() {
		Scanner in = new Scanner(System.in);
		try {
			FileInputStream f=new FileInputStream("dst.txt");
	        ObjectInputStream ob=new ObjectInputStream(f);
	        temp = (Map<String, Integer>) ob.readObject();
	        //System.out.println(temp.get("you"));
	        ob.close();
		} catch (Exception e) {
			System.out.println("Corrector Error!");
			e.printStackTrace();
		}
		try {
			FileInputStream f2=new FileInputStream("dst2.txt");
			ObjectInputStream ob2=new ObjectInputStream(f2);
			temp2 = (Map<String, Integer>) ob2.readObject();
			//System.out.println(temp.get("you"));
			ob2.close();
		} catch (Exception e) {
			System.out.println("Corrector Error!");
			e.printStackTrace();
		}
		String xc = "";
		while (in.hasNext()) {
			String word = in.next("[A-Za-z]+").replace("\n","");
			buildOc(word, collection1);
			buildTc();
			String x = searchCollection(xc);
			if (temp.get(word) != null) {
				System.out.print(word + " ");
			} else {
				System.out.print(x + " ");
			}
			xc = word;
			collection1.clear();
			collection2.clear();
		}
		in.close();
	}
}
