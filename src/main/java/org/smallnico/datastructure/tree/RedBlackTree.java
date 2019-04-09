package org.smallnico.datastructure.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.smallnico.datastructure.tree.AVLTree.ANode;
import org.smallnico.datastructure.tree.AbstractBinaryTree.Node;

/**
 * AVL：左右子树最大深度差的绝对值小于2
 *  1、每个结点或是红色的，或是黑色的 
    2、根节点是黑色的 
    3、每个叶结点（NIL）是黑色的 
    4、如果一个节点是红色的，则它的两个儿子都是黑色的。 
    5、对于每个结点，从该结点到其叶子结点构成的所有路径上的黑结点个数相同。
 * @author nico
 */
public class RedBlackTree extends BinarySortTree{

    @Override
    public Node insert(int index, Object value) {
        Node newNode = super.insert(index, value);
        rebalance((RNode)newNode);
        return newNode;
    }

    public Node insert(int index) {
        return insert(index, index);
    }

    @Override
    public Node remove(int index) {
        if(index == 0) {
            System.out.println(1);
        }

        Node node = super.get(index);
        if(node == null) {
            return null;
        }
        Node temp = node;
        while(temp.left != null || temp.right != null) {
            if(temp.left != null) {
                Node n = temp.left;
                while(n.right != null) {
                    n = n.right;
                }
                temp.index = n.index;
                temp.value = n.value;
                temp = n;    
            }else {
                Node n = temp.right;
                temp.index = n.index;
                temp.value = n.value;
                temp = n;  
            }
        }
        //边缘叶子结点
        RNode rn = rnode(temp);
        super.remove(rn);
        rebalanceDelete(rn);
        return node;
    }

    private void rebalanceDelete(RNode rn) {
        if(rn.isRoot()) {
            return;
        }
        if(rn.isRed) return;

        //以下为被删除结点为黑的场景

        RNode p = rnode(rn.parent);
        RNode s = rn.isLeft ? rnode(p.right) : rnode(p.left);

        if(rn.isLeft) {
            if(p.isRed) {
                if(s.right == null && s.left == null) {
                    //TODO 兄弟结点没有子结点
                }else if(s.right == null && s.left != null) {
                    rightRotate(s);
                    p.isRed = false;
                    leftRotate(p);
                }else {
                    s.isRed = true;
                    p.isRed = false;
                    rnode(s.right).isRed = false;
                    leftRotate(p);
                }
            }else {
                if(! s.isRed) {
                    s.isRed = false;
                    p.isRed = true;
                    if(s.right != null) {
                        rnode(s.right).isRed = false;
                    }
                    leftRotate(p);
                }else {
                    if(s.left != null && s.right != null) {
                        s.isRed = false;
                        p.isRed = true;
                        leftRotate(p);
                    }else if(s.left == null && s.right != null) {
                        s.isRed = false;
                        leftRotate(p);
                    }else {
                        s.isRed = false;
                        rightRotate(s);
                        leftRotate(p);
                    }
                }
            }
        }else {
            if(p.isRed) {
                p.isRed = false;
            }
        }
    }

    @Override
    public Node createNode(int index, Object value, Node parent) {
        return new RNode(index, value, parent);
    }

    private void rebalance(RNode node) {
        if(node.isRoot()) {
            node.isRed = false;
            return;
        }

        if(node.parent != null) {
            if(! rnode(node.parent).isRed) {
                //父节点是黑色，不需要变动
                return;
            }

            RNode parent = rnode(node.parent);
            RNode grandfather = rnode(node.parent.parent);

            if(parent.isRed) {
                //叔叔节点
                RNode n = rnode(parent.isLeft ? grandfather.right : grandfather.left);
                if(n == null) {
                    if(parent.isLeft) {
                        if(node.isLeft) {
                            rightRotate(grandfather);
                            parent.isRed = false;
                            grandfather.isRed = true;
                            rebalance(parent);
                        }else {
                            leftRotate(parent);
                            rightRotate(grandfather);
                            node.isRed = false;
                            grandfather.isRed = true;
                            rebalance(node);
                        }
                    }else {
                        if(node.isLeft) {
                            rightRotate(parent);
                            leftRotate(grandfather);
                            node.isRed = false;
                            grandfather.isRed = true;
                            rebalance(node);
                        }else {
                            leftRotate(grandfather);
                            parent.isRed = false;
                            grandfather.isRed = true;
                            rebalance(parent);
                        }
                    }
                }else if(n.isRed) {
                    parent.isRed = false;
                    n.isRed = false;
                    if(! grandfather.isRoot()) {
                        grandfather.isRed = true;
                        rebalance(grandfather);
                    }
                }else{
                    if(parent.isLeft) {
                        if(node.isLeft) {
                            parent.isRed = false;
                            grandfather.isRed = true;
                            rightRotate(grandfather);
                            rebalance(parent);
                        }else {
                            node.isRed = false;
                            grandfather.isRed = true;
                            leftRotate(parent);
                            rightRotate(grandfather);
                            rebalance(node);
                        }
                    }else {
                        if(! node.isLeft) {
                            parent.isRed = false;
                            grandfather.isRed = true;
                            leftRotate(grandfather);
                            rebalance(parent);
                        }else {
                            node.isRed = false;
                            grandfather.isRed = true;
                            rightRotate(parent);
                            leftRotate(grandfather);
                            rebalance(node);
                        }
                    }
                }
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
    }

    class RNode extends Node{

        private boolean isRed = true;

        public RNode(int index, Object value, Node parent) {
            super(index, value, parent);
        }

        public boolean isRed() {
            return isRed;
        }

        public void setRed(boolean isRed) {
            this.isRed = isRed;
        }

        public boolean isRoot() {
            return this.index == root.index;
        }

    }

    public RNode rnode(Node node) {
        return (RNode) node;
    }

    public String toString(boolean colorOnly) {
        //保存树形
        StringBuilder builder = new StringBuilder();
        //按层序保留结点
        List<Object> cache = new ArrayList<>(50);
        //使用栈对树做层序遍历
        Queue<Node> queue = new LinkedBlockingQueue<Node>();
        queue.add(root);

        int depth = 0; //临时深度
        int maxDepth = getMaxDepth(); //最大深度
        while(! queue.isEmpty()) {
            Node cur = queue.poll();
            if(cur.left != null) {
                queue.add(cur.left);
            }else if(cur.height() < maxDepth){
                //填补空缺
                queue.add(createNode(0, '#', cur)); 
            }
            if(cur.right != null) {
                queue.add(cur.right);
            }else if(cur.height() < maxDepth){
                //填补空缺
                queue.add(createNode(0, '#', cur)); 
            }
            if(depth != cur.height()) {
                //深度切换，将高度保存
                depth = cur.height();
                cache.add(depth);
            }
            //加入该结点
            cache.add(cur);
        }
        //将保留结点渲染为树形
        for(int index = 0; index < cache.size(); index ++) {
            Object o = cache.get(index);
            if(o instanceof Integer) {
                builder.append(System.lineSeparator());
                builder.append(getOffset(maxDepth - (Integer)o));
            }else {
                if(index == 0) {
                    builder.append(getOffset(maxDepth));
                }
                String c = String.valueOf(((RNode)o).value);
                if(c.equals("#")) {
                    builder.append(c);
                }else {
                    builder.append(colorWrapper(Integer.valueOf(c), ((RNode)o).isRed, colorOnly));
                }
                builder.append(getOffset(maxDepth - ((Node)o).height() + 1));
            }
        }
        return builder.toString();
    }

    protected String colorWrapper(int o, boolean isRed, boolean onlyColor) {
        if(onlyColor) {
            return isRed ? "○" : "●";
        }
        if(isRed) {
            switch (o) {
            case 1:
                return "①";
            case 2:
                return "②";
            case 3:
                return "③";
            case 4:
                return "④";
            case 5:
                return "⑤";
            case 6:
                return "⑥";
            case 7:
                return "⑦";
            case 8:
                return "⑧";
            case 9:
                return "⑨";
            case 10:
                return "⑩";
            }
        }else {
            switch (o) {
            case 1:
                return "❶";
            case 2:
                return "❷";
            case 3:
                return "❸";
            case 4:
                return "❹";
            case 5:
                return "❺";
            case 6:
                return "❻";
            case 7:
                return "❼";
            case 8:
                return "❽";
            case 9:
                return "❾";
            case 10:
                return "❿";
            } 
        }
        return String.valueOf(o);
    }

}
