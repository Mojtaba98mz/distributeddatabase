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
import com.example.university.distributeddatabase.util.MergeSort;
import com.example.university.distributeddatabase.util.Utils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestExecutor {
    @Test
    public void test_runIncreasePoolSize() throws Exception {
        ArrayList<Integer> unsortedArray = Utils.getRandomNumbers();
        MergeSort ms = new MergeSort(unsortedArray);
        ArrayList<List<Integer>> arrayLists = new ArrayList<>();
        arrayLists.add(unsortedArray.subList(0, 250));
        arrayLists.add(unsortedArray.subList(250, 500));
        arrayLists.add(unsortedArray.subList(500, 750));
        arrayLists.add(unsortedArray.subList(750, 1000));

        ExecutorService executor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() + 1);
        int i = 1;
        for (List<Integer> arrayList : arrayLists) {
            executor.execute(
                    new SampleSort(arrayList, i));
            i++;
        }
    }
}
