package org.smallnico.datastructure.btree;

import java.util.LinkedList;

import org.smallnico.datastructure.btree.BTree.BEntry;
import org.smallnico.datastructure.btree.BTree.BNode;

public class BPlusTree<V> extends BTree<V>{

    public BPlusTree(int limit) {
        super(limit);
    }
    
    protected BEntry<V> search(BNode<V> node, int index) {
	    if(node == null) return null;
	    BEntry<V> result = node.find(index);
        if(result != null && result.isLeaf()) {
            return result;
        }
        int pos = node.indexOf(index) + 1;
        if(pos < node.entries.size()) {
            return search(node.entries.get(pos).left, index);
        }else {
            return search(node.entries.get(pos - 1).right, index);
        }
	}
    
    protected SplitEntity<V> split(BNode<V> node){
        final LinkedList<BEntry<V>> entries = node.entries;
        int splitIndex = ( entries.size() >> 1 ); 
        if((entries.size() ^ 0x00000001) == 1) splitIndex --;
        
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
                entry.group = splitEntity.left;
                entryList.add(entry);
            }else if(index == splitIndex) {
                splitEntity.left.entries = entryList;
                splitEntity.mid = middle;
                entryList = new LinkedList<BEntry<V>>();
                if(middle.isLeaf()) {
                    BEntry<V> c = middle.copy();
                    entryList.add(c);
                    c.group = splitEntity.right;
                    middle.value = null;
                }
            }else {
                if(entry.left != null) 
                    entry.left.parentNode = splitEntity.right;
                if(entry.right != null) 
                    entry.right.parentNode = splitEntity.right;
                entry.group = splitEntity.right;
                entryList.add(entry);
                splitEntity.right.entries = entryList;
            }
        }
        middle.left = splitEntity.left;
        middle.right = splitEntity.right;
        
        if(middle.left.entries.get(0).isLeaf()) {
        	middle.left.rightNode = middle.right;
        }
        return splitEntity;
    }
    
    protected BEntry<V> addToEnties(BNode<V> node, BEntry<V> entry){
        int pos = node.indexOf(entry.index);
        final LinkedList<BEntry<V>> entries = node.entries;
        
        int insertIndex = pos == node.limit ? pos : pos + 1; 
        
        
        entries.add(insertIndex, entry);
        
        BEntry<V> r = null;
        BEntry<V> l = null;
        if(insertIndex == 0) {
            r = entries.get(1);
        }else if(insertIndex == entries.size() - 1) {
            l = entries.get(insertIndex - 1);
        }else {
            r = entries.get(insertIndex + 1);
            l = entries.get(insertIndex - 1);
        }
        if(r != null) {
            r.left = entry.right;
            if(entry.right.entries.get(0).value != null) {
            	entry.right.rightNode = r.right;
            }
        }
        if(l != null) {
            l.right = entry.left;
            if(l.left.entries.get(0).value != null) {
            	l.left.rightNode = entry.left;
            }
        }
        
        entry.group = node;
        return entry;
    }
    
    protected BEntry<V> insertEntry(BNode<V> node, BEntry<V> inserted){
        final LinkedList<BEntry<V>> entries = node.entries;
        
        if(entries.isEmpty()) {
            entries.add(inserted);
            inserted.group = node;
        }else {
            BEntry<V> target = node.find(inserted.index);
            if(target != null && target.isLeaf()) {
                target.value = inserted.value;
                return target;
            }else {
                int pos = node.indexOf(inserted.index);
                BNode<V> next = pos == -1 ?  entries.getFirst().left : entries.get(pos).right;
                int insertIndex = pos == node.limit ? pos : pos + 1; 
                if(next == null) {
                    entries.add(insertIndex, inserted);
                    inserted.group = node;
                    insertEntryAdjust(node);
                }else {
                    inserted = insertEntry(next, inserted);
                }
            }
        }
        return inserted;
    }
    
    public void removeEntry(BEntry<V> target) {
        if(target.left == null) {
            if(target.group.capacity() == 1) {
                if(target.group.parentNode == null) {
                    root = null;
                }else {
                    removeEntryAdjust(target, target.group.leftParentEntry(), target.group.rightParentEntry());    
                }
            }else {
                BEntry<V> lp = target.group.leftParentEntry();
                if(target.group.entries.getFirst().index == target.index && lp != null) {
                    lp.index = target.group.entries.get(1).index;
                }
                target.group.entries.remove(target);
            }
        }else {
            BNode<V> left = target.left;

            while(left.entries.getLast().right != null) {
                left = left.entries.getLast().right;
            }

            BEntry<V> replaced = left.entries.getLast();
            BEntry<V> lp = replaced.group.leftParentEntry();
            BEntry<V> rp = replaced.group.rightParentEntry();

            target.index = replaced.index;
            target.value = replaced.value;
            removeEntryAdjust(replaced, lp, rp);
        }
    }
    
    public void removeEntryAdjust(BEntry<V> target,  BEntry<V> lp, BEntry<V> rp) {
        if(target.group.entries.size() > 1) {
            if(target.group.entries.getFirst().index == target.index && lp != null) {
                lp.index = target.group.entries.get(1).index;
            }
            target.group.entries.remove(target);
        }else {
            if(lp != null) {
                BNode<V> lpl = lp.left;
                BNode<V> lpg = lp.group;
                if(lpl.capacity() == 1) {
                    lpl.entries.addLast(lp);
                    if(rp != null) rp.left = lpl;
                    lpg.entries.remove(lp);
                    lp.group = lpl;
                    if(lpg.isBlank()) {
                        lpg.entries = lpl.entries;
                        for(BEntry<V> e: lpl.entries) {
                            e.group = lpg;
                            e.left = null;
                            e.right = null;
                        }
                    }
                    if(lpg.parentNode != null) {
                        removeSingleNodeAdjust(lpg);
                    }
                }else {
                    BEntry<V> r = lpl.entries.getLast();
                    target.index = lp.index;
                    target.value = lp.value;
                    lp.index = r.index;
                    lp.value = r.value;
                    lpl.entries.remove(r);
                }
            }else {
                BNode<V> rpr = rp.right;
                BNode<V> rpg = rp.group;
                if(rpr.capacity() == 1) {
                    rpr.entries.addFirst(rp);
                    rpg.entries.remove(rp);
                    rp.group = rpr;
                    if(rpg.isBlank()) {
                        rpg.entries = rpr.entries;
                        for(BEntry<V> e: rpr.entries) {
                            e.group = rpg;
                            e.left = null;
                            e.right = null;
                        }
                    }
                    if(rpg.parentNode != null) {
                        removeSingleNodeAdjust(rpg);
                    }
                }else {
                    BEntry<V> r = rpr.entries.getFirst();
                    target.index = rp.index;
                    target.value = rp.value;
                    rp.index = r.index;
                    rp.value = r.value;
                    rpr.entries.remove(r);
                }
            }

        }
    }
    
}