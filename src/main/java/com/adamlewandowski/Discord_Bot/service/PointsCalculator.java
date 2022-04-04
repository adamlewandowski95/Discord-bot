package com.adamlewandowski.Discord_Bot.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PointsCalculator {
    private static Map<String, Integer> wordMap = new HashMap<>();

    static {
        wordMap.put("thanks", 1);
        wordMap.put("thx", 1);
        wordMap.put("thks", 1);
        wordMap.put("fuck", -1);
    }

    public Integer calculatePoints(String message) {
        Integer finalPoints = 0;
        Integer reduce = Arrays.stream(message.split(" "))
                .map(p -> wordMap.getOrDefault(p, 0))
                .toList()
                .stream()
                .reduce(0, Integer::sum);

        if (reduce > 0) {
            finalPoints = 1;
        }
        if (reduce < 0) {
            finalPoints = -1;
        }
        return finalPoints;
    }
}