package Backend;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Prepare {
	public static Map<String, Integer> map=new HashMap<String, Integer>();
	public static Map<String, Integer> map2=new HashMap<String, Integer>();
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
								map2.put(t + " "+ x, 1);
							} else {
								map2.put(t + " "+ x, map2.get(t + " "+ x)+1);
							}
							//System.out.println("t = " + t + " " + "m = " + x);
						}
					}
					i++;
	    			if (map.get(x) == null) {
	    				t = x;
	    				map.put(x, 1);
	    			} else {
						t = x;
	    				map.put(x, map.get(x)+1);
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
		dealWith("src2.txt");
		dealWith("src1.txt");
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
