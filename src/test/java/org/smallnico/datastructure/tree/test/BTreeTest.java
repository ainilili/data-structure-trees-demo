package org.smallnico.datastructure.tree.test;

import org.junit.Test;
import org.smallnico.datastructure.btree.BTree;

public class BTreeTest {

	@Test
	public void insertTest() {
		BTree<Integer> btree = new BTree<Integer>(10);
		
		int count = 3000;
		
		while(count -- > 0) {
			btree.put(count, count);
		}
		System.out.println(btree);
		System.out.println();
		
		System.out.println(">>>>>>> search");
		System.out.println(btree.search(25));
	}
}
