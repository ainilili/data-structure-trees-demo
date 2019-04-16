package org.smallnico.datastructure.tree.test;

import java.util.Random;
import java.util.Scanner;

import org.junit.Test;
import org.smallnico.datastructure.btree.BTree;

public class BTreeTest {

	@Test
	public void insertTest() {
		BTree<Integer> btree = new BTree<Integer>(4);
		Random random = new Random();
		int count = 20;
		int s = 0;
		while(count -- > 0) {
		    s = random.nextInt(1000);
		    btree.put(s, s);
		}
        System.out.println(btree);
        System.out.println();
        
        System.out.println("search check " + s + ": " + btree.search(s));
        
		Scanner in = new Scanner(System.in);
		while(in.hasNextInt()) {
		    System.out.println(">>>>>>> delete");
	        System.out.println(btree.remove(in.nextInt()));
	        System.out.println(btree);
	        System.out.println();
		}
		
	}
}
