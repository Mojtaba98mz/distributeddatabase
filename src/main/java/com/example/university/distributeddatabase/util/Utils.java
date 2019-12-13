package com.example.university.distributeddatabase.util;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

import static com.example.university.distributeddatabase.util.Constant.MAX_NUMBER;

public class Utils {
    private static ArrayList<Integer> randomNumbers;

    static {
        Random random = new Random();
        randomNumbers = (ArrayList<Integer>) random.ints(0, 10000).limit(1000000).boxed().collect(Collectors.toList());
    }

    public static ArrayList<Integer> getRandomNumbers() {
        return randomNumbers;
    }

    public static int coreNumbers() {
        return Runtime.getRuntime().availableProcessors();
    }

}
