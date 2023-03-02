package com.learnreactiveprogramming.imperative;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ImperativeExample {

    public static void main(String[] args) {
        var namesList = List.of("alex","ben","chole","adam");
        var newNamesList = namesGreaterThanSize(namesList, 3);
        System.out.println("newNameList : "+newNamesList);
    }

    private static List<String> namesGreaterThanSize(List<String> namesList, int i) {
        List<String> newNameList = new ArrayList<>();
        for (String name: namesList) {
            if(name.length() > 3 && !newNameList.contains(name) ){
                newNameList.add(name.toUpperCase());
            }
        }
        return newNameList;
    }


}
