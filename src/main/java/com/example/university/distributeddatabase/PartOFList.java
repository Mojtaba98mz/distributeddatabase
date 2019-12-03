package com.example.university.distributeddatabase;

import com.example.university.distributeddatabase.util.MergeSort;

import java.util.List;
import java.util.concurrent.Callable;

public class PartOFList implements Callable<MergeSort> {
    public List<Integer> unSortList;
    public int threadCount=0;

    public  PartOFList(List<Integer> unSortList,int threadCount) {
        this.unSortList = unSortList;
        this.threadCount = threadCount;
    }

//    @Override
//    public void run() {
//        System.out.println("t"+threadCount +"===>start");
//        MergeSort ms = new MergeSort(unSortList);
//        ms.sortGivenArray();
//        System.out.println("t"+threadCount +"===>end");
//    }

    @Override
    public MergeSort call() throws Exception {
        System.out.println("t"+threadCount +"===>start");
        MergeSort ms = new MergeSort(unSortList);
        ms.sortGivenArray();
        System.out.println("t"+threadCount +"===>end");
        return ms;
    }
}
