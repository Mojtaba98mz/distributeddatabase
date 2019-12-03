package com.example.university.distributeddatabase.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {
    public static ArrayList<Integer> getRandomNumbers() {
        Random random = new Random();
        return (ArrayList<Integer>) random.ints(0, 1000).limit(1000).boxed().collect(Collectors.toList());
    }
    public static int[] toIntArray(List<Integer> list){
        int[] ret = new int[list.size()];
        for(int i = 0;i < ret.length;i++)
            ret[i] = list.get(i);
        return ret;
    }
}
