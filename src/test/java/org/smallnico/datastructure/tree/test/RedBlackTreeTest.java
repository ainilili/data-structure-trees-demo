package org.smallnico.datastructure.tree.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.junit.Test;
import org.smallnico.datastructure.tree.AbstractBinaryTree;
import org.smallnico.datastructure.tree.RedBlackTree;

public class RedBlackTreeTest {

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
        RedBlackTree bt = new RedBlackTree();
        Integer[] array = new Integer[] {29,8,19,26,0,9,9,25,3,3,19,21,10,20,14,20,0,1,3,3,16,9,9,11,13,25,4,25,17};
//        Integer[] array = new Integer[] {1,2,3,4,5,6,7,8,9,10};
        array = Arrays.asList(array).stream().distinct().collect(Collectors.toList()).toArray(new Integer[] {});
        for(int a: array) {
            bt.insert(a, a);
            System.out.println("\n>>>>>>>>> 插入：" + a);
            System.out.println(bt);
            System.out.println("\n" + bt.toString(true));
        }
        
        delete(19, bt);
        delete(13, bt);
        delete(21, bt);
        delete(20, bt);
        delete(0, bt);
        delete(25, bt);
        
	}
    
    static void delete(int index, RedBlackTree bt) {
        bt.remove(index);
        System.out.println("\n >>>>>>>>>>>>>>>> 删除：" + index);
        System.out.println("\n" + bt);
        System.out.println("\n" + bt.toString(true));
    }
}
