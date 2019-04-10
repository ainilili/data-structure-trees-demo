package org.smallnico.datastructure.tree;

import org.smallnico.datastructure.tree.AbstractBinaryTree.Node;

/**
 * AVL：左右子树最大深度差的绝对值小于2
 *
 * @author nico
 */
public class SplayTree extends BinarySortTree{

    @Override
    public Node get(int index) {
        Node node = super.get(index);
        if(node != null) {
            adjust(node);
        }
        return node;
    }
    
    public void adjust(Node node) {
        if(node.parent == null) {
            return;
        }
        Node p = node.parent;
        Node g = p.parent;
        
        if(g != null) {
            if(node.isLeft && p.isLeft) {
                rightRotate(g);
                rightRotate(p);
            }else if(! node.isLeft && ! p.isLeft) {
                leftRotate(g);
                leftRotate(p);
            }else if(node.isLeft && ! p.isLeft) {
                rightRotate(p);
                leftRotate(g);
            }else if(! node.isLeft && p.isLeft) {
                leftRotate(p);
                rightRotate(g);
            }
        }else {
            if(node.isLeft) {
                rightRotate(p);
            }else {
                leftRotate(p);
            }
        }
        adjust(node);
    }
    
    
}
