/*
 * Since: December, 2014
 * Author: gvenzl
 * Name: TestExecutor.java
 * Description:
 *
 * Copyright (c) 2018 Gerald Venzl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.university.distributeddatabase;

import com.example.university.distributeddatabase.util.Executor;
import com.example.university.distributeddatabase.util.MergeKSortedArrays;
import com.example.university.distributeddatabase.util.MergeSort;
import com.example.university.distributeddatabase.util.Utils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class TestExecutor {

    @Test
    public void InvokeAllDemo() throws InterruptedException {
        List<Future<MergeSort>> futures;
        System.out.println("creating service");
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ArrayList<Integer> unsortedArray = Utils.getRandomNumbers();
        ArrayList<List<Integer>> arrayLists = new ArrayList<>();
        arrayLists.add(unsortedArray.subList(0, 250));
        arrayLists.add(unsortedArray.subList(250, 500));
        arrayLists.add(unsortedArray.subList(500, 750));
        arrayLists.add(unsortedArray.subList(750, 1000));
        List<PartOFList> futureList = new ArrayList<PartOFList>();
        int i=1;
        for (List<Integer> arrayList : arrayLists) {
            PartOFList myCallable = new PartOFList(arrayList, i);
            futureList.add(myCallable);
            i++;
        }
        System.out.println("Start");
        try {
            futures= service.invokeAll(futureList);
        } catch (Exception err) {
            err.printStackTrace();
        }

        System.out.println("Completed");
        MergeKSortedArrays tester = new MergeKSortedArrays();
        int[] a1 = Utils.toIntArray(arrayLists.get(0));
        int[] a2 = Utils.toIntArray(arrayLists.get(1));
        int[] a3 = Utils.toIntArray(arrayLists.get(2));
        int[] a4 = Utils.toIntArray(arrayLists.get(3));
        int[][] input = {a1, a2, a3, a4};
        System.out.println(Arrays.toString(tester.mergeKSortedArrays(input)));
    }


    @Test
    public void test_runIncreasePoolSize() throws Exception {
        ArrayList<Integer> unsortedArray = Utils.getRandomNumbers();
        ArrayList<List<Integer>> arrayLists = new ArrayList<>();
        arrayLists.add(unsortedArray.subList(0, 250));
        arrayLists.add(unsortedArray.subList(250, 500));
        arrayLists.add(unsortedArray.subList(500, 750));
        arrayLists.add(unsortedArray.subList(750, 1000));
        ExecutorService executor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() + 1);
        int i = 1;
        for (List<Integer> arrayList : arrayLists) {
            executor.submit(
                    new PartOFList(arrayList, i));
            i++;
//            executor.invokeAll();
        }
        int[][] result;
        MergeKSortedArrays tester = new MergeKSortedArrays();
        int[] a1 = Utils.toIntArray(arrayLists.get(0));
        int[] a2 = Utils.toIntArray(arrayLists.get(1));
        int[] a3 = Utils.toIntArray(arrayLists.get(2));
        int[] a4 = Utils.toIntArray(arrayLists.get(3));
        int[][] input = {a1, a2, a3, a4};
        executor.submit(() -> {
            int[] ints = tester.mergeKSortedArrays(input);
            System.out.println(Arrays.toString(ints));
        });
    }
}
