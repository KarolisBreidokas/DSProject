package voIPStats;

import java.util.Comparator;

public interface MultiLevelComparator<T> extends Comparator<T>{
	int comparasonCount();
	int compareBykey(int index,T obj1,T obj2);
	@Override
	default int compare(T o1, T o2) {
		for(int a=0;a<comparasonCount();a++) {
			int i;
			if((i=compareBykey(a, o1, o2))!=0) {
				return i;
			}
		}
		return 0;
	}
}
