package voIPStats;

import java.io.Console;
import java.util.Comparator;


public class TestClass {
	public static void main(String[] args) {
		CustomSkipList<String,String> test = new CustomSkipList<>(new StringComparer(3));
		test.put("111","0");
		test.put("112","1");
		test.put("114","2");
		test.put("113","3");
		test.put("124","4");
		test.put("121","5");
		test.put("123","6");
		//System.out.println(test.SelectBySpecificComparator(2, "111").toString());
		System.out.println(test.toString());
		//System.out.println("1".compareTo("2"));
	}
	
}

