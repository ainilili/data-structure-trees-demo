package org.smallnico.datastructure.tree.test;

import org.junit.Test;
import org.smallnico.datastructure.tree.AbstractBinaryTree;
import org.smallnico.datastructure.tree.SplayTree;

public class SplayTreeTest {

    @Test
    public void test() {
        AbstractBinaryTree bt = new SplayTree();
        Integer[] array = new Integer[] {7,6,5,4,3,2,1};
        for(int a: array) {
            bt.insert(a, a);
            System.out.println("\n" + bt);
        }
        
        bt.get(1);
        System.out.println("\n" + bt);
        
	}
}
