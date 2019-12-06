package com.example.university.distributeddatabase.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class SorterThread implements Callable<Long> {
    private ArrayList<Integer> unSortList;
    private Integer threadNumber;

    public SorterThread(ArrayList<Integer> unSortList, int threadNumber) {
        this.unSortList = unSortList;
        this.threadNumber = threadNumber;
    }

    @Override
    public Long call() {
        Instant start = Instant.now();
        MergeSort ms = new MergeSort(unSortList);
        ms.sortGivenArray();
        Instant finish = Instant.now();
        return Duration.between(start, finish).toMillis();
    }
}
