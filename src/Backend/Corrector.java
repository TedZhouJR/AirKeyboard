package Backend;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.*;
import Frontend.mainWindow;

public class Corrector {
	private Map<String, Double> temp = new HashMap<String, Double>();
	private Map<String, Double> temp2 = new HashMap<String, Double>();
	private Map<String, Double> collection1 = new HashMap<String, Double>();
	private Map<String, Double> collection2 = new HashMap<String, Double>();
	private Map<Double, String> result = new HashMap<Double, String>();
	public Corrector() {
		try {
			FileInputStream f=new FileInputStream("dst.txt");
			ObjectInputStream ob=new ObjectInputStream(f);
			temp = (Map<String, Double>) ob.readObject();
			//System.out.println(temp.get("you"));
			ob.close();
		} catch (Exception e) {
			System.out.println("Corrector Error!");
			e.printStackTrace();
		}
		try {
			FileInputStream f2=new FileInputStream("dst2.txt");
			ObjectInputStream ob2=new ObjectInputStream(f2);
			temp2 = (Map<String, Double>) ob2.readObject();
			//System.out.println(temp.get("you"));
			ob2.close();
		} catch (Exception e) {
			System.out.println("Corrector Error!");
			e.printStackTrace();
		}
	};
	// 构建编辑距离为1 的集合
	private void buildOc(String word, Map<String, Double> collection) {
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
	private void buildTc() {
		for (Map.Entry<String, Double> entry : collection1.entrySet()) {
			buildOc(entry.getKey(), collection2);
			//System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
	}

	// 查找最短编辑距离集合 利用bigram模型计算概率得出概率最大的返回。
	private void searchCollection(String pre) {
		String x = "1";
		Map<String, Double> current;
		if (!collection1.isEmpty()) {
			current = collection1;
		} else if (!collection2.isEmpty()) {
			current = collection2;
		} else {
			current = null;
		}
		for (Map.Entry<String, Double> entry : current.entrySet()) {
			x = entry.getKey();
			if (!pre.equalsIgnoreCase("")) {
				if (temp2.get(pre + " " + x) != null) {
					result.put((entry.getValue() / temp.get("map1")) * (temp2.get(pre + " " + x) / temp2.get("map2")), x);
				} else {
					result.put((entry.getValue() / temp.get("map1")) * (0.1 / temp2.get("map2")), x);
				}
			} else {
				result.put(entry.getValue() / temp.get("map1"), x);
			}
		}
	}

	// 程序入口
	public void dealWith(Map<String, Double> arg, mainWindow mwin) {
		if (mwin.inputWord.length() < 1) {
			String x = "";
			double v = 0;
			for (Map.Entry<String, Double> entry : arg.entrySet()) {
				if (x.equalsIgnoreCase("")) {
					x = entry.getKey();
					v = entry.getValue();
				} else {
					if (v < entry.getValue()) {
						x = entry.getKey();
						v = entry.getValue();
					}
				}
			}
			String[] a = new String[1];
			a[0] = "";
			//System.out.println(x + " NULL");
			mwin.pushKey(x, a);
			return;
		}
		String xc = mwin.prefixWord.toLowerCase();
		int i = 0;
		for (Map.Entry<String, Double> entry : arg.entrySet()) {
			String word = mwin.inputWord.toLowerCase()+entry.getKey().toLowerCase();
			buildOc(word, collection1);
			buildTc();
			searchCollection(xc);
			collection1.clear();
			collection2.clear();
		}
		Set <Double> keys= result.keySet();
		List<Double> lists = new ArrayList<Double>(keys);
		Collections.sort(lists);
		int io = 0;
		String[] res = new String[lists.size()];
		for (int kk=lists.size()-1;kk>=0; kk--) {
			if (io == 9) {
				break;
			}
			res[io] = result.get(lists.get(kk));
			System.out.println(result.get(lists.get(kk)));
			io++;
		}
		result.clear();
		//System.out.println(res[0].substring(res[0].length()-1) + " "+res);
		mwin.pushKey(res[0].substring(res[0].length()-1), res);
	}
}
