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

import com.example.university.distributeddatabase.util.MergeKSortedArrays;
import com.example.university.distributeddatabase.util.Utils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class TestExecutor {

    @Test
    public void InvokeAllDemo() {
        try {
            ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            int availableProcessors = Runtime.getRuntime().availableProcessors();
            ArrayList<Integer> unsortedArray = Utils.getRandomNumbers();
            ArrayList<List<Integer>> arrayListsDivided = new ArrayList<>();
            int gam = unsortedArray.size() / availableProcessors;
            for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
                arrayListsDivided.add(i, unsortedArray.subList(gam * i, gam * (i + 1)));
            }
            List<PartOFList> futureList = new ArrayList<>();
            for (List<Integer> arrayList : arrayListsDivided) {
                futureList.add(new PartOFList(arrayList));
            }
            service.invokeAll(futureList);
            MergeKSortedArrays tester = new MergeKSortedArrays();
            int[][] input = new int[availableProcessors][availableProcessors];
            for (int j = 0; j < Runtime.getRuntime().availableProcessors(); j++) {
                input[j] = Utils.toIntArray(arrayListsDivided.get(j));
            }
            System.out.println(Arrays.toString(tester.mergeKSortedArrays(input)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
