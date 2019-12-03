package com.example.university.distributeddatabase.controller;

import com.example.university.distributeddatabase.util.MergeSort;
import com.example.university.distributeddatabase.util.MergeUtil;
import com.example.university.distributeddatabase.util.SorterThread;
import com.example.university.distributeddatabase.util.Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.*;

@RestController
public class ParallelSort {

    @RequestMapping(value = "/mergeAllSort", method = RequestMethod.GET)
    public String mergeAllSort(@RequestParam("core") int core) throws ExecutionException, InterruptedException {

        ArrayList<ArrayList<Integer>> dividedNumbers = new ArrayList<>();

        ArrayList<Integer> randomNumbers = Utils.getRandomNumbers();

        int divide = randomNumbers.size() / core;

        for (int i = 0; i < randomNumbers.size(); i += divide) {
            ArrayList<Integer> integers = new ArrayList<>(randomNumbers.subList(i, i + divide));
            dividedNumbers.add(integers);
        }

        ExecutorService pool = Executors.newFixedThreadPool(core + 1);
        Map<Integer, Future<Long>> executionTime = new HashMap<>();

        for (int i = 0; i < core; i++) {
            Callable<Long> callable = new SorterThread(dividedNumbers.get(i), i);
            Future<Long> future = pool.submit(callable);
            executionTime.put(i, future);
        }

        for (Integer integer : executionTime.keySet()) {
            System.out.println(integer + " " + executionTime.get(integer).get());
        }

        List<Integer> merged = MergeUtil.mergeSortedArray(dividedNumbers);

        System.out.println(merged);
        return "Merge All Sort";
    }
}
