package com.example.university.distributeddatabase;

import com.example.university.distributeddatabase.util.MergeSort;

import java.util.List;
import java.util.concurrent.Callable;

public class PartOFList implements Callable<MergeSort> {
    public List<Integer> unSortList;

    public  PartOFList(List<Integer> unSortList) {
        this.unSortList = unSortList;
    }
    @Override
    public MergeSort call() {
        MergeSort ms = new MergeSort(unSortList);
        ms.sortGivenArray();
        return ms;
    }
}
