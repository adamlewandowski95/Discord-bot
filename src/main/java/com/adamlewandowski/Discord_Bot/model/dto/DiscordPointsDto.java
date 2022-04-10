package com.adamlewandowski.Discord_Bot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DiscordPointsDto {

    private String name;
    private long discordPoints;

    @Override
    public String toString() {
        return "Nick: " + name + " | discord points: " + discordPoints + "\n";
    }
}
