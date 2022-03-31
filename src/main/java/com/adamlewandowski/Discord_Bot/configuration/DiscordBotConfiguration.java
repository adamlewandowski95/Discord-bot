package com.adamlewandowski.Discord_Bot.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscordBotConfiguration {

    @Value("${token}")
    @Getter
    private String token;
}
