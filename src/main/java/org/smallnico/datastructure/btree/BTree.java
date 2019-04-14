package org.smallnico.datastructure.btree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class BTree<V> {

	private int limit;

	private BNode<V> root;
	
	public BTree(int limit){
		this.limit = limit;
	}
	
	public BEntry<V> insert(int index, V value){
		if(root == null) {
			BEntry<V> inserted = new BEntry<V>(index, value, null);
			root = new BNode<V>(limit, null, inserted);
			return inserted;
		}else {
			return insertEntry(root, new BEntry<V>(index, value, root));
		}
	}
	
	protected BEntry<V> insertEntry(BNode<V> node, BEntry<V> inserted){
		final LinkedList<BEntry<V>> entries = node.entries;
		
		if(entries.isEmpty()) {
			entries.add(inserted);
		}else {
			BEntry<V> target = node.find(inserted.index);
			if(target != null) {
				target.value = inserted.value;
				return target;
			}else {
				int pos = node.indexOf(inserted.index);
				BNode<V> next = pos == -1 ?  entries.getFirst().left : entries.get(pos).right;
				int insertIndex = pos == node.limit ? pos : pos + 1; 
				if(next == null) {
					entries.add(insertIndex, inserted);
					insertEntryAdjust(node);
				}else {
					inserted = insertEntry(next, inserted);
				}
			}
		}
		return inserted;
	}
	
	protected void insertEntryAdjust(BNode<V> node){
		if(node.needAdjust()) {
			SplitEntity<V> splitEntity = split(node);
			if(node.parent == null) {
				BNode<V> newParent = new BNode<V>(node.limit, null, splitEntity.mid);
				root = newParent;
				splitEntity.left.parent = newParent;
				splitEntity.right.parent = newParent;
			}else {
				addToEnties(node.parent, splitEntity.mid);
				insertEntryAdjust(node.parent);
			}
		}
	}
	
	protected BEntry<V> addToEnties(BNode<V> node, BEntry<V> entry){
		int pos = node.indexOf(entry.index);
		int insertIndex = pos == node.limit ? pos : pos + 1; 
		
		final LinkedList<BEntry<V>> entries = node.entries;
		entries.add(insertIndex, entry);
		
		BEntry<V> r = null;
		BEntry<V> l = null;
		if(insertIndex == 0) {
			r = entries.get(1);
		}else if(insertIndex == node.limit) {
			l = entries.get(node.limit - 1);
		}else {
			r = entries.get(insertIndex + 1);
			l = entries.get(insertIndex - 1);
		}
		if(r != null) {
			r.left = entry.right;
		}
		if(l != null) {
			l.right = entry.left;
		}
		return entry;
	}
	
	protected SplitEntity<V> split(BNode<V> node){
		final LinkedList<BEntry<V>> entries = node.entries;
		int splitIndex = ( entries.size() >> 1 ) - 1; 
		
		SplitEntity<V> splitEntity = new SplitEntity<V>();
		LinkedList<BEntry<V>> entryList = new LinkedList<BEntry<V>>();
		
		BEntry<V> middle = entries.get(splitIndex);
		
		splitEntity.left = new BNode<V>(node.limit, node.parent);
		splitEntity.right = new BNode<V>(node.limit, node.parent);
		
		for(int index = 0; index < entries.size(); index ++) {
			BEntry<V> entry = entries.get(index);
			if(index < splitIndex) {
				if(entry.left != null) 
					entry.left.parent = splitEntity.left;
				if(entry.right != null) 
					entry.right.parent = splitEntity.left;
				entryList.add(entry);
			}else if(index == splitIndex) {
				splitEntity.left.entries = entryList;
				splitEntity.mid = middle;
				entryList = new LinkedList<BEntry<V>>();
			}else {
				if(entry.left != null) 
					entry.left.parent = splitEntity.right;
				if(entry.right != null) 
					entry.right.parent = splitEntity.right;
				entryList.add(entry);
				splitEntity.right.entries = entryList;
			}
		}
		middle.left = splitEntity.left;
		middle.right = splitEntity.right;
		return splitEntity;
	}
	
	@Override
	public String toString() {
		Queue<BNode<V>> queue = new LinkedBlockingQueue<BNode<V>>();
		StringBuilder builder = new StringBuilder();
		queue.add(root);
		int h = root.height();
		while(! queue.isEmpty()) {
			BNode<V> cur = queue.poll();
			
			for(int index = 0; index < cur.entries.size(); index ++) {
				BEntry<V> e = cur.entries.get(index);
				if(e.left != null) {
					queue.add(e.left);
				}
				if(index == cur.entries.size() - 1) {
					if(e.right != null) {
						queue.add(e.right);	
					}
				}
			}
			
			if(h == cur.height()) {
				builder.append(cur.entries + " ");
			}else {
				builder.append("\n");
				builder.append(cur.entries + " ");
				h = cur.height();
			}
		}
		return builder.toString();
	}
	
	public static class BNode<V> {
		int limit;
		BNode<V> parent;
		LinkedList<BEntry<V>> entries;
		
		public BNode(int limit, BNode<V> parent, BEntry<V> entry) {
			this(limit, parent);
			entries.add(entry);
		}
		
		public BNode(int limit, BNode<V> parent) {
			this(limit, parent, new LinkedList<BEntry<V>>());
		}
		
		public BNode(int limit, BNode<V> parent, LinkedList<BEntry<V>> entries) {
			this.limit = limit;
			this.parent = parent;
			this.entries = entries;
		}
		
		public boolean needAdjust() {
			return capacity() > limit;
		}

		public int capacity() {
			return entries.size();
		}
		
		public int indexOf(int index) {
			int i = -1;
			for(BEntry<V> e: this.entries) {
				if(e.index > index) break;
				i ++;
			}
			return i;
		}
		
		public BEntry<V> find(int index) {
			for(BEntry<V> e: this.entries) {
				if(e.index == index) return e;
			}
			return null;
		}
		
		public int height() {
			int height = 0;
			BNode<V> cur = this;
			while(cur.parent != null) {
				height ++;
				cur = cur.parent;
			}
			return height;
		}

		@Override
		public String toString() {
			return "BNode [entries=" + entries + "]";
		}
	}
	
	public static class SplitEntity<V>{
		BNode<V> left;
		BNode<V> right;
		BEntry<V> mid;
	}
	
	public static class BEntry<V>{
		Integer index;
		V value;
		BNode<V> left;
		BNode<V> right;
		BNode<V> group;

		public BEntry(Integer index, V value, BNode<V> group) {
			this.index = index;
			this.value = value;
			this.group = group;
		}

		public BEntry<V> right(BNode<V> right){
			this.right = right;
			return this;
		}

		public BEntry<V> left(BNode<V> left){
			this.left = left;
			return this;
		}

		public boolean hasRight() {
			return right != null;
		}

		public boolean hasLeft() {
			return left != null;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}
		
		

	}
}