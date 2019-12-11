package com.example.university.distributeddatabase.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Callable;

public class MergerThread implements Callable<Long> {
    private ArrayList<LinkedList<Integer>> toMergeLists;
    private Integer threadNumber;
    private LinkedList<Integer> result;

    public MergerThread(ArrayList<LinkedList<Integer>> toMergeLists, int threadNumber, LinkedList<Integer> result) {
        this.toMergeLists = toMergeLists;
        this.threadNumber = threadNumber;
        this.result = result;
    }

    @Override
    public Long call() {
        Instant start = Instant.now();
        MergeUtil.mergeSortedArray(toMergeLists,result);
        Instant finish = Instant.now();
        return Duration.between(start, finish).toMillis();
    }
}
