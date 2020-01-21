package com.example.university.distributeddatabase.service;

import com.example.university.distributeddatabase.pojo.MyTuple;
import com.sun.rowset.internal.Row;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class WordCountService {

    @Autowired
    JavaSparkContext sc;

    public List<MyTuple> spark() {
        System.setProperty("hadoop.home.dir", "C:\\2\\");
        JavaRDD<String> inputFile = sc.textFile("C:\\2\\logfile.txt");
        JavaRDD<String> wordsFromFile = inputFile.flatMap(content -> Arrays.asList(content.split(" ")));
        JavaPairRDD countData = wordsFromFile.mapToPair(t -> new Tuple2(t, 1)).reduceByKey((x, y) -> (int) x + (int) y);
        List<MyTuple> myTuples = new ArrayList<>();
        for (Object o : countData.collect()) {
            MyTuple myTuple = new MyTuple();
            myTuple.setWordName(((Tuple2) o)._1.toString());
            myTuple.setNumber(((Tuple2) o)._2.toString());
            myTuples.add(myTuple);
        }
        return myTuples;
    }

    public List<MyTuple> search(String wordSearch) {
        JavaRDD lines = sc.textFile("C:\\2\\logfile.txt")
                .filter(s -> s.contains("wordSearch"));
        Long numErrors = lines.count();
        MyTuple myTuple = new MyTuple();
        myTuple.setWordName(wordSearch);
        myTuple.setNumber(numErrors.toString());
        ArrayList<MyTuple> myTuples = new ArrayList<>();
        myTuples.add(myTuple);
        return myTuples;
    }
}
