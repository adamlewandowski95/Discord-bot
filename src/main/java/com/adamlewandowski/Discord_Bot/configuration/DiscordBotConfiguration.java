package com.adamlewandowski.Discord_Bot.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

@Configuration
public class DiscordBotConfiguration {

    //todo // zmienić nazwę bota
    @Getter
    private String botName = "Najs Bot";

    @Value("${token}")
    @Getter
    private String token;

    @Getter
    private List<String> authorizedRoles = List.of("MOD", "Headquarter");

    @Getter
    private Map<String, List<String>> listenedCategories = Map.of( //dokładne nazwy kategorii i kanałów (razem z myślnikami)
            "World", emptyList(),
            "----OFF - TOPIC----", emptyList(),
            "Casper army", List.of("discussion", "suggestions"));

    public void addAuthorizedRole(String role) {
        authorizedRoles.add(role);
    }

    public void removeAuthorizedRole(String role) {
        authorizedRoles.remove(role);
    }
}
