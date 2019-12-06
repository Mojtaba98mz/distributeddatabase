package com.example.university.distributeddatabase.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class MergerThread implements Callable<Long> {
    private ArrayList<ArrayList<Integer>> toMergeLists;
    private Integer threadNumber;
    private ArrayList<Integer> result;

    public MergerThread(ArrayList<ArrayList<Integer>> toMergeLists, int threadNumber, ArrayList<Integer> result) {
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
