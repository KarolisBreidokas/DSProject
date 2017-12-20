package voIPStats;

import java.util.ArrayList;
import java.util.Iterator;

public class CustomSkipList<K, V> implements Iterable<V> {

	MultiLevelComparator<K> c;

	node<K, V> start;

	int size = 0;

	private int levelCount;

	public boolean isEmpty() {
		return size == 0;
	}
	public int getSize() {
		return size;
	}
	public CustomSkipList(MultiLevelComparator<K> cmpr) {
		c = cmpr;
		levelCount = c.comparasonCount();
	}

	public void put(K key, V value) {
		if (isEmpty()) {
			start = new node<K, V>(key, value, c.comparasonCount());
		} else {
			putRecursive(key, value, start, 0);
		}
		size++;
	}

	private void putRecursive(K key, V value, node<K, V> start, int Level) {
		if (c.compareBykey(Level, start.key, key) > 0) {
			node<K, V> t = new node<K, V>(start.key, start.value, levelCount - Level);
			for (int a = 0; a < t.next.length; a++) {
				t.next[a] = start.next[a];
			}
			start.updateNext(t, t.next.length);
			start.key = key;
			start.value = value;
			return;
		}
		node<K, V> n = start;
		for (; n != (Level == 0 ? null : start.next[levelCount - Level]); n = n.next[levelCount - Level - 1]) {
			if (c.compareBykey(Level, n.key, key) == 0) {
				putRecursive(key, value, n, Level + 1);
				return;
			}
			if (n.next[levelCount - Level - 1] == (Level == 0 ? null : start.next[levelCount - Level])
					|| (c.compareBykey(Level, n.key, key) < 0
							&& c.compareBykey(Level, n.next[levelCount - Level - 1].key, key) > 0)) {
				node<K, V> t = new node<K, V>(key, value, levelCount - Level);
				t.updateNext(n.next[levelCount - Level - 1], t.next.length);
				updateVals(n, t, Level);
				return;
			}
		}
		return;
	}

	private void updateVals(node<K, V> from, node<K, V> inserted, int level) {
		int l = level;
		node<K, V> n = from;
		while (l < levelCount) {
			if (n.next[levelCount - l - 1] == inserted.next[levelCount - l - 1]) {
				n.next[levelCount - l - 1] = inserted;
				l++;
			} else {
				n = n.next[levelCount - l - 1];
			}
		}
	}

	public CustomSkipList<K, V> SelectBySpecificComparator(int ComparatorIndex, K selectKey) {
		
		return SelectBySpecificComparator(ComparatorIndex,selectKey,c);
	}

	public CustomSkipList<K, V> SelectBySpecificComparator(int ComparatorIndex, K selectKey,
			MultiLevelComparator<K> newComparator) {
		CustomSkipList<K, V> ans = new CustomSkipList<K, V>(newComparator);
		for (node<K, V> a = this.start; a != null; a = a.next[levelCount - ComparatorIndex - 1]) {
			if (c.compareBykey(ComparatorIndex, selectKey, a.key) == 0) {
				for (node<K, V> b = a; b != a.next[levelCount - ComparatorIndex - 1]; b = b.next[0]) {
					ans.put(b.key, b.value);
				}
			}
		}
		return ans;
	}

	public V get(K key) {
		int level = 0;
		node<K, V> toCheck = null;
		node<K, V> n = start;
		for (; n != toCheck && levelCount > level;) {
			int cmp = c.compareBykey(level, n.key, key);
			if (cmp == 0) {
				level++;
				toCheck = n.next[levelCount - level];
			}
			if (cmp < 0) {
				n = n.next[levelCount - level - 1];
			}
			if (cmp > 0) {
				return n.value;
			}
		}
		return n.value;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (node<K, V> n = start; n != null; n = n.next[0]) {
			builder.append(n.toString() + "\r\n");
		}
		return builder.toString();
	}

	@Override
	public Iterator<V> iterator() {
		return new iterator();
	}
	public ArrayList<SkipListSubList> GroupByPrimaryComparer() {
		ArrayList<SkipListSubList> t=new ArrayList<SkipListSubList>();
		int n=0;
		for(node<K, V> d=start;d!=null;d=d.next[d.next.length-1]) {
			t.add(new SkipListSubList(d));
		}
		t.trimToSize();
		return t;
	}

	public class SkipListSubList implements Iterable<V>{

		private node<K,V> root;
		
		public V GetRootValue() {
			return root.value;
		}
		
		@Override
		public Iterator<V> iterator() {
			return new iterator(root, root.next[root.next.length-1]);
		}
		private SkipListSubList(node<K,V> root) {
			this.root=root;
		}
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			for (node<K, V> n = root; n != root.next[root.next.length-1]; n = n.next[0]) {
				builder.append(n.toString() + "\r\n");
			}
			return builder.toString();
		}
	}
	
	protected class node<K, V> {
		K key;
		V value;
		node<K, V>[] next;

		public node(K key, V value, int levelCount) {
			this.key = key;
			this.value = value;
			next = new node[levelCount];
		}

		public void updateNext(node<K, V> next, int to) {
			for (int a = 0; a < to; a++) {
				this.next[a] = next;
			}
		}

		@Override
		public String toString() {
			return key + " => " + value + "(" + next.length + ")";
		}
	}

	private class iterator implements Iterator<V> {

		node<K, V> D;
		node<K,V> end;

		public iterator() {
			D = start;
		}
		public iterator(node<K,V> start,node<K,V> end) {
			this.D=start;
			this.end=end;
		}

		@Override
		public boolean hasNext() {
			return D != end;
		}

		@Override
		public V next() {
			V val = D.value;
			D = D.next[0];
			return val;
		}
	}
}
