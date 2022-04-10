package com.adamlewandowski.Discord_Bot.controller;

import com.adamlewandowski.Discord_Bot.model.dto.PointedWordDto;
import com.adamlewandowski.Discord_Bot.service.WordsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/words")
@RequiredArgsConstructor
public class WordsController {

    private final WordsService wordsService;

    @PostMapping
    public void createPointedWord(@RequestBody PointedWordDto pointedWordDto) {
        wordsService.addWordToDb(pointedWordDto);
    }
}