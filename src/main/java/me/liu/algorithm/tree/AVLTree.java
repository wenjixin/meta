package me.liu.algorithm.tree;

/**
 * Created by tongwei on 17/8/25.
 */
public class AVLTree<T extends Comparable<T>> {

    private AVLTreeNode<T> mRoot;

    private class AVLTreeNode<T> {

        private T key;
        private int height;

        private AVLTreeNode<T> left;
        private AVLTreeNode<T> right;

        public AVLTreeNode(T key, AVLTreeNode<T> left, AVLTreeNode<T> right) {
            this.key = key;
            this.left = left;
            this.right = right;
            this.height = 0;
        }
    }

    /**
     * 获取树的高度
     */
    private int height(AVLTreeNode<T> tree) {
        if (tree != null)
            return tree.height;

        return 0;
    }

    public int height() {
        return height(mRoot);
    }


    private AVLTreeNode<T> leftLeftRotation(AVLTreeNode<T> k2) {
        AVLTreeNode<T> k1;

        k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;

        k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
        k1.height = Math.max(height(k1.left), k2.height) + 1;

        return k1;
    }

    private AVLTreeNode<T> rightRightRotation(AVLTreeNode<T> k2) {
        AVLTreeNode<T> k1;

        k1 = k2.right;
        k2.right = k1.left;
        k1.left = k2;

        k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
        k1.height = Math.max(k2.height, height(k1.right)) + 1;

        return k1;
    }

    /**
     * LR：左右对应的情况(左双旋转)。
     * <p>
     * 返回值：旋转后的根节点
     */
    private AVLTreeNode<T> leftRightRotation(AVLTreeNode<T> k3) {

        k3.left = rightRightRotation(k3.left);

        return leftLeftRotation(k3);
    }


    /**
     * LR：左右对应的情况(左双旋转)。
     * <p>
     * 返回值：旋转后的根节点
     */
    private AVLTreeNode<T> rightLeftRotation(AVLTreeNode<T> k1) {

        k1.right = leftLeftRotation(k1.right);

        return rightRightRotation(k1);

    }

    public void insert(T key) {
        mRoot = insert(mRoot, key);
    }

    private AVLTreeNode<T> insert(AVLTreeNode<T> tree, T key) {

        if (tree == null) {
            tree = new AVLTreeNode<>(key, null, null);
        } else {

            int cmp = key.compareTo(tree.key);

            if (cmp > 0) { //需要插入tree的右子树
                tree.right = insert(tree.right, key);

                if (height(tree.right) - height(tree.left) > 1) {//不平衡

                    if (key.compareTo(tree.right.key) > 0) { //右子树的右子树不平衡  RR
                        tree = rightRightRotation(tree);
                    } else {//RL
                        tree = rightLeftRotation(tree);
                    }
                }
            } else {//插入tree的左子树

                tree
                        .left = insert(tree.left, key);
                if (height(tree.left) - height(tree.right) > 1) {
                    if (key.compareTo(tree.left.key) < 0) { //LL
                        tree = leftLeftRotation(tree);

                    } else {//LR
                        tree = leftRightRotation(tree);
                    }
                }

            }
        }
        tree.height = Math.max(height(tree.left), height(tree.right)) + 1;

        return tree;

    }

    /**
     * 删除结点(z)，返回根节点
     * <p>
     * 参数说明：
     * tree AVL树的根结点
     * z 待删除的结点
     * 返回值：
     * 根节点
     */
    private AVLTreeNode<T> remove(AVLTreeNode<T> tree, AVLTreeNode<T> z) {

        if (tree == null || z == null)
            return null;

        //从左子树中删除


        //从右子树中删除

        //中间节点删除
        return null;
    }

    public void remove(T key) {
        AVLTreeNode<T> z;

        if ((z = search(mRoot, key)) != null)
            mRoot = remove(mRoot, z);
    }

    private AVLTreeNode<T> search(AVLTreeNode<T> mRoot, T key) {

        if (mRoot == null) {
            return mRoot;
        }

        int cmp = key.compareTo(mRoot.key);
        if (cmp > 0) { //R
            mRoot = search(mRoot.right, key);

        } else if (cmp < 0) {
            mRoot = search(mRoot.left, key);
        } else {
        }

        return mRoot;


    }

    /**
     * 打印"二叉查找树"
     * <p>
     * key        -- 节点的键值
     * direction  --  0，表示该节点是根节点;
     * -1，表示该节点是它的父结点的左孩子;
     * 1，表示该节点是它的父结点的右孩子。
     */
    private void print(AVLTreeNode<T> tree, T key, int direction) {
        if (tree != null) {
            if (direction == 0)    // tree是根节点
                System.out.printf("%2d is root\n", tree.key, key);
            else                // tree是分支节点
                System.out.printf("%2d is %2d's %6s child\n", tree.key, key, direction == 1 ? "right" : "left");

            print(tree.left, tree.key, -1);
            print(tree.right, tree.key, 1);
        }
    }

    public void print() {
        if (mRoot != null)
            print(mRoot, mRoot.key, 0);
    }

    private static int arr[] = {1, 2, 3, 4};

    public static void main(String[] args) {
        int i;
        AVLTree<Integer> tree = new AVLTree<Integer>();

        System.out.printf("== 依次添加: ");
        for (i = 0; i < arr.length; i++) {
            System.out.printf("%d ", arr[i]);
            tree.insert(arr[i]);
        }
        tree.print();


    }

}