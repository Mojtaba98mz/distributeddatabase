package com.example.university.distributeddatabase.controller;

import com.example.university.distributeddatabase.util.MergeSort;
import com.example.university.distributeddatabase.util.MergeUtil;
import com.example.university.distributeddatabase.util.Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ParallelSort {

    @RequestMapping(value = "/mergeAllSort", method = RequestMethod.GET)
    public String mergeAllSort(@RequestParam("core") int core) {

        ArrayList<ArrayList<Integer>> dividedNumbers = new ArrayList<>();

        ArrayList<Integer> randomNumbers = Utils.getRandomNumbers();

        int divide = randomNumbers.size() / core;

        for (int i = 0; i < randomNumbers.size(); i += divide) {
            ArrayList<Integer> integers = new ArrayList<>(randomNumbers.subList(i, i + divide));
            dividedNumbers.add(integers);
        }

        for (int i = 0; i < core; i++) {
            int finalI = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    MergeSort mergeSort = new MergeSort(dividedNumbers.get(finalI));
                    mergeSort.sortGivenArray();
                }
            };
            Thread thread = new Thread(runnable, "Thread-" + i);
            thread.start();
        }

        List<Integer> merged = MergeUtil.mergeSortedArray(dividedNumbers);
        System.out.println(merged);
        return "Merge All Sort";
    }
}
