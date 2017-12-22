package voIPStats;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import voIPStats.CDRLog.KeyComparare;
import voIPStats.CDRLog.LogKey;

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

		return SelectBySpecificComparator(ComparatorIndex, selectKey, c);
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
		node<K, V> toCheck = null;
		for (node<K, V> n = start; n != toCheck;n=n.next[levelCount-1]) {
			int cmp = c.compareBykey(0, n.key, key);
			if (cmp == 0) {
				if(0<levelCount-1) {
					V ans=getRecursvie(key, n, 1);
					if(ans==null) {
						if(c.compare(key, start.key)==0)
							return start.value;
						return null;
					}else {
						return ans;
					}
				}else {
					if(c.compare(key, n.key)==0) {
						return n.value;
					}else {
						return null;
					}
					
				}
			}
			if (cmp > 0) {
				return null;
			}
		}
		return null;
	}
	
	private V getRecursvie(K key,node<K, V> start,int level) {
		for (node<K, V> n=start; n != start.next[levelCount-level];n=n.next[levelCount-level-1]) {
			int cmp = c.compareBykey(level, n.key, key);
			if (cmp == 0) {
				if(level<levelCount-1) {
					V ans=getRecursvie(key, n, level+1);
					if(ans==null) {
						if(c.compare(key, start.key)==0)
							return start.value;
						return null;
					}else {
						return ans;
					}
				}else {
					if(c.compare(key, n.key)==0) {
						return n.value;
					}else {
						return null;
					}
					
				}
			}
			if (cmp > 0) {
				if(c.compare(key, start.key)==0)
					return start.value;
				return null;
			}
		}
		if(c.compare(key, start.key)==0)
			return start.value;
		return null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (node<K, V> n = start; n != null; n = n.next[0]) {
			builder.append(n.toString() + "\r\n");
		}
		return builder.toString();
	}

	public SkipListSubList toSublist() {
		return new SkipListSubList(start, null);
	}

	public SkipListSubList getSublist(K key) {
		node<K, V> toCheck = null;
		node<K, V> n = start;
		for (; n != toCheck; n = n.next[levelCount - 1]) {
			if (c.compareBykey(0, n.key, key) == 0) {
				return new SkipListSubList(n);
			}
		}
		return null;
	}

	@Override
	public Iterator<V> iterator() {
		return new iterator();
	}

	public ArrayList<SkipListSubList> GroupByPrimaryComparer() {
		ArrayList<SkipListSubList> t = new ArrayList<SkipListSubList>();
		int n = 0;
		for (node<K, V> d = start; d != null; d = d.next[d.next.length - 1]) {
			t.add(new SkipListSubList(d));
		}
		t.trimToSize();
		return t;
	}

	public class SkipListSubList implements Iterable<V> {

		private node<K, V> root;
		private node<K, V> end;
		boolean full;

		public V GetRootValue() {
			return root.value;
		}

		@Override
		public Iterator<V> iterator() {
			return new iterator(root, end);
		}

		private SkipListSubList(node<K, V> root) {
			this.root = root;
			this.end = root.next[root.next.length - 1];
			full = false;
		}

		private SkipListSubList(node<K, V> root, node<K, V> end) {
			this.root = root;
			this.end = end;
			full = true;
		}

		public CustomSkipList<K, V> toSkipList() {
			if (!full) {
				CustomSkipList<K, V> ans = new CustomSkipList<K, V>(new MultiLevelComparator<K>() {
					@Override
					public int comparasonCount() {
						return c.comparasonCount() - 1;
					}

					@Override
					public int compareBykey(int index, K obj1, K obj2) {
						return c.compareBykey(index + 1, obj1, obj2);
					}
				});
				ans.start = new node<K, V>(root.key, root.value, root.next.length - 1);
				node<K, V> u = new node<K, V>(null, null, levelCount - 1);
				ans.size++;
				u.updateNext(ans.start, ans.start.next.length);
				for (node<K, V> n = root.next[0]; n != end; n = n.next[0]) {
					node<K, V> d = new node<K, V>(n.key, n.value, n.next.length);
					for (int a = 0; a < n.next.length; a++) {
						u.next[a].next[a] = d;
						u.next[a] = d;
						ans.size++;
					}
				}
				return ans;
			} else {
				return CustomSkipList.this;
			}
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			for (node<K, V> n = root; n != root.next[root.next.length - 1]; n = n.next[0]) {
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
		node<K, V> end;

		public iterator() {
			D = start;
		}

		public iterator(node<K, V> start, node<K, V> end) {
			this.D = start;
			this.end = end;
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
