package com.example.university.distributeddatabase.controller;

import com.example.university.distributeddatabase.util.MergeSort;
import com.example.university.distributeddatabase.util.Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class ParallelSort {

    @RequestMapping(value = "/mergeAllSort", method = RequestMethod.GET)
    public String mergeAllSort(@RequestParam("core") int core) {
        ArrayList<Integer> randomNumbers = Utils.getRandomNumbers();

        int divide = randomNumbers.size() / core;


        MergeSort mergeSort = new MergeSort(randomNumbers);
        mergeSort.sortGivenArray();
        return "Merge All Sort";
    }

}
