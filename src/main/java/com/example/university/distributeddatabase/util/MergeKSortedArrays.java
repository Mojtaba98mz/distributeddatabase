package com.example.university.distributeddatabase.util;

import java.util.PriorityQueue;

public class MergeKSortedArrays {
    private class HeapNode {
        public int arrayNum;
        public int index;
        public int value;

        public HeapNode(int arrayNum, int index, int value) {
            this.arrayNum = arrayNum;
            this.index = index;
            this.value = value;
        }
    }

    public int[] mergeKSortedArrays(int[][] arrays) {

        if (arrays == null) return null;

        PriorityQueue<HeapNode> minHeap =
                new PriorityQueue<>(arrays.length,
                        (HeapNode a, HeapNode b) -> a.value - b.value);

        int size = 0;
        for (int i = 0; i < arrays.length; i++) {
            size += arrays[i].length;
        }
        int[] result = new int[size]; // k * n

        //add first elements in the array to this heap
        for (int i = 0; i < arrays.length; i++) {
            minHeap.add(new HeapNode(i, 0, arrays[i][0]));
        }

        //Complexity O(n * k * log k)
        for (int i = 0; i < size; i++) {
            //Take the minimum value and put into result
            HeapNode node = minHeap.poll();

            if (node != null) {
                result[i] = node.value;
                if (node.index + 1 < arrays[node.arrayNum].length) {
                    //Complexity of O(log k)
                    minHeap.add(new HeapNode(node.arrayNum,
                            node.index + 1,
                            arrays[node.arrayNum][node.index + 1]));
                }
            }
        }
        return result;
    }
}
