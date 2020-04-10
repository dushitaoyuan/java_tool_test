package com.taoyuanx.sortexample;

import com.taoyuanx.sortexample.sort.Sort;

import javax.swing.tree.TreeNode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dushitaoyuan
 * @date 2020/4/7
 * @desc 堆排序
 * 将初始待排序关键字序列(R1,R2….Rn)构建成大顶堆，此堆为初始的无序区；
 * 将堆顶元素R[1]与最后一个元素R[n]交换，此时得到新的无序区(R1,R2,……Rn-1)和新的有序区(Rn),且满足R[1,2…n-1]<=R[n]；
 * 由于交换后新的堆顶R[1]可能违反堆的性质，因此需要对当前无序区(R1,R2,……Rn-1)调整为新堆，然后再次将R[1]与无序区最后一个元素交换，得到新的无序区(R1,R2….Rn-2)和新的有序区(Rn-1,Rn)。不断重复此过程直到有序区的元素个数为n-1，则整个排序过程完成。
 * 1. 若array[0，...，n-1]表示一颗完全二叉树的顺序存储模式，则双亲节点指针和孩子结点指针之间的内在关系如下：
 * <p>
 * 　　任意一节点指针 i：父节点：i==0 ? null : (i-1)/2
 * <p>
 * 　　　　　　　　　　  左孩子：2*i + 1
 * <p>
 * 　　　　　　　　　　  右孩子：2*i + 2
 * <p>
 * 2. 堆的定义：n个关键字序列array[0，...，n-1]，当且仅当满足下列要求：(0 <= i <= (n-1)/2)
 * <p>
 * 　　　　　　① array[i] <= array[2*i + 1] 且 array[i] <= array[2*i + 2]； 称为小根堆；
 * <p>
 * 　　　　　　② array[i] >= array[2*i + 1] 且 array[i] >= array[2*i + 2]； 称为大根堆；
 */
public class HeapSort<T> implements Sort<T> {


    @Override
    public T[] sort(T[] array, Comparator comparator) {
        /**
         * 假设 该数组为一个最大堆的宽序存储
         *  则根据 公式:array.length=2*i + 2 可计算出 最后一个非叶子节点的位置
         */
        int nodeIndex = (array.length - 2) / 2;
        for (; nodeIndex >= 0; nodeIndex--) {
            /**
             * 由下至上调整
             */
            heapAdjust(array, nodeIndex, array.length, comparator);
        }
        //将堆顶的元素和最后一个元素交换，并重新调整堆
        for (int i = array.length - 1; i > 0; i--) {
            T temp = array[i];
            array[i] = array[0];
            array[0] = temp;
            heapAdjust(array, 0, i, comparator);
        }
        return array;
    }


    /**
     * 堆调整
     */
    private void heapAdjust(T[] array, int nodeIndex, int length, Comparator comparator) {
        T nodeRoot = array[nodeIndex];
        /**
         * 左右节点index
         */
        int leftNodeIndex = 2 * nodeIndex + 1;
        int rightNodeIndex = 2 * nodeIndex + 2;
        /**
         * 计算最大值所在 index
         */
        int maxIndex = nodeIndex;
        if (leftNodeIndex < length && comparator.compare(array[leftNodeIndex], array[maxIndex]) > 0) {
            maxIndex = leftNodeIndex;
        }
        if (rightNodeIndex < length && comparator.compare(array[rightNodeIndex], array[maxIndex]) > 0) {
            maxIndex = rightNodeIndex;
        }
        /**
         * 如果最大值不是根节点,交换根节点与最大值位置
         * 并递归调整原最大值所在的子树
         */
        if (comparator.compare(nodeRoot, array[maxIndex]) != 0) {
            array[nodeIndex] = array[maxIndex];
            array[maxIndex] = nodeRoot;
            heapAdjust(array, maxIndex, length, comparator);
        }

    }


    private <T> void writeArray(TreeNode<T> currNode, int rowIndex, int columnIndex, String[][] res, int treeDepth) {
        if (currNode == null) {
            return;
        }
        // 先将当前节点保存到二维数组中
        res[rowIndex][columnIndex] = String.valueOf(currNode.rootNode);

        int nodeDepth = currNode.getDepth();

        if (nodeDepth == treeDepth) {
            return;
        }
        /**
         * 每行的元素间隔
         */
        int gap = treeDepth - nodeDepth - 1;

        /**
         * 填充左右子节点及其连接符
         */
        if (Objects.nonNull(currNode.leftNode)) {
            res[rowIndex + 1][columnIndex - gap] = "/";
            writeArray(currNode.leftNode, rowIndex + 2, columnIndex - gap * 2, res, treeDepth);
        }

        if (Objects.nonNull(currNode.rightNode)) {
            res[rowIndex + 1][columnIndex + gap] = "\\";
            writeArray(currNode.rightNode, rowIndex + 2, columnIndex + gap * 2, res, treeDepth);
        }
    }


    public <T> void show(TreeNode<T> root) {
        if (root == null) {
            return;
        }
        // 得到树的深度
        int treeDepth = getTreeDepth(root);

        /**
         * 二维数组存储整棵二叉树(包含连接符和空格)
         * hight(数组高度)=2*treeDepth  - 1
         * width(数组宽度)=2^(treeDepth-1)*3+1
         */
        int hight = 2 * treeDepth - 1;
        int width = Double.valueOf(Math.pow(2, treeDepth - 1)).intValue() * 3 + 1;
        String[][] res = new String[hight][width];
        System.out.println("树高:" + treeDepth);
        for (int i = 0; i < hight; i++) {
            for (int j = 0; j < width; j++) {
                res[i][j] = " ";

            }
        }
        /**
         * 根在数组中的坐标(0,width/2)
         */
        writeArray(root, 0, width / 2, res, treeDepth);
        /**
         * 按行 打印数组
         */
        Arrays.stream(res).forEach(row -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < row.length; i++) {
                sb.append(row[i]);
                if (row[i].length() > 1 && i <= row.length - 1) {
                    i += row[i].length() > 4 ? 2 : row[i].length() - 1;
                }
            }
            System.out.println(sb.toString());
        });

    }

    public static class TreeNode<T> {
        private T rootNode;
        private TreeNode<T> leftNode;
        private TreeNode<T> rightNode;
        private int depth;

        public TreeNode(T rootNode, int depth) {
            this.rootNode = rootNode;
            this.depth = depth;
        }

        public T getRootNode() {
            return rootNode;
        }

        public void setRootNode(T rootNode) {
            this.rootNode = rootNode;
        }

        public TreeNode<T> getLeftNode() {
            return leftNode;
        }

        public void setLeftNode(TreeNode<T> leftNode) {
            this.leftNode = leftNode;
        }

        public TreeNode<T> getRightNode() {
            return rightNode;
        }

        public void setRightNode(TreeNode<T> rightNode) {
            this.rightNode = rightNode;
        }

        public int getDepth() {
            return depth;
        }

        public void setDepth(int depth) {
            this.depth = depth;
        }
    }

    /**
     * 数组转 堆
     */
    public TreeNode<T> arrayToMaxHeap(T[] array, Comparator comparator) {
        T[] maxHeapArray = sort(array, comparator);
        TreeNode<T> treeNode = new TreeNode<>(maxHeapArray[0], 0);
        setLeftAndRight(maxHeapArray, treeNode, 0);
        show(treeNode);
        return treeNode;
    }

    private void setLeftAndRight(T[] array, TreeNode<T> node, int nodeIndex) {
        /**
         * 左右节点index
         */

        int leftNodeIndex = 2 * nodeIndex + 1;
        int rightNodeIndex = 2 * nodeIndex + 2;
        TreeNode<T> leftNode = null, rightNode = null;
        int nextDepth = node.getDepth() + 1;
        if (leftNodeIndex < array.length) {
            leftNode = new TreeNode<>(array[leftNodeIndex], nextDepth);
            node.setLeftNode(leftNode);
            setLeftAndRight(array, leftNode, leftNodeIndex);
        }
        if (rightNodeIndex < array.length) {
            rightNode = new TreeNode<>(array[rightNodeIndex], nextDepth);
            node.setRightNode(rightNode);
            setLeftAndRight(array, rightNode, rightNodeIndex);
        }
    }

    private int getTreeDepth(TreeNode root) {
        return root == null ? 0 : (1 + Math.max(getTreeDepth(root.leftNode), getTreeDepth(root.rightNode)));
    }

    private int getMaxNodeLength(TreeNode root) {
        if (Objects.isNull(root)) {
            return 0;
        }
        int nodeLen = root == null ? 1 : String.valueOf(root.rootNode).length();
        int leftNodeLen = getMaxNodeLength(root.leftNode);
        int rightNodeLen = getMaxNodeLength(root.rightNode);
        return Math.max(Math.max(nodeLen, leftNodeLen), rightNodeLen);
    }

    private String repeat(String str, int num) {
        if (num == 0) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < num; i++) {
            buf.append(str);
        }
        return buf.toString();
    }

    private String padding(String str, int strLen, String paddingStr) {
        StringBuilder buf = new StringBuilder();
        int midPaddingLen = (strLen - str.length()) / 2;
        for (int i = 0; i < midPaddingLen; i++) {
            buf.append(paddingStr);
        }
        buf.append(str);
        for (int i = 0; i < midPaddingLen; i++) {
            buf.append(paddingStr);
        }
        if (buf.length() != strLen) {
            buf.append(paddingStr);
        }
        return buf.toString();
    }
}
