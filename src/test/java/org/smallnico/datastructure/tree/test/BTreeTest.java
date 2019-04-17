package org.smallnico.datastructure.tree.test;

import java.util.Random;
import java.util.Scanner;

import org.junit.Test;
import org.smallnico.datastructure.btree.BTree;

public class BTreeTest {

	@Test
	public void insertTest() {
		BTree<Integer> btree = new BTree<Integer>(2);
		Random random = new Random();
		int count = 10;
//		int s = 0;
		while(count -- > 3) {
//		    s = random.nextInt(1000);
		    btree.put(count, count);
		    System.out.println(btree);
	        System.out.println();
		}
        
		btree.remove(3);
		System.out.println(btree);
        
//        System.out.println("search check " + s + ": " + btree.search(s));
//        
//		Scanner in = new Scanner(System.in);
//		while(in.hasNextInt()) {
//		    System.out.println(">>>>>>> delete");
//	        System.out.println(btree.remove(in.nextInt()));
//	        System.out.println(btree);
//	        System.out.println();
//		}
		
	}
}
