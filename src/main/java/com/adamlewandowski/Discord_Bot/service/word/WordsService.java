package com.adamlewandowski.Discord_Bot.service.word;

import com.adamlewandowski.Discord_Bot.model.DiscordPoints;
import com.adamlewandowski.Discord_Bot.persistance.DailyLogRepository;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WordsService {

    private final DailyLogRepository dailyLogRepository;
    private final PointsCalculator pointsCalculator;

    public void checkMessageForGoodAndBadWords(MessageReceivedEvent event) {
        Member member = event.getMember();
        Long userDiscordId = member.getIdLong();
        String message = event.getMessage().getContentRaw().toLowerCase();

        Integer pointsForMessage = pointsCalculator.checkPointsForMessage(message);

        if (pointsForMessage > 0 && dailyLogRepository.findOneByUserId(userDiscordId).isPresent()) {
            Optional<LocalDateTime> dateOfLastUserPost = dailyLogRepository.getDateOfLastPositiveUserPoints(userDiscordId);
            if (dateOfLastUserPost.isPresent() && LocalDateTime.now().isBefore(dateOfLastUserPost.get().plusMinutes(15))) {
                return;
            }
        }
        DiscordPoints discordPoints = DiscordPoints.builder()
                .userId(userDiscordId)
                .userName(member.getEffectiveName())
                .points(pointsForMessage)
                .date(LocalDateTime.now())
                .build();

        dailyLogRepository.save(discordPoints);
    }
}