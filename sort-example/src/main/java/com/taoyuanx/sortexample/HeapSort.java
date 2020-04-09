package com.taoyuanx.sortexample;

import com.taoyuanx.sortexample.sort.Sort;

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


    public static class TreeNode<T> {
        private T rootNode;
        private TreeNode<T> leftNode;
        private TreeNode<T> rightNode;
        private int deep;

        public TreeNode(T rootNode, int deep) {
            this.rootNode = rootNode;
            this.deep = deep;
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

        public int getDeep() {
            return deep;
        }

        public void setDeep(int deep) {
            this.deep = deep;
        }


    }

    /**
     * 数组转 堆
     */
    public TreeNode<T> arrayToMaxHeap(T[] array, Comparator comparator) {
        T[] maxHeapArray = sort(array, comparator);
        TreeNode<T> treeNode = new TreeNode<>(maxHeapArray[0], 0);
        LevelMap<T> levelMap = new LevelMap<>();
        setLeftAndRight(maxHeapArray, treeNode, 0, levelMap);
        printNode(levelMap);
        return treeNode;
    }

    private void setLeftAndRight(T[] array, TreeNode<T> node, int nodeIndex, LevelMap<T> levelMap) {
        /**
         * 左右节点index
         */
        levelMap.put(node.getDeep(), node);
        int leftNodeIndex = 2 * nodeIndex + 1;
        int rightNodeIndex = 2 * nodeIndex + 2;
        TreeNode<T> leftNode = null, rightNode = null;
        int nextDeep = node.getDeep() + 1;
        if (leftNodeIndex < array.length) {
            leftNode = new TreeNode<>(array[leftNodeIndex], nextDeep);
            node.setLeftNode(leftNode);
            setLeftAndRight(array, leftNode, leftNodeIndex, levelMap);
        }
        if (rightNodeIndex < array.length) {
            rightNode = new TreeNode<>(array[rightNodeIndex], nextDeep);
            node.setRightNode(rightNode);
            setLeftAndRight(array, rightNode, rightNodeIndex, levelMap);
        }
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

    public void printNode(LevelMap<T> levelMap) {
        int maxLevel = levelMap.getMap().size() - 1;

    }

    private String repeat(String str, Integer repeatNum) {
        if (repeatNum == 0) {
            return "";
        }
        StringBuilder buf = new StringBuilder(str);
        for (int i = 0; i < repeatNum; i++) {
            buf.append(str);
        }
        return buf.toString();
    }

    static class LevelMap<T> {
        private Map<Integer, List<TreeNode<T>>> map = new HashMap<>();

        public Map<Integer, List<TreeNode<T>>> getMap() {
            return map;
        }

        public void put(int level, TreeNode<T> node) {
            if (!map.containsKey(level)) {
                map.put(level, new ArrayList<>());
            }
            map.get(level).add(node);
        }

    }

}
