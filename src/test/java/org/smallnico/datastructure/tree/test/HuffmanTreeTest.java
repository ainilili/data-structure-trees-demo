package org.smallnico.datastructure.tree.test;

import org.junit.Test;
import org.smallnico.datastructure.tree.HuffmanTree;

public class HuffmanTreeTest {

    @Test
    public void test() {
        HuffmanTree ht = new HuffmanTree(new int[] {9,12,6,3,5,15});
        System.out.println(ht);
        
	}
}
