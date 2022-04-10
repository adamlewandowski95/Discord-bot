package com.adamlewandowski.Discord_Bot.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DiscordBotConfiguration {

    @Value("${token}")
    @Getter
    private String token;

    @Getter
    private List<String> authorizedRoles = List.of("MOD", "Headquarter");

    public void addAuthorizedRole(String role){
        authorizedRoles.add(role);
    }

    public void removeAuthorizedRole(String role){
        authorizedRoles.remove(role);
    }
}
