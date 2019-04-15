package org.smallnico.datastructure.tree.test;

import java.util.Scanner;

import org.junit.Test;
import org.smallnico.datastructure.btree.BTree;

public class BTreeTest {

	@Test
	public void insertTest() {
		BTree<Integer> btree = new BTree<Integer>(3);
		
		int count = 15;
		
		while(count -- > 0) {
			btree.put(count, count);
			
			System.out.println(btree);
	        System.out.println();
		}
		
		
		System.out.println(">>>>>>> search");
		System.out.println(btree.search(14));
		
		Scanner in = new Scanner(System.in);
		while(in.hasNextInt()) {
		    System.out.println(">>>>>>> delete");
	        System.out.println(btree.remove(in.nextInt()));
	        System.out.println(btree);
	        System.out.println();
		}
		
		
		
	}
}
