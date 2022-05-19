package com.adamlewandowski.Discord_Bot.service;

import com.adamlewandowski.Discord_Bot.configuration.BotConfiguration;
import com.adamlewandowski.Discord_Bot.model.DailyPoints;
import com.adamlewandowski.Discord_Bot.persistance.DailyLogRepository;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCommandsService {
    private final BotConfiguration botConfiguration;
    private final DailyLogRepository dailyLogRepository;

    public void checkAdminCommands(MessageReceivedEvent event) {
        List<Role> memberRoles = event.getMember().getRoles();
        String message = event.getMessage().getContentRaw();
        for (String role : botConfiguration.getAuthorizedRoles()) {
            if (memberRoles.toString().contains(role)) {
                if (message.contains("/addpoint") || message.contains("/subpoint")) {
                    savePointsToDailyLog(message);
                }
                break;
            }
        }
    }

    private void savePointsToDailyLog(String message) {
        String[] splitString = message.split(" ");
        String discordMember = splitString[1];
        int userPointsFromAdmin = Integer.parseInt(splitString[2]);
        if (message.contains("/subpoint")) {
            userPointsFromAdmin = userPointsFromAdmin * -1;
        }
        DailyPoints dailyPoints = DailyPoints.builder()
                .userName(discordMember)
                .points(userPointsFromAdmin)
                .date(LocalDateTime.now())
                .build();
        dailyLogRepository.save(dailyPoints);
    }
}