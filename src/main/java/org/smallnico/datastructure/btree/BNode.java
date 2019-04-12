package org.smallnico.datastructure.btree;

import java.util.LinkedList;

public class BNode<V> {

    private LinkedList<BEntry<V>> keys;
    
    private LinkedList<BNode<V>> values;
    
    
    public LinkedList<BEntry<V>> getKeys() {
        return keys;
    }

    public void setKeys(LinkedList<BEntry<V>> keys) {
        this.keys = keys;
    }

    public LinkedList<BNode<V>> getValues() {
        return values;
    }

    public void setValues(LinkedList<BNode<V>> values) {
        this.values = values;
    }

    public static class BEntry<V>{
        
        private Integer index;
        
        private V value;

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
        
    }
}
