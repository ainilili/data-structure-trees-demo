package org.smallnico.datastructure.tree.test;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.Test;
import org.smallnico.datastructure.tree.AVLTree;
import org.smallnico.datastructure.tree.AbstractBinaryTree;

public class AVLTreeTest {

    @Test
    public void test() {
		
//		int count = 30;
//		AbstractBinaryTree bt = new AVLTree();
//		Random random = new Random();
//		
//		while(count -- > 0) {
//		    int a = random.nextInt(30);
//		    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>" + a);
//		    bt.insert(a, a);
//		    System.out.println(bt.toString());
//		    System.out.println();
//		}
        AbstractBinaryTree bt = new AVLTree();
        Integer[] array = new Integer[] {29,8,19,26,0,9,9,25,3,3,19,21,10,20,14,20,0,1,3,3,16,9,9,11,13,25,4,25,17};
        array = Arrays.asList(array).stream().distinct().collect(Collectors.toList()).toArray(new Integer[] {});
        for(int a: array) {
            bt.insert(a, a);
            System.out.println("\n" + bt);
        }
        
        bt.remove(9);
        System.out.println("\n" + bt);
        
        bt.remove(10);
        System.out.println("\n" + bt);
	}
}
