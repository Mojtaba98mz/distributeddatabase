package com.example.university.distributeddatabase.controller;

import com.example.university.distributeddatabase.pojo.CoreTimePojo;
import com.example.university.distributeddatabase.util.MergerThread;
import com.example.university.distributeddatabase.util.SorterThread;
import com.example.university.distributeddatabase.util.Utils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@CrossOrigin
@RestController
public class ParallelSort {

    @RequestMapping(value = "/mergeAllSort", method = RequestMethod.GET)
    public List<CoreTimePojo> mergeAllSort(@RequestParam("core") int core) throws ExecutionException, InterruptedException {

        ArrayList<ArrayList<Integer>> dividedNumbers = new ArrayList<>();

        ArrayList<Integer> randomNumbers = Utils.getRandomNumbers();

        int divide = randomNumbers.size() / core;

        for (int i = 0; i < randomNumbers.size(); i += divide) {
            ArrayList<Integer> integers = new ArrayList<>(randomNumbers.subList(i, i + divide));
            dividedNumbers.add(integers);
        }

        // why core+1
        ExecutorService pool = Executors.newFixedThreadPool(core + 1);

        List<CoreTimePojo> coreTimePojos = new ArrayList<>();

        List<Callable<Long>> callables = new ArrayList<>();
        for (int i = 0; i < core; i++) {
            Callable<Long> callable = new SorterThread(dividedNumbers.get(i), i);
            callables.add(callable);
        }
        List<Future<Long>> futures = pool.invokeAll(callables);

        for (int i = 0; i < core; i++) {
            coreTimePojos.add(new CoreTimePojo(i, futures.get(i).get()));
        }

        ArrayList<Integer> result = new ArrayList<>();
        Callable<Long> callable = new MergerThread(dividedNumbers, 1, result);
        Future<Long> future = pool.submit(callable);
        Long mergeTime = future.get();
        coreTimePojos.get(0).setExecutionTime(coreTimePojos.get(0).getExecutionTime() + mergeTime);

        return coreTimePojos;
    }
}
