package org.smallnico.datastructure.tree.test;

import org.junit.Test;
import org.smallnico.datastructure.btree.BPlusTree;
import org.smallnico.datastructure.btree.BTree;

public class BPlusTreeTest {

	@Test
	public void insertTest() {
		BTree<Integer> btree = new BPlusTree<Integer>(2);
		int count = 10;
		while(count -- > 3) {
		    btree.put(count, count);
		    System.out.println(btree);
		    System.out.println();
		}
		
		System.out.println(">>> search：" + btree.search(5));
		
		btree.remove(3);
        System.out.println(btree);
	}
}
