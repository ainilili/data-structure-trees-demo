package org.smallnico.datastructure.tree.test;

import org.junit.Test;
import org.smallnico.datastructure.tree.AbstractBinaryTree;
import org.smallnico.datastructure.tree.SplayTree;

public class SplayTreeTest {

    @Test
    public void test() {
        AbstractBinaryTree bt = new SplayTree();
        Integer[] array = new Integer[] {1,32,30,31,28,29,26,27,24,25,22,23,20,21,18,19,16,17,14,15,12,13,10,11,8,9,6,7,4,5,2,3};
        for(int a: array) {
            bt.insert(a, a);
//            System.out.println("\n" + bt);
        }
        
        bt.get(2);
        bt.get(3);
        System.out.println("\n" + bt);
        
	}
}
