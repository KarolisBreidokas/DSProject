package voIPStats;

import java.util.Comparator;


public class StringComparer implements MultiLevelComparator<String> {

	int comparacount;
	
	public StringComparer(int checkCount) {
		comparacount=checkCount;
	}
	@Override
	public int comparasonCount() {
		return comparacount;
	}
	@Override
	public int compare(String o1, String o2) {
		int l=Math.min(o1.length(),o2.length());
		return o1.substring(0, l).compareTo(o2.substring(0, l));
	}

	@Override
	public int compareBykey(int index, String obj1, String obj2) {
		if(obj1.length()<=index||obj2.length()<=index) {
			return obj1.length()-obj2.length();
		}
		return obj2.charAt(index)-obj1.charAt(index);
	}
	
}