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
	
	public V put(int index, V value){
		if(root == null) {
			BEntry<V> inserted = new BEntry<V>(index, value, null);
			root = new BNode<V>(limit, null, inserted);
		}else {
		    insertEntry(root, new BEntry<V>(index, value, root));
		}
		return value;
	}
	
	public V search(int index){
	    BEntry<V> result = search(root, index);
	    return result != null ? result.value : null;
	}
	
	public V remove(int index) {
	    BEntry<V> target = search(root, index);
	    if(target == null) return null;
	    
	    if(target.left == null && target.right == null) {
	        target.group.entries.remove(target);
	        if(target.group.isBlank()) {
	        }
	    }
	    
	    
	    removeEntryAdjust(target);
	    return target.value;
	}
	
	public void removeEntryAdjust(BEntry<V> target) {
	    
	}
	
	protected BEntry<V> search(BNode<V> node, int index) {
	    if(node == null) return null;
	    BEntry<V> result = node.find(index);
        if(result == null) {
            int pos = node.indexOf(index) + 1;
            if(pos < node.entries.size()) {
                return search(node.entries.get(pos).left, index);
            }else {
                return search(node.entries.get(pos - 1).right, index);
            }
        }
        return result;
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
			if(node.parentNode == null) {
				BNode<V> newParent = new BNode<V>(node.limit, null, splitEntity.mid);
				root = newParent;
				splitEntity.left.parentNode = newParent;
				splitEntity.right.parentNode = newParent;
			}else {
				addToEnties(node.parentNode, splitEntity.mid);
				insertEntryAdjust(node.parentNode);
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
		
		splitEntity.left = new BNode<V>(node.limit, node.parentNode);
		splitEntity.right = new BNode<V>(node.limit, node.parentNode);
		
		for(int index = 0; index < entries.size(); index ++) {
			BEntry<V> entry = entries.get(index);
			if(index < splitIndex) {
				if(entry.left != null) 
					entry.left.parentNode = splitEntity.left;
				if(entry.right != null) 
					entry.right.parentNode = splitEntity.left;
				entryList.add(entry);
			}else if(index == splitIndex) {
				splitEntity.left.entries = entryList;
				splitEntity.mid = middle;
				entryList = new LinkedList<BEntry<V>>();
			}else {
				if(entry.left != null) 
					entry.left.parentNode = splitEntity.right;
				if(entry.right != null) 
					entry.right.parentNode = splitEntity.right;
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
		BNode<V> parentNode;
		BEntry<V> parentEntryLeft;
		BEntry<V> parentEntryRight;
		
		LinkedList<BEntry<V>> entries;
		
		public BNode(int limit, BNode<V> parent, BEntry<V> entry) {
			this(limit, parent);
			entries.add(entry);
		}
		
		public BNode(int limit, BNode<V> parent) {
			this(limit, parent, new LinkedList<BEntry<V>>());
		}
		
		public BNode(int limit, BNode<V> parentNode, LinkedList<BEntry<V>> entries) {
			this.limit = limit;
			this.parentNode = parentNode;
			this.entries = entries;
		}
		
		public boolean needAdjust() {
			return capacity() > limit;
		}

		public int capacity() {
			return entries.size();
		}
		
		public boolean isBlank() {
		    return capacity() == 0;
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
			while(cur.parentNode != null) {
				height ++;
				cur = cur.parentNode;
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

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((index == null) ? 0 : index.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            BEntry<V> other = (BEntry<V>) obj;
            if (index == null) {
                if (other.index != null)
                    return false;
            } else if (!index.equals(other.index))
                return false;
            return true;
        }
	}
}