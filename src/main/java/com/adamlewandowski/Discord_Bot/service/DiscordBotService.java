package com.adamlewandowski.Discord_Bot.service;

import com.adamlewandowski.Discord_Bot.configuration.DiscordBotConfiguration;
import com.adamlewandowski.Discord_Bot.listeners.MessageListeners;
import com.adamlewandowski.Discord_Bot.listeners.ReactionListener;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.security.auth.login.LoginException;

@Service
@RequiredArgsConstructor
public class DiscordBotService {
    private final MessageListeners messageListeners;
    private final ReactionListener reactionListener;

    private final DiscordBotConfiguration discordBotConfiguration;

    @PostConstruct
    public void init() throws LoginException {
        JDABuilder.createDefault(discordBotConfiguration.getToken())
                .setActivity(Activity.watching("Watching You!"))
                .setStatus(OnlineStatus.ONLINE)
                .addEventListeners(messageListeners)
                .addEventListeners(reactionListener)
                .build();
    }
}