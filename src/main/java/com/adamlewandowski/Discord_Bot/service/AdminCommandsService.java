package com.adamlewandowski.Discord_Bot.service;

import com.adamlewandowski.Discord_Bot.configuration.DiscordBotConfiguration;
import com.adamlewandowski.Discord_Bot.model.DiscordLogPoints;
import com.adamlewandowski.Discord_Bot.model.DiscordUserPoints;
import com.adamlewandowski.Discord_Bot.persistance.DiscordLogRepository;
import com.adamlewandowski.Discord_Bot.persistance.DiscordUserRepository;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminCommandsService {
    private final DiscordBotConfiguration discordBotConfiguration;
    private final DiscordUserRepository discordUserRepository;
    private final DiscordLogRepository discordLogRepository;

    public void checkAdminCommands(MessageReceivedEvent event) {
        Member member = event.getMember();
        List<Role> roles = member.getRoles();
        String message = event.getMessage().getContentRaw();
        for (String role : discordBotConfiguration.getAuthorizedRoles()) {
            if (roles.toString().contains(role)) {
                if (message.contains("/addpoint")) {
                    changeUserPoints(message, true);
                }
                if (message.contains("/subpoint")) {
                    changeUserPoints(message, false);
                }
                System.out.println("Authorized");
            }
        }
    }

    private void changeUserPoints(String message, boolean isPositivePoints) {
        String[] splitString = message.split(" ");
        String userName = splitString[1];
        int userPointsFromAdmin = Integer.parseInt(splitString[2]);

        Optional<DiscordUserPoints> byUserName = discordUserRepository.findByUserName(userName);
        if (byUserName.isPresent()) {
            DiscordUserPoints discordUserPoints = byUserName.orElseThrow(() ->
                    new EntityNotFoundException("Bad username or user is not in Db (he should write a message at least once)"));
            if (isPositivePoints) {

//                discordUserPoints.addPoints(userPointsFromAdmin);
            } else {
                userPointsFromAdmin = userPointsFromAdmin * (-1);
//                discordUserPoints.subtractPoints(userPointsFromAdmin);
            }
            DiscordLogPoints discordLogPoints = DiscordLogPoints.builder()
                    .userId(discordUserPoints.getUserId())
                    .userName(discordUserPoints.getUserName())
                    .points(userPointsFromAdmin)
                    .date(LocalDateTime.now())
                    .build();

            discordLogRepository.save(discordLogPoints);

//            discordPointsRepository.save(discordUserPoints);
        }
    }
}
