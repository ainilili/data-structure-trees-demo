package org.smallnico.datastructure.tree;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class HuffmanTree extends BinarySortTree{

    public HuffmanTree(int[] ins) {

        LinkedList<Node> list = new LinkedList<AbstractBinaryTree.Node>();

        for(int i = 0; i < ins.length; i ++) {
            list.add(new Node(ins[i], ins[i], null));
        }
        sort(list);

        while(list.size() > 1) {
            Node cur = list.get(0);
            Node nex = list.get(1);
            if(isSingle(cur) && isSingle(nex)) {
                list.set(0, createTree(cur.index, nex.index));
            }else {
                list.set(0, mergeMasterAndStandby(cur, nex));
            }
            list.remove(1);
            sort(list);
        }
        root = list.get(0);
    }

    public void sort(List<Node> ns) {
        Collections.sort(ns, (n1 ,n2) -> n1.index > n2.index ? 1 : -1);
    }

    @Override 
    public Node insert(int index, Object value) {
        throw new UnsupportedOperationException();
    }

    public boolean isSingle(Node n) {
        return n.left == null && n.right == null;
    }

    public Node createTree(int v1, int v2) {
        int newIndex = v1 + v2;
        Node newMaster = new Node(newIndex, newIndex, null);
        newMaster.setLeft(new Node(v1, v1, root));
        newMaster.setRight(new Node(v2, v2, root));
        return newMaster;
    }


    public Node mergeMasterAndStandby(Node v1, Node v2) {
        int newIndex = v1.index + v2.index;
        Node newMaster = new Node(newIndex, newIndex, null);
        newMaster.setLeft(v1);
        newMaster.setRight(v2);    
        return newMaster;
    }


    public Node insert(int v) {
        return insert(v, v);
    }

    @Override
    public Node remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Node get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return 0;
    }

}
