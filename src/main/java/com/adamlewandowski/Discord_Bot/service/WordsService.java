package com.adamlewandowski.Discord_Bot.service;

import com.adamlewandowski.Discord_Bot.model.dao.DictionaryDao;
import com.adamlewandowski.Discord_Bot.model.dto.PointedWordDto;
import com.adamlewandowski.Discord_Bot.persistance.DictionaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class WordsService {

    private final DictionaryRepository dictionaryRepository;

    @PostConstruct
    public void initializeWords() {
        addWordToDb(new PointedWordDto("thanks", 1));
        addWordToDb(new PointedWordDto("thks", 1));
        addWordToDb(new PointedWordDto("fuck", -1));
    }

    public void addWordToDb(PointedWordDto pointedWordDto) {
        DictionaryDao dictionaryDao = convertDtoToDao(pointedWordDto);
        dictionaryRepository.save(dictionaryDao);
    }

    private DictionaryDao convertDtoToDao(PointedWordDto pointedWordDto) {
        DictionaryDao dictionaryDao = new DictionaryDao();
        dictionaryDao.setWord(pointedWordDto.getWord());
        dictionaryDao.setPower(pointedWordDto.getPower());
        return dictionaryDao;
    }
}
