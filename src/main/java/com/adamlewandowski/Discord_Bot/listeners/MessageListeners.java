package com.adamlewandowski.Discord_Bot.listeners;

import com.adamlewandowski.Discord_Bot.configuration.DiscordBotConfiguration;
import com.adamlewandowski.Discord_Bot.model.DiscordUser;
import com.adamlewandowski.Discord_Bot.model.dto.DiscordPointsDto;
import com.adamlewandowski.Discord_Bot.persistance.DiscordPointsRepository;
import com.adamlewandowski.Discord_Bot.service.PointsCalculator;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class MessageListeners extends ListenerAdapter {
    private final DiscordPointsRepository discordPointsRepository;
    private final PointsCalculator pointsCalculator;
    private final DiscordBotConfiguration discordBotConfiguration;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getMember().getEffectiveName().equals("Najs Bot")) {
            return;
        }
        checkMessageForGoodAndBadWords(event);
        checkAdminCommands(event);
        checkPing(event);
        checkMyPoints(event);
        checkTopLists(event);
        addPointFromUserToUser(event);
    }

    private void addPointFromUserToUser(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        MessageChannel channel = event.getChannel();
        if (message.equals("+")) {
            channel.sendMessage("" + event.getResponseNumber())
                    .queue();
        }
    }

    private void checkTopLists(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (message.contains("/bottomrep")) {
            showTopUsers(event, "asc");
        } else if (message.contains("/toprep")) {
            showTopUsers(event, "desc");
        }
    }

    private void showTopUsers(MessageReceivedEvent event, String sort) {
        String message = event.getMessage().getContentRaw();
        MessageChannel channel = event.getChannel();
        String[] splitString = message.split(" ");
        Integer numberOfUsers = Integer.parseInt(splitString[1]);
        List<DiscordUser> usersList;
        if (sort.equals("desc")) {
            usersList = discordPointsRepository.findUsersWithBestReputation(numberOfUsers); // nie wiem jak przekazać Sort.Direction do psql, nie moge sobie ztym poradzic
        } else {
            usersList = discordPointsRepository.findUsersWithWorstReputation(numberOfUsers);
        }
        List<DiscordPointsDto> discordPointsDtos = usersList.stream()
                .map(p -> new DiscordPointsDto(p.getUserName(), p.getPoints()))
                .toList();
        channel.sendMessage(String.format(discordPointsDtos.toString())).queue();
    }

    private void checkMyPoints(MessageReceivedEvent event) {
        Member member = event.getMember();
        Long userDiscordId = member.getIdLong();
        String message = event.getMessage().getContentRaw();
        MessageChannel channel = event.getChannel();
        Integer currentPoints = 0;
        if (message.contains("/me")) {
            Optional<DiscordUser> byUserName = discordPointsRepository.findByUserId(userDiscordId);
            if (byUserName.isPresent()) {
                currentPoints = byUserName.get().getPoints();
            }
            channel.sendMessage(String.format("You currently have %s points!", currentPoints)).queue();
        }
    }

    private void checkAdminCommands(MessageReceivedEvent event) {
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
        Optional<DiscordUser> byUserName = discordPointsRepository.findByUserName(userName);
        DiscordUser discordUser = byUserName.orElseThrow(() -> new EntityNotFoundException("Bad username or user is not in Db (he should write a message at least once)"));
        if (isPositivePoints) {
            discordUser.addPoints(userPointsFromAdmin);
        } else {
            discordUser.subtractPoints(userPointsFromAdmin);
        }
        discordPointsRepository.save(discordUser);
    }

    private void checkMessageForGoodAndBadWords(MessageReceivedEvent event) {
        Member member = event.getMember();
        Long userDiscordId = member.getIdLong();
        String message = event.getMessage().getContentRaw().toLowerCase();

        Optional<DiscordUser> userFromRepo = discordPointsRepository.findByUserId(userDiscordId);
        DiscordUser discordUser = userFromRepo.orElseGet(() -> DiscordUser.builder()
                .userId(userDiscordId)
                .userName(member.getEffectiveName())
                .points(0)
                .build());

        Integer pointsForMessage = pointsCalculator.checkPointsForMessage(message);
        discordUser.addPoints(pointsForMessage);
        discordPointsRepository.save(discordUser);
    }

    private void checkPing(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw().toLowerCase();
        if (message.equals("!ping")) {
            MessageChannel channel = event.getChannel();
            long time = System.currentTimeMillis();
            channel.sendMessage("Pong!")
                    .queue(response -> response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time)
                            .queue());
        }
    }
}