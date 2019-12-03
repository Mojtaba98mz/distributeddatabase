package com.example.university.distributeddatabase.util;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class Utils {
    private static ArrayList<Integer> randomNumbers;

    static {
        Random random = new Random();
        randomNumbers = (ArrayList<Integer>) random.ints(0, 1000).limit(100000).boxed().collect(Collectors.toList());
    }

    public static ArrayList<Integer> getRandomNumbers() {
        return randomNumbers;
    }

    public static int coreNumbers() {
        return Runtime.getRuntime().availableProcessors();
    }
}
