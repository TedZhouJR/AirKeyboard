import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;

public class Prepare {
	public static Map<String, Integer> map=new HashMap<String, Integer>();   
	public static void dealwith(String filename) {
	    try {
	    	BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename)), "UTF-8"));
	    	String linetxt = null;
	    	String pattern = "[A-Za-z]+";
	    	Pattern r = Pattern.compile(pattern);
	    	while ((linetxt = in.readLine()) != null) {
	    		Matcher m = r.matcher(linetxt);
	    		while(m.find()) {
	    			if (map.get(m.group().toLowerCase()) == null) {
	    				map.put(m.group().toLowerCase(), 1);
	    			} else {
	    				map.put(m.group().toLowerCase(), map.get(m.group().toLowerCase())+1);
	    			}
	    		}
            }
	    	in.close();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	public static void main(String[] args) {
		dealwith("src2.txt");
		dealwith("src1.txt");
		try {
			FileOutputStream fos=new FileOutputStream("MyTest.txt");
	        ObjectOutputStream objectOutputStream=new ObjectOutputStream(fos);
	        objectOutputStream.writeObject(map);
	        objectOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
