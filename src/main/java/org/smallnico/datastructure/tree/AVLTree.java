package org.smallnico.datastructure.tree;

/**
 * AVL：左右子树最大深度差的绝对值小于2
 *
 * @author nico
 */
public class AVLTree extends BinarySortTree{

    @Override
    public Node insert(int index, Object value) {
        Node newNode = super.insert(index, value);
        rebalance((ANode)newNode);
        return newNode;
    }

    public Node insert(int index) {
        return insert(index, index);
    }

    @Override
    public Node remove(int index) {
        Node node = super.get(index);
        if(node == null) {
            return null;
        }
        if(node.left == null && node.right == null) {
            super.remove(index);
            rebalance((ANode)node);
        }else {
            if(node.left == null) {
                super.remove(index);
                rebalance((ANode)node);
            }else {
                Node n = node.left;
                while(n.right != null) {
                    n = n.right;
                }
                super.remove(n.index);
                node.index = n.index;
                node.value = n.value;
                n.index = index;
                
                rebalance((ANode)n);
            }
        }
        return node;
    }

    @Override
    public Node createNode(int index, Object value, Node parent) {
        return new ANode(index, value, parent);
    }

    private void rebalance(ANode node) {
        flushLinkDeep(node);
    }

    private boolean rebalanceF(ANode node, ANode newNode) {
        if(Math.abs(node.leftDeep - node.rightDeep) > 1) {
            if(node.leftDeep > node.rightDeep) {
                if(newNode.index < node.left.index) {
                    rightRotate(node);
                }else {
                    leftRotate(node.left);
                    rightRotate(node);
                }
            }else {
                if(newNode.index < node.right.index) {
                    rightRotate(node.right);
                    leftRotate(node);
                }else {
                    leftRotate(node);
                }

            }
            return true;
        }
        return false;
    }

    private void flushLinkDeep(ANode node) {
        node.flushDeep();
        ANode parent = null;
        ANode cur = node;
        while((parent = (ANode)cur.parent) != null) {
            parent.flushDeep();
            cur = parent;
            if(rebalanceF(cur, node)) {
                break;
            }
        }
    }

    protected void leftRotate(Node node) {
        Node center = node.right;

        center.parent = node.parent;
        if(center.parent == null) {
            this.root = center;
        }else {
            if(node.isLeft) {
                node.parent.left = center;    
            }else {
                node.parent.right = center;
            }
            center.isLeft = node.isLeft;
        }

        node.right = null;
        node.isLeft = true;

        Node leftMost = center.left;
        
        if(leftMost != null) {
            leftMost.parent = node;
            node.right = leftMost;
            leftMost.isLeft = false;
        }
        center.left = node;
        node.parent = center;
        aNode(center).flushDeep();
        aNode(node).flushDeep();
    }

    private ANode aNode(Node node) {
        return (ANode)node;
    }

    protected void rightRotate(Node node) {
        Node center = node.left;

        center.parent = node.parent;
        if(center.parent == null) {
            this.root = center;
        }else {
            if(node.isLeft) {
                node.parent.left = center;    
            }else {
                node.parent.right = center;
            }
            center.isLeft = node.isLeft;
        }

        node.left = null;
        node.isLeft = false;

        Node rightMost = center.right;
        if(rightMost != null) {
            rightMost.parent = node;
            node.left = rightMost;
            rightMost.isLeft = true;
        }
        center.right = node;
        node.parent = center;
        aNode(center).flushDeep();
        aNode(node).flushDeep();
    }


    class ANode extends Node{

        private int leftDeep;

        private int rightDeep;

        public ANode(int index, Object value, Node parent) {
            super(index, value, parent);
        }

        public int getLeftDeep() {
            return leftDeep;
        }

        public void setLeftDeep(int leftDeep) {
            this.leftDeep = leftDeep;
        }

        public int getRightDeep() {
            return rightDeep;
        }

        public void setRightDeep(int rightDeep) {
            this.rightDeep = rightDeep;
        }

        public void flushDeep() {
            if(right != null) {
                rightDeep =  Math.max(aNode(right).leftDeep, aNode(right).rightDeep) + 1;
            }else {
                rightDeep = 0;
            }
            if(left != null) {
                leftDeep =  Math.max(aNode(left).leftDeep, aNode(left).rightDeep) + 1;
            }else {
                leftDeep = 0;
            }
        }

    }
}
