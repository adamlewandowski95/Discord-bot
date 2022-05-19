package com.adamlewandowski.Discord_Bot.service;

import com.adamlewandowski.Discord_Bot.model.DailyPoints;
import com.adamlewandowski.Discord_Bot.model.dao.DictionaryDao;
import com.adamlewandowski.Discord_Bot.model.dto.PointedWordDto;
import com.adamlewandowski.Discord_Bot.persistance.DictionaryRepository;
import com.adamlewandowski.Discord_Bot.persistance.DailyLogRepository;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WordsService {

    private final DictionaryRepository dictionaryRepository;
    private final DailyLogRepository dailyLogRepository;
    private final PointsCalculator pointsCalculator;

    @PostConstruct
    public void initializeWords() {
        addWordToDb(new PointedWordDto("thanks", 1));
        addWordToDb(new PointedWordDto("thks", 1));
        addWordToDb(new PointedWordDto("fuck", -1));
    }

    public void checkMessageForGoodAndBadWords(MessageReceivedEvent event) {
        Member member = event.getMember();
        Long userDiscordId = member.getIdLong();
        String message = event.getMessage().getContentRaw().toLowerCase();

        Integer pointsForMessage = pointsCalculator.checkPointsForMessage(message);

        if (pointsForMessage > 0 && dailyLogRepository.findOneByUserId(userDiscordId).isPresent()) {
            LocalDateTime dateOfLastUserPost = dailyLogRepository.getDateOfLastUserPost(userDiscordId);
            if (LocalDateTime.now().isBefore(dateOfLastUserPost.plusMinutes(15))) {
                return;
            }
        }
        DailyPoints dailyPoints = DailyPoints.builder()
                .userId(userDiscordId)
                .userName(member.getEffectiveName())
                .points(pointsForMessage)
                .date(LocalDateTime.now())
                .build();

        dailyLogRepository.save(dailyPoints);

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