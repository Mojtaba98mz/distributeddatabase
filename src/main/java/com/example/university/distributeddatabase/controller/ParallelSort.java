package com.example.university.distributeddatabase.controller;

import com.example.university.distributeddatabase.pojo.CoreTimePojo;
import com.example.university.distributeddatabase.pojo.MyTuple;
import com.example.university.distributeddatabase.service.WordCountService;
import com.example.university.distributeddatabase.util.MergerThread;
import com.example.university.distributeddatabase.util.SorterThread;
import com.example.university.distributeddatabase.util.Utils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import scala.Tuple2;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import static com.example.university.distributeddatabase.util.Constant.MAX_NUMBER;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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
            if (!(i + divide > randomNumbers.size())) {
                ArrayList<Integer> integers = new ArrayList<>(randomNumbers.subList(i, i + divide));
                dividedArrayListNumbers.add(integers);
            } else {
                ArrayList<Integer> integers = new ArrayList<>(randomNumbers.subList(i, randomNumbers.size()));
                dividedArrayListNumbers.add(integers);
            }
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
            if (!(i + divide > randomNumbers.size())) {
                ArrayList<Integer> integers = new ArrayList<>(randomNumbers.subList(i, i + divide));
                dividedArrayListNumbers.add(integers);
            } else {
                ArrayList<Integer> integers = new ArrayList<>(randomNumbers.subList(i, randomNumbers.size()));
                dividedArrayListNumbers.add(integers);
            }
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
        int range = Math.round((float) MAX_NUMBER / core);
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
        int range = Math.round((float) MAX_NUMBER / core);
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

    @RequestMapping(value = "/binaryMergeSort", method = RequestMethod.GET)
    public List<CoreTimePojo> binaryMergeSort(@RequestParam("core") int core) throws ExecutionException, InterruptedException {
        ArrayList<ArrayList<Integer>> dividedNumbers = new ArrayList<>();
        ArrayList<Integer> randomNumbers = Utils.getRandomNumbers();
        int divide = randomNumbers.size() / core;
        for (int i = 0; i < randomNumbers.size(); i += divide) {
            if (i + divide > randomNumbers.size()) {
                dividedNumbers.get(dividedNumbers.size() - 1).addAll(randomNumbers.subList(i, randomNumbers.size()));
                break;
            }
            ArrayList<Integer> integers = new ArrayList<>(randomNumbers.subList(i, i + divide));
            dividedNumbers.add(integers);
        }
        ExecutorService pool = Executors.newFixedThreadPool(core + 1);
        List<CoreTimePojo> coreTimePojos = new ArrayList<>();
        List<Callable<Long>> callables = new ArrayList<>();
        for (int i = 0; i < core; i++) {
            Callable<Long> callable = new SorterThread(dividedNumbers.get(i), i);
            callables.add(callable);
        }
        List<Future<Long>> futures = pool.invokeAll(callables);
        for (int i = 0; i < core; i++) {
            coreTimePojos.add(new CoreTimePojo(i, futures.get(i).get(), i));
        }
        LinkedList<Integer> result = new LinkedList<>();
        ArrayList<ArrayList<Integer>> temp = new ArrayList<>();
        double level = Math.log(core) / Math.log(2);
        ArrayList<LinkedList<Integer>> binaryList = new ArrayList<>();
        float coreLevel = core;
        for (int j = 0; j < level; j++) {
            for (int i = 0; i < coreLevel / 2; i++) {
                if (dividedNumbers.size() > i * 2 + 1) {
                    binaryList.add(new LinkedList<>(dividedNumbers.get(i * 2)));
                    binaryList.add(new LinkedList<>(dividedNumbers.get(i * 2 + 1)));
                    Long mergeTime = pool.submit(new MergerThread(binaryList, i * 2, result)).get();
                    coreTimePojos.get(i * 2).setExecutionTime(coreTimePojos.get(i * 2).getExecutionTime() + mergeTime);
                    temp.add(new ArrayList<>(result));
                } else {
                    temp.add(new ArrayList<>(dividedNumbers.get(i * 2)));
                }
                binaryList = new ArrayList<>();
                result = new LinkedList<>();
            }
            coreLevel = coreLevel / 2;
            dividedNumbers = new ArrayList<>(temp);
            temp = new ArrayList<>();

        }
        return coreTimePojos;
    }

    @RequestMapping(value = "/redistributionBinaryMergeSort", method = RequestMethod.GET)
    public List<CoreTimePojo> redistributionBinaryMergeSort(@RequestParam("core") int core) throws ExecutionException, InterruptedException {
        ArrayList<ArrayList<Integer>> dividedNumbers = new ArrayList<>();
        ArrayList<Integer> randomNumbers = Utils.getRandomNumbers();
        int divide = randomNumbers.size() / core;
        for (int i = 0; i < randomNumbers.size(); i += divide) {
            if (i + divide > randomNumbers.size()) {
                dividedNumbers.get(dividedNumbers.size() - 1).addAll(randomNumbers.subList(i, randomNumbers.size()));
                break;
            }
            ArrayList<Integer> integers = new ArrayList<>(randomNumbers.subList(i, i + divide));
            dividedNumbers.add(integers);
        }
        ExecutorService pool = Executors.newFixedThreadPool(core + 1);
        List<CoreTimePojo> coreTimePojos = new ArrayList<>();
        List<Callable<Long>> callables = new ArrayList<>();


        /////////////////////////////////////////////////////////////////
        /*local sort*/
        for (int i = 0; i < core; i++) {
            Callable<Long> callable = new SorterThread(dividedNumbers.get(i), i);
            callables.add(callable);
        }
        List<Future<Long>> futures = pool.invokeAll(callables);
        for (int i = 0; i < core; i++) {
            coreTimePojos.add(new CoreTimePojo(i, futures.get(i).get(), i));
        }


        //////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////
        /*redistributionBinary*/
        ArrayList<ArrayList<Integer>> redistributedArray = new ArrayList<>();
        for (int i = 0; i < core; i++) {
            ArrayList<Integer> redisArray = new ArrayList<>(divide);
            redistributedArray.add(redisArray);
        }
        Long redistributedTime = 0l;

        int range = Math.round((float) MAX_NUMBER / 2);
        for (int i = 0; i < core / 2; i++) {

            Instant start = Instant.now();
            ArrayList<Integer> temp = new ArrayList<>();
            temp.addAll(dividedNumbers.get(2 * i));
            temp.addAll(dividedNumbers.get(2 * i + 1));
            for (Integer integer : temp) {
                int index = integer / range;
                redistributedArray.get(index + 2 * i).add(integer);
            }
            Instant finish = Instant.now();
            redistributedTime += Duration.between(start, finish).toMillis();
            coreTimePojos.get(2 * i).setExecutionTime(coreTimePojos.get(2 * i).getExecutionTime() + redistributedTime);

        }

        ////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////
        /*local sort*/
        for (int i = 0; i < core; i++) {
            Callable<Long> callable = new SorterThread(redistributedArray.get(i), i);
            callables.add(callable);
        }
        List<Future<Long>> futures1 = pool.invokeAll(callables);
        for (int i = 0; i < core; i++) {
            coreTimePojos.get(i).setExecutionTime(coreTimePojos.get(i).getExecutionTime() + futures1.get(i).get());
        }
        ////////////////////////////////////////////////////////////////////////////////


        ////////////////////////////////////////////////////////////////////////////////
        /*redistributionBinary*/
        redistributedTime = 0l;
        ArrayList<ArrayList<Integer>> redistributedArrayAll = new ArrayList<>();
        for (int i = 0; i < core; i++) {
            ArrayList<Integer> redisArray = new ArrayList<>(divide);
            redistributedArrayAll.add(redisArray);
        }


        range = Math.round((float) MAX_NUMBER / core);
        for (int i = 0; i < core; i++) {

            Instant start = Instant.now();
            for (Integer integer : redistributedArray.get(i)) {
                int index = integer / range;
                redistributedArrayAll.get(index).add(integer);
            }
            Instant finish = Instant.now();
            redistributedTime += Duration.between(start, finish).toMillis();
            coreTimePojos.get(i).setExecutionTime(coreTimePojos.get(i).getExecutionTime() + futures1.get(i).get());

        }
        ////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////
        /*local sort*/
        for (int i = 0; i < core; i++) {
            Callable<Long> callable = new SorterThread(redistributedArrayAll.get(i), i);
            callables.add(callable);
        }
        List<Future<Long>> futures2 = pool.invokeAll(callables);
        for (int i = 0; i < core; i++) {
            coreTimePojos.get(i).setExecutionTime(coreTimePojos.get(i).getExecutionTime() + futures2.get(i).get());
        }
        ////////////////////////////////////////////////////////////////////////////////
        return coreTimePojos;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @ResponseBody
    public void downloadFile(@RequestParam("core") int core, HttpServletResponse response) throws IOException, URISyntaxException {
        File file = new File("C:\\new\\test.txt");
        FileUtils.writeStringToFile(file, "3333");
        InputStream in = new FileInputStream(file); // My service to get the stream.
        response.setContentType(MediaType.TEXT_PLAIN);
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
        try {
            IOUtils.copy(in, response.getOutputStream()); //Apache commons IO.
            in.close();
            response.flushBuffer();
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            //log error.
        }
    }

    @Autowired
    WordCountService service;

    @RequestMapping(value = "/sparkSearch", method = RequestMethod.GET)
    public List<MyTuple> spark(@RequestParam("wordSearch") String wordsearach) {
        return service.search(wordsearach);
    }

    @RequestMapping(value = "/sparkCount", method = RequestMethod.GET)
    public List<MyTuple> spark() {
        return service.spark();

    }
}

