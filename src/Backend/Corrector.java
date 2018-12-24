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
	private String[] wordlist = new String[10];
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
	private void searchCollection(String pre, double val) {
		String x = "1";
		Map<String, Double> current;
		if (!collection1.isEmpty()) {
			current = collection1;
		} else if (!collection2.isEmpty()) {
			current = collection2;
		} else {
			current = null;
		}
		if (current != null) {
			for (Map.Entry<String, Double> entry : current.entrySet()) {
				x = entry.getKey();
				if (!pre.equalsIgnoreCase("")) {
					if (temp2.get(pre + " " + x) != null) {
						result.put((entry.getValue() / temp.get("map1")) * (temp2.get(pre + " " + x) / temp2.get("map2")) * val, x);
					} else {
						result.put((entry.getValue() / temp.get("map1")) * (0.1 / temp2.get("map2")) * val, x);
					}
				} else {
					result.put((entry.getValue() / temp.get("map1")) * val, x);
				}
			}
		}
	}

	private String getBigValue(Map<String, Double> arg){
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
		return x;
	}

	public String[] setList(Map<String, Double> arg, mainWindow mwin){
		for (int d=1; d<10; d++) {
			wordlist[d] = "";
		}
		if (mwin.inputWord.length() < 1) {
			wordlist[0] = "";
			return wordlist;
		}
		String xc = mwin.prefixWord.toLowerCase();
		int i = 0;
		if (!arg.isEmpty()) {
			for (Map.Entry<String, Double> entry : arg.entrySet()) {
				String word = mwin.inputWord.toLowerCase() + entry.getKey().toLowerCase();
				buildOc(word, collection1);
				buildTc();
				searchCollection(xc, entry.getValue());
				collection1.clear();
				collection2.clear();
			}
			wordlist[0] = mwin.inputWord + getBigValue(arg);
		} else {
			String word = mwin.inputWord.toLowerCase();
			buildOc(word, collection1);
			buildTc();
			searchCollection(xc, 1);
			collection1.clear();
			collection2.clear();
			wordlist[0] = mwin.inputWord;
		}
		if (!result.isEmpty()) {
			Set<Double> keys = result.keySet();
			List<Double> lists = new ArrayList<Double>(keys);
			Collections.sort(lists);
			int io = 1;
			int g = 0;
			for (int kk = lists.size() - 1; kk >= 0; kk--) {
				if (io == 10) {
					break;
				}
				if (result.get(lists.get(kk)) != null) {
					for (int d = 0; d <= 9; d++) {
						if (wordlist[d].equalsIgnoreCase(result.get(lists.get(kk)))) {
							g = 1;
						}
					}
					if (g == 0) {
						wordlist[io] = result.get(lists.get(kk));
						io++;
					} else {
						g = 0;
					}
				}
				//System.out.println(result.get(lists.get(kk)));
			}
			result.clear();
		}
		return wordlist;
	}

	// 程序入口
	public void dealWith(Map<String, Double> arg, mainWindow mwin) {
		String x = getBigValue(arg);
		if (mwin.inputWord.length() < 1) {
			for (int ic=0; ic <10 ;ic++) {
				wordlist[ic] = "";
			}
			//System.out.println(x + " NULL");
			mwin.pushKey(x, wordlist);
			return;
		}
		String[] aka = setList(arg, mwin);
		mwin.pushKey(x, aka);
	}
}
