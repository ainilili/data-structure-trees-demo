package org.smallnico.datastructure.tree.test;

import org.junit.Test;
import org.smallnico.datastructure.tree.CartesianTree;

public class CartesianTreeTest {

    @Test
    public void test() {
        CartesianTree bt = new CartesianTree();
//        Integer[] array = new Integer[] {7,2,1,4,5,1,3,3};
        Integer[] array = new Integer[] {6 ,2 ,5, 2 ,5, 5 ,2};
        for(int a: array) {
            bt.insert(a, a);
        }
        
        System.out.println("\n" + bt);
        bt.print();
	}
}
