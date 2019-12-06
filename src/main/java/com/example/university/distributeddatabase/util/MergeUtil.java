package com.example.university.distributeddatabase.util;

import java.util.ArrayList;

public class MergeUtil {

    public static <T extends Comparable<? super T>> void mergeSortedArray(ArrayList<ArrayList<T>> lists, ArrayList<T> result) {
        int totalSize = 0; // every element in the set
        for (ArrayList<T> l : lists) {
            totalSize += l.size();
        }

        ArrayList<T> lowest;

        while (result.size() < totalSize) { // while we still have something to add
            lowest = null;

            for (ArrayList<T> l : lists) {
                if (!l.isEmpty()) {
                    if (lowest == null) {
                        lowest = l;
                    } else if (l.get(0).compareTo(lowest.get(0)) <= 0) {
                        lowest = l;
                    }
                }
            }

            result.add(lowest.get(0));
            lowest.remove(0);
        }
    }
}
