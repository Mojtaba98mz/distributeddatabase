package com.example.university.distributeddatabase;

import com.example.university.distributeddatabase.util.MergeSort;
import com.example.university.distributeddatabase.util.Utils;

import java.util.ArrayList;

public class Main {


    public static void main(String[] args) {
        ArrayList<Integer> unsortedArray = Utils.getRandomNumbers();
        MergeSort ms = new MergeSort(unsortedArray);

        ms.sortGivenArray();

        System.out.println("\n------------Sorted Array------------");
        for (int i : ms.getSortedArray()) {
            System.out.print(i + " ");
        }


    }
}
