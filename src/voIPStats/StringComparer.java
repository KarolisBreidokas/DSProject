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
		return o1.compareTo(o2);
	}

	@Override
	public int compareBykey(int index, String obj1, String obj2) {
		return obj2.charAt(index)-obj1.charAt(index);
	}
	
}