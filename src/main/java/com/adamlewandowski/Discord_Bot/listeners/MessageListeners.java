package com.adamlewandowski.Discord_Bot.listeners;

import com.adamlewandowski.Discord_Bot.model.DiscordUser;
import com.adamlewandowski.Discord_Bot.persistance.DiscordPointsRepository;
import com.adamlewandowski.Discord_Bot.service.PointsCalculator;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class MessageListeners extends ListenerAdapter {
    private final DiscordPointsRepository discordPointsRepository;
    private final PointsCalculator pointsCalculator;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getMember().getEffectiveName().equals("Najs Bot")) {
            return;
        }
        String message = event.getMessage().getContentRaw().toLowerCase();
        checkWords(message, event);
        checkPing(message, event);
    }

    private void checkWords(String userMessage, MessageReceivedEvent event) {
        Member member = event.getMember();
        Long userDiscordId = member.getIdLong();

        Optional<DiscordUser> userFromRepo = discordPointsRepository.findByUserId(userDiscordId);
        DiscordUser discordUser = userFromRepo.orElseGet(() -> DiscordUser.builder()
                .userId(userDiscordId)
                .userName(member.getEffectiveName())
                .points(0)
                .build());

        Integer points = pointsCalculator.calculatePoints(userMessage);
        discordUser.setPoints(discordUser.getPoints() + points);
        discordPointsRepository.save(discordUser);
    }

    private void checkPing(String message, MessageReceivedEvent event) {
        if (message.equals("!ping")) {
            MessageChannel channel = event.getChannel();
            long time = System.currentTimeMillis();
            channel.sendMessage("Pong!")
                    .queue(response -> response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time)
                            .queue());
        }
    }
}