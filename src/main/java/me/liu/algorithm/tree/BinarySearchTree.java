package me.liu.algorithm.tree;

/**
 * (01) 若任意节点的左子树不空，则左子树上所有结点的值均小于它的根结点的值；
 * (02) 任意节点的右子树不空，则右子树上所有结点的值均大于它的根结点的值；
 * (03) 任意节点的左、右子树也分别为二叉查找树。
 * (04) 没有键值相等的节点（no duplicate nodes）。
 * <p>
 * 现在的问题是不是平衡的，导致查找和排序的性能差
 * Created by tongwei on 17/8/25.
 */
public class BinarySearchTree<T extends Comparable<T>> {


    private BSTNode<T> mRoot;    // 根结点

    public class BSTNode<T extends Comparable<T>> {
        T key;                // 关键字(键值)
        BSTNode<T> left;      // 左孩子
        BSTNode<T> right;     // 右孩子
        BSTNode<T> parent;    // 父结点

        public BSTNode(T key, BSTNode<T> parent, BSTNode<T> left, BSTNode<T> right) {
            this.key = key;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }
    }


    public void preOrder(BSTNode<T> tree) {

        if (tree == null) {
            return;
        }

        System.out.println(tree.key);

        preOrder(tree.left);

        preOrder(tree.right);

    }


    public void inOrder(BSTNode<T> tree) {

        if (tree == null) {
            return;
        }


        inOrder(tree.left);

        System.out.println(tree.key);

        inOrder(tree.right);

    }

    public void postOrder(BSTNode<T> tree) {

        if (tree == null) {
            return;
        }

        postOrder(tree.left);

        postOrder(tree.right);

        System.out.println(tree.key);

    }


    public BSTNode<T> iterativeSearch(BSTNode<T> x, T key) {

        while (x != null) {

            int cmp = x.key.compareTo(key);
            if (cmp == 0) {
                return x;
            } else if (cmp == -1) { //x小
                x = x.right;
            } else { //x 大
                x = x.left;
            }
        }

        return x;
    }


    private void insert(T key) {

        BSTNode<T> newNode = new BSTNode<>(key, null, null, null);

        BSTNode<T> x = mRoot;

        BSTNode<T> tmp = null;

        while (x != null) {

            tmp = x;

            int cmp = x.key.compareTo(key);

            if (cmp > 0) {
                x = x.left;
            } else {
                x = x.right;
            }

        }

        newNode.parent = tmp;
        if (tmp == null) {
            mRoot = newNode;
        } else {
            int cmp = newNode.key.compareTo(tmp.key);

            if (cmp >= 0) {
                tmp.right = newNode;
            } else {
                tmp.left = newNode;
            }
        }


    }

    private void print(BSTNode<T> tree, T key, int direction) {

        if (tree != null) {

            if (direction == 0)    // tree是根节点
                System.out.printf("%2d is root\n", tree.key);
            else                // tree是分支节点
                System.out.printf("%2d is %2d's %6s child\n", tree.key, key, direction == 1 ? "right" : "left");

            print(tree.left, tree.key, -1);
            print(tree.right, tree.key, 1);
        }
    }


    public static void main(String[] args) {

        int[] array = new int[]{5, 4, 1, 2, 3};

        BinarySearchTree binarySearchTree = new BinarySearchTree();

        for (int value :
                array) {
            binarySearchTree.insert(value);
        }

        //binarySearchTree.preOrder(binarySearchTree.mRoot);

        binarySearchTree.inOrder(binarySearchTree.mRoot);//中序遍历是排序的

        //binarySearchTree.postOrder(binarySearchTree.mRoot);

        binarySearchTree.print(binarySearchTree.mRoot, binarySearchTree.mRoot.key, 0);

    }


}
