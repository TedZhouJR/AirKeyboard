import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

public class Corrector {
	public static Map<String, Integer> temp = new HashMap<String, Integer>();
	public static void main(String[] args) {
		try {
			FileInputStream f=new FileInputStream("dst.txt");
	        ObjectInputStream ob=new ObjectInputStream(f);
	        temp = (Map<String, Integer>) ob.readObject();
	        System.out.println(temp.get("you"));
	        ob.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
