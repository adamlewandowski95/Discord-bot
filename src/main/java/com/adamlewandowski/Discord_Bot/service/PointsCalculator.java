package com.adamlewandowski.Discord_Bot.service;

import com.adamlewandowski.Discord_Bot.model.dao.DictionaryDao;
import com.adamlewandowski.Discord_Bot.persistance.DictionaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PointsCalculator {
    private static final int FIFTEEN_MINUTES_IN_MILLISECONDS = 900000;
    private static final int ONE_MINUTE_IN_MILLISECONDS = 60000;
    private final DictionaryRepository dictionaryRepository;
    private static Map<String, Integer> wordMap = new HashMap<>();

    public Integer checkPointsForMessage(String message) {
        int finalPoints = 0;
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

    @Scheduled(fixedRate = FIFTEEN_MINUTES_IN_MILLISECONDS)
    private void updateWordsMap(){
        List<DictionaryDao> dictionaryDaos = dictionaryRepository.findAll();
        dictionaryDaos.forEach(p -> wordMap.putIfAbsent(p.getWord(), p.getPower()));
    }
}