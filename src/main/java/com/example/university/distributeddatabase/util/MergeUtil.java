package com.example.university.distributeddatabase.util;

import java.util.ArrayList;
import java.util.LinkedList;

public class MergeUtil {

    public static void mergeSortedArray(ArrayList<LinkedList<Integer>> lists, LinkedList<Integer> result) {
        int totalSize = 0; // every element in the set
        for (LinkedList<Integer> l : lists) {
            totalSize += l.size();
        }
        LinkedList<Integer> lowest;
        while (result.size() < totalSize) { // while we still have something to add
            lowest = null;
            for (LinkedList<Integer> l : lists) {
                if (!l.isEmpty()) {
                    if (lowest == null) {
                        lowest = l;
                    } else if (l.getFirst().compareTo(lowest.getFirst()) <= 0) {
                        lowest = l;
                    }
                }
            }
            result.addLast(lowest.removeFirst());
        }
    }
}
