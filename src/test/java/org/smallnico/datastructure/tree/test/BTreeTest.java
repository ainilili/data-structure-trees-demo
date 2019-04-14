package org.smallnico.datastructure.tree.test;

import org.junit.Test;
import org.smallnico.datastructure.btree.BTree;

public class BTreeTest {

	@Test
	public void insertTest() {
		BTree<Integer> btree = new BTree<Integer>(3);
		
		int count = 15;
		
		while(count -- > 0) {
			btree.insert(count, count);
			System.out.println(btree);
			System.out.println();
		}
		System.out.println(btree);
	}
}
