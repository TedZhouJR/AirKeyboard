package Backend;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Prepare {
	public static Map<String, Double> map=new HashMap<String, Double>();
	public static Map<String, Double> map2=new HashMap<String, Double>();
	// 得到单词词频
	public static void dealWith(String filename) {
	    try {
	    	BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename)), "UTF-8"));
	    	String linetxt = null;
	    	String pattern = "[A-Za-z]+";
	    	Pattern r = Pattern.compile(pattern);
	    	int i = 0;
	    	while ((linetxt = in.readLine()) != null) {
	    		Matcher m = r.matcher(linetxt);
	    		i = 0;
	    		String t = "";
	    		while(m.find()) {
					String x = m.group().toLowerCase();
					if (i != 0){
						if (t != "" && x != "") {
							if (map2.get(t + " "+ x) == null) {
								map2.put(t + " "+ x, 1.0);
								map2.put("map2", map2.get("map2")+1.0);
							} else {
								map2.put(t + " "+ x, map2.get(t + " "+ x)+1.0);
								map2.put("map2", map2.get("map2")+1.0);
							}
							//System.out.println("t = " + t + " " + "m = " + x);
						}
					}
					i++;
	    			if (map.get(x) == null) {
	    				t = x;
	    				map.put(x, 1.0);
						map.put("map1", map.get("map1")+1.0);
	    			} else {
						t = x;
	    				map.put(x, map.get(x)+1);
						map.put("map1", map.get("map1")+1.0);
	    			}
	    		}
            }
	    	in.close();
	    } catch (Exception e) {
	    	System.out.println("Dealwith Error!");
	    	e.printStackTrace();
	    }
	}

	public static void main(String[] args) {
		map.put("map1",0.0);
		map2.put("map2",0.0);
		dealWith("src1.txt");
		dealWith("src2.txt");
		dealWith("src3.txt");
		dealWith("src4.txt");
		dealWith("src5.txt");
		dealWith("src6.txt");
		dealWith("src7.txt");
		dealWith("src8.txt");
		dealWith("src9.txt");
		dealWith("src10.txt");
		try {
			FileOutputStream fos=new FileOutputStream("dst.txt");
	        ObjectOutputStream objectOutputStream=new ObjectOutputStream(fos);
	        objectOutputStream.writeObject(map);
	        objectOutputStream.close();
		} catch (Exception e) {
			System.out.println("Prepare Error!");
			e.printStackTrace();
		}
		try {
			FileOutputStream fos2=new FileOutputStream("dst2.txt");
			ObjectOutputStream objectOutputStream2=new ObjectOutputStream(fos2);
			objectOutputStream2.writeObject(map2);
			objectOutputStream2.close();
		} catch (Exception e) {
			System.out.println("Prepare Error!");
			e.printStackTrace();
		}
	}
}
