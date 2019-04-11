package org.smallnico.datastructure.tree;

public class CartesianTree extends BinarySortTree{

    private Node cur;

    @Override
    public Node insert(int index, Object value) {
        if(root == null) {
            root = new Node(index, value, null);
            cur = root;
        }else {
            Node n = new Node(index, value, cur);
            cur.right = n;
            if(index < cur.index) {
                leftRotate(cur);
                while(n.parent != null && n.parent.index > n.index) {
                    leftRotate(n.parent);
                }
            }
            cur = n;
        }
        return cur;
    }

    public Node insert(int index) {
        return insert(index, index);
    }

    @Override
    public Node get(int index) {
        return findInOrder(root, index);
    }

    public Node findInOrder(Node n, int index) {
        if(n == null) return null;
        Node s = findInOrder(n.left, index);
        if(s != null) return s;
        if(n.index == index) return n;
        s = findInOrder(n.right, index);
        if(s != null) return s;
        return null;
    }
    
    public Node findLeft(Node node, int index) {
        Node c = node;
        while(c.left != null) {
            c = c.left;
        }
        if(c.index == index) return c;
        return null;
    }
    
    public void print() {
        printInOrder(root);
        System.out.println();
    }
    
    public void printInOrder(Node n) {
        if(n == null) return;
        printInOrder(n.left);
        System.out.print(n.index + " ");
        printInOrder(n.right);
    }

    @Override
    public Node remove(int index) {

        Node node = get(index);
        if(node != null) {
            if(node.left == null && node.right == null) {
                super.remove(index);
            }else if(node.left != null && node.right == null){
                if(node.parent == null) {
                    root = node.left;
                    node.left.parent = null;
                }else {
                    node.left.parent = node.parent;
                    node.parent.left = node.left;
                }
            }else if(node.left == null && node.right != null) {
                if(node.parent == null) {
                    root = node.right;
                    node.right.parent = null;
                }else {
                    node.right.parent = node.parent;
                    node.parent.right = node.right;
                }
            }else if(node.left != null && node.right != null) {
                if(node.parent == null) {
                    root = node.left;
                    node.left.parent = null;
                }else {
                    node.left.parent = node.parent;
                    if(node.isLeft) {
                        node.parent.left = node.left;
                    }else {
                        node.parent.right = node.left;
                    }
                    
                }
                node.left.right = node.right;
                node.right.parent = node.left;
            }
        }
        return node;
    }

}
