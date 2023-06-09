package com.learnreactiveprogramming.functional;

import java.util.List;
import java.util.stream.Collectors;

public class FunctionalExample {
    public static void main(String[] args) {
        var namesList = List.of("alex","ben","chole","adam");
        var newNamesList = namesGreaterThanSize(namesList, 3);
        System.out.println("newNameList : "+newNamesList);
    }

    private static List<String> namesGreaterThanSize(List<String> namesList, int i) {
        return  namesList.stream()
                .filter(s->s.length() >3)
                .map(String::toUpperCase)
                .distinct()
                .collect(Collectors.toList());
    }
}
