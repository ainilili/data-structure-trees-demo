package org.smallnico.datastructure.btree;

import java.util.LinkedList;

public class BStarTree<V> extends BPlusTree<V>{

    public BStarTree(int limit) {
        super(limit);
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
                entryList.add(middle.copy());
                middle.value = null;
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
        
        middle.left.rightNode = middle.right;
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
            entry.right.rightNode = r.right;
        }
        if(l != null) {
            l.right = entry.left;
            l.left.rightNode = entry.left;
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
    
    
}