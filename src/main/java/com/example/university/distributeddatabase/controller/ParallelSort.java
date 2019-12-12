package com.example.university.distributeddatabase.controller;

import com.example.university.distributeddatabase.pojo.CoreTimePojo;
import com.example.university.distributeddatabase.util.MergerThread;
import com.example.university.distributeddatabase.util.SorterThread;
import com.example.university.distributeddatabase.util.Utils;
import org.springframework.web.bind.annotation.*;
import static com.example.university.distributeddatabase.util.Constant.MAX_NUMBER;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

@CrossOrigin
@RestController
public class ParallelSort {

    @RequestMapping(value = "/mergeAllSort", method = RequestMethod.GET)
    public List<CoreTimePojo> mergeAllSort(@RequestParam("core") int core) throws ExecutionException, InterruptedException {
        ArrayList<ArrayList<Integer>> dividedArrayListNumbers = new ArrayList<>();
        ArrayList<LinkedList<Integer>> dividedLinkedListNumbers = new ArrayList<>();
        ArrayList<Integer> randomNumbers = Utils.getRandomNumbers();
        int divide = randomNumbers.size() / core;
        for (int i = 0; i < randomNumbers.size(); i += divide) {
            ArrayList<Integer> integers = new ArrayList<>(randomNumbers.subList(i, i + divide));
            dividedArrayListNumbers.add(integers);
        }
        ExecutorService pool = Executors.newFixedThreadPool(core);
        List<CoreTimePojo> coreTimePojos = new ArrayList<>();
        List<Callable<Long>> callables = new ArrayList<>();
        for (int i = 0; i < core; i++) {
            Callable<Long> callable = new SorterThread(dividedArrayListNumbers.get(i), i);
            callables.add(callable);
        }
        List<Future<Long>> futures = pool.invokeAll(callables);
        for (int i = 0; i < core; i++) {
            coreTimePojos.add(new CoreTimePojo(i, futures.get(i).get()));
            LinkedList<Integer> integers = new LinkedList<>(dividedArrayListNumbers.get(i));
            dividedLinkedListNumbers.add(integers);
        }
        LinkedList<Integer> result = new LinkedList<>();
        Callable<Long> callable = new MergerThread(dividedLinkedListNumbers, 1, result);
        Future<Long> future = pool.submit(callable);
        Long mergeTime = future.get();
        coreTimePojos.get(0).setExecutionTime(coreTimePojos.get(0).getExecutionTime() + mergeTime);
        return coreTimePojos;
    }

    @RequestMapping(value = "/redistributionMergeAllSort", method = RequestMethod.GET)
    public List<CoreTimePojo> redistributionMergeAllSort(@RequestParam("core") int core) throws ExecutionException, InterruptedException {
        ArrayList<ArrayList<Integer>> dividedArrayListNumbers = new ArrayList<>();
        ArrayList<Integer> randomNumbers = Utils.getRandomNumbers();
        int divide = randomNumbers.size() / core;
        for (int i = 0; i < randomNumbers.size(); i += divide) {
            ArrayList<Integer> integers = new ArrayList<>(randomNumbers.subList(i, i + divide));
            dividedArrayListNumbers.add(integers);
        }
        ExecutorService pool = Executors.newFixedThreadPool(core);
        List<CoreTimePojo> coreTimePojos = new ArrayList<>();
        List<Callable<Long>> callables = new ArrayList<>();
        for (int i = 0; i < core; i++) {
            Callable<Long> callable = new SorterThread(dividedArrayListNumbers.get(i), i);
            callables.add(callable);
        }
        List<Future<Long>> futures = pool.invokeAll(callables);
        ArrayList<ArrayList<Integer>> redistributedArray = new ArrayList<>();
        for (int i = 0; i < core; i++) {
            ArrayList<Integer> redisArray = new ArrayList<>(divide);
            redistributedArray.add(redisArray);
        }
        int range = MAX_NUMBER / core;
        Long redistributedTime = 0l;
        for (int i = 0; i < core; i++) {
            coreTimePojos.add(new CoreTimePojo(i, futures.get(i).get()));
            Instant start = Instant.now();
            for (Integer integer : dividedArrayListNumbers.get(i)) {
                int index = integer / range;
                redistributedArray.get(index).add(integer);
            }
            Instant finish = Instant.now();
            redistributedTime += Duration.between(start, finish).toMillis();
        }
        callables = new ArrayList<>();
        for (int i = 0; i < core; i++) {
            Callable<Long> callable = new SorterThread(redistributedArray.get(i), i);
            callables.add(callable);
        }
        futures = pool.invokeAll(callables);
        for (int i = 0; i < core; i++) {
            Long time = futures.get(i).get();
            coreTimePojos.get(i).setExecutionTime(coreTimePojos.get(i).getExecutionTime() + time);
        }
        coreTimePojos.get(0).setExecutionTime(coreTimePojos.get(0).getExecutionTime() + redistributedTime);
        return coreTimePojos;
    }

    @RequestMapping(value = "/parallelPartitionedSort", method = RequestMethod.GET)
    public List<CoreTimePojo> parallelPartitionedSort(@RequestParam("core") int core) throws ExecutionException, InterruptedException {
        ArrayList<ArrayList<Integer>> dividedArrayListNumbers = new ArrayList<>();
        ArrayList<Integer> randomNumbers = Utils.getRandomNumbers();
        int divide = randomNumbers.size() / core;
        for (int i = 0; i < randomNumbers.size(); i += divide) {
            ArrayList<Integer> integers = new ArrayList<>(randomNumbers.subList(i, i + divide));
            dividedArrayListNumbers.add(integers);
        }
        ExecutorService pool = Executors.newFixedThreadPool(core);
        List<CoreTimePojo> coreTimePojos = new ArrayList<>();
        List<Callable<Long>> callables = new ArrayList<>();
        ArrayList<ArrayList<Integer>> redistributedArray = new ArrayList<>();
        for (int i = 0; i < core; i++) {
            ArrayList<Integer> redisArray = new ArrayList<>(divide);
            redistributedArray.add(redisArray);
        }
        int range = MAX_NUMBER / core;
        Long redistributedTime = 0l;
        for (int i = 0; i < core; i++) {
            Instant start = Instant.now();
            for (Integer integer : dividedArrayListNumbers.get(i)) {
                int index = integer / range;
                redistributedArray.get(index).add(integer);
            }
            Instant finish = Instant.now();
            redistributedTime += Duration.between(start, finish).toMillis();
        }
        for (int i = 0; i < core; i++) {
            Callable<Long> callable = new SorterThread(redistributedArray.get(i), i);
            callables.add(callable);
        }
        List<Future<Long>> futures = pool.invokeAll(callables);
        for (int i = 0; i < core; i++) {
            Long time = futures.get(i).get();
            coreTimePojos.add(new CoreTimePojo(i, time));
        }
        for (ArrayList<Integer> integers : redistributedArray) {
            System.out.println(integers);
        }
        coreTimePojos.get(0).setExecutionTime(coreTimePojos.get(0).getExecutionTime() + redistributedTime);
        return coreTimePojos;
    }
}
