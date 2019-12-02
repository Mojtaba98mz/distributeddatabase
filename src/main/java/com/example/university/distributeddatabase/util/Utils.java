package com.example.university.distributeddatabase.util;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {
    public static ArrayList<Integer> getRandomNumbers() {
        Random random = new Random();
        return (ArrayList<Integer>) random.ints(0, 1000).limit(1000).boxed().collect(Collectors.toList());
    }
}
