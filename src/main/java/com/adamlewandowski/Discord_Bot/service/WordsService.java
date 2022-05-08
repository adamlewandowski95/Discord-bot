package com.adamlewandowski.Discord_Bot.service;

import com.adamlewandowski.Discord_Bot.model.DiscordLogPoints;
import com.adamlewandowski.Discord_Bot.model.DiscordUserPoints;
import com.adamlewandowski.Discord_Bot.model.dao.DictionaryDao;
import com.adamlewandowski.Discord_Bot.model.dto.PointedWordDto;
import com.adamlewandowski.Discord_Bot.persistance.DictionaryRepository;
import com.adamlewandowski.Discord_Bot.persistance.DiscordLogRepository;
import com.adamlewandowski.Discord_Bot.persistance.DiscordUserRepository;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WordsService {

    private final DictionaryRepository dictionaryRepository;
    private final DiscordUserRepository discordUserRepository;
    private final DiscordLogRepository discordLogRepository;
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

        // To powinno być w metodzie podliczającej punkty chyba z discord log do discord points
        Optional<DiscordUserPoints> userFromRepo = discordUserRepository.findByUserId(userDiscordId);
        if (!userFromRepo.isPresent()) {
            DiscordUserPoints newDiscordUser = DiscordUserPoints.builder()
                    .userId(userDiscordId)
                    .userName(member.getEffectiveName())
                    .allPoints(0)
                    .build();

            discordUserRepository.save(newDiscordUser);
//            DiscordUserPoints discordUserPoints = userFromRepo.orElseGet(() -> DiscordUserPoints.builder()
//                    .userId(userDiscordId)
//                    .userName(member.getEffectiveName())
//                    .allPoints(0)
//                    .build());
        }
        // %%%%%%%%%%%%%%%%

        Integer pointsForMessage = pointsCalculator.checkPointsForMessage(message);
//        discordUserPoints.addPoints(pointsForMessage);
//        discordPointsRepository.save(discordUserPoints);
        if (pointsForMessage != 0) {
            DiscordLogPoints discordLogPoints = DiscordLogPoints.builder()
                    .userId(userDiscordId)
                    .userName(member.getEffectiveName())
                    .points(pointsForMessage)
                    .date(LocalDateTime.now())
                    .build();

            discordLogRepository.save(discordLogPoints);
        }
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
