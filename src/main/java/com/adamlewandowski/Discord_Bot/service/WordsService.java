package com.adamlewandowski.Discord_Bot.service;

import com.adamlewandowski.Discord_Bot.model.dao.DictionaryDao;
import com.adamlewandowski.Discord_Bot.model.dto.PointedWordDto;
import com.adamlewandowski.Discord_Bot.persistance.DictionaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WordsService {

    private final DictionaryRepository dictionaryRepository;

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
