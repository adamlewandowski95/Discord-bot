package com.adamlewandowski.Discord_Bot.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

@Configuration
public class BotConfiguration {

    //todo // zmienić nazwę bota
    @Getter
    private String botName = "Najs Bot";

    @Value("${token}")
    @Getter
    private String token;

    @Getter
    private List<String> authorizedRoles = List.of("MOD", "Headquarter");

    //Kluczem jest kategoria (obowiązkowa) kluczem jest lista kanalów nasłuchiwanych. Jeżeli jest empty list to nasłuchuje wszystkie
    @Getter
    private Map<String, List<String>> listenedCategories = Map.of( //dokładne nazwy kategorii i kanałów (razem z myślnikami)
            "World", emptyList(),
            "Text Channels", emptyList(),
            "----OFF - TOPIC----", emptyList(),
            "Casper army", List.of("discussion", "suggestions"));

    public void addAuthorizedRole(String role) {
        authorizedRoles.add(role);
    }

    public void removeAuthorizedRole(String role) {
        authorizedRoles.remove(role);
    }
}
